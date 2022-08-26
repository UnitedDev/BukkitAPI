package fr.uniteduhc;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import fr.uniteduhc.command.CommandHandler;
import fr.uniteduhc.command.impl.ModCommands;
import fr.uniteduhc.command.impl.PlayerCommands;
import fr.uniteduhc.common.api.CommonAPI;
import fr.uniteduhc.common.cache.data.Division;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.manager.ChatReportManager;
import fr.uniteduhc.manager.PunishmentManager;
import fr.uniteduhc.menu.MenuAPI;
import fr.uniteduhc.messaging.subscriber.*;
import fr.uniteduhc.staff.StaffManager;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.item.CustomItemListener;
import fr.uniteduhc.messaging.packet.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BukkitAPI extends JavaPlugin implements PluginMessageListener {

    @Getter
    private static JavaPlugin plugin;
    @Getter
    private static MenuAPI menuAPI;
    @Getter
    private static CommandHandler commandHandler;
    @Getter
    private static CommonAPI commonAPI;
    @Getter
    private static ChatReportManager chatReportManager;
    @Getter
    private static PunishmentManager punishmentManager;
    @Getter
    private static StaffManager staffManager;

    @Getter
    @Setter
    private static int normalPlayers;
    @Getter
    @Setter
    private static int totalPlayers;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        commonAPI = CommonAPI.create();
        menuAPI = new MenuAPI(plugin);
        commandHandler = new CommandHandler(plugin);
        chatReportManager = new ChatReportManager();
        punishmentManager = new PunishmentManager();
        staffManager = new StaffManager();

        plugin.getServer().getPluginManager().registerEvents(new CustomItemListener(plugin), plugin);
        this.loadRedis();
        this.registerCommands();
        this.registerListeners();

        this.getServer().getScheduler().runTaskLater(this, CommandHandler::deleteCommands, 2 * 20);
    }

    public static ServiceInfoSnapshot getFactory(int port) {
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices().stream()
                .filter(service -> service.getAddress().getPort() == port)
                .findFirst().orElse(null);
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
    }

    private void registerCommands() {
        commandHandler.registerClass(ModCommands.class);
        commandHandler.registerClass(PlayerCommands.class);
    }

    private void loadRedis() {
        commonAPI.getMessaging().registerAdapter(CountUpdatePacket.class, new CountUpdateSubscriber());
        commonAPI.getMessaging().registerAdapter(ServerDeletePacket.class, new ServerDeleteSubscriber());
        commonAPI.getMessaging().registerAdapter(PunishmentAskPacket.class, new PunishmentAskSubscriber());
        commonAPI.getMessaging().registerAdapter(PunishmentPacket.class, new PunishmentSubscriber());
        commonAPI.getMessaging().registerAdapter(UpdatePlayersPacket.class, new UpdatePlayersSubscriber());
        commonAPI.getMessaging().registerAdapter(AttemptLinkPacket.class, new AttemptLinkSubscriber());
        commonAPI.getMessaging().registerAdapter(LinkSuccessPacket.class, null);
        commonAPI.getMessaging().registerAdapter(MessagePacket.class, new MessageSubscriber());
    }

    public void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        final ByteArrayDataInput in = ByteStreams.newDataInput(message);
        in.readUTF();
    }

    public static void sendToServer(Player player, String server) {
        final ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(getPlugin(), "BungeeCord", out.toByteArray());
    }

    public static void changeExperience(Player player, int add, String reason) {
        ProfileData profile = commonAPI.getProfile(player.getUniqueId());

        profile.setExperience(profile.getExperience() + add);
        player.sendMessage(ChatUtil.prefix("&f+&c" + add + " xp &7(&c" + reason + "&7)"));
        updateExperience(player);
    }

    private static void updateExperience(Player player) {
        ProfileData profile = getCommonAPI().getProfile(player.getUniqueId());

        Division division = profile.getDivision();

        if (profile.getExperience() >= division.getExperience()) {
            profile.setExperience(0);
            profile.setDivision(nextDivision(profile.getDivision()));

            division.getConsumer().accept(profile);
            player.sendMessage(ChatUtil.prefix("&fVous êtes désormais dans la division " + nextDivision(division).getDisplay()));
            player.sendMessage(ChatUtil.prefix(division.getMessage()));
        }
    }

    public static Division nextDivision(Division division) {
        return Division.values()[(division.ordinal() + 1) % Division.values().length];
    }
}
