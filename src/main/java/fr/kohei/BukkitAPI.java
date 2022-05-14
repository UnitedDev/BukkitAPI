package fr.kohei;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import fr.kohei.command.CommandHandler;
import fr.kohei.command.impl.ModCommands;
import fr.kohei.command.impl.PlayerCommands;
import fr.kohei.common.api.CommonAPI;
import fr.kohei.common.cache.Division;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.manager.ChatReportManager;
import fr.kohei.manager.PunishmentManager;
import fr.kohei.manager.ServerCache;
import fr.kohei.menu.MenuAPI;
import fr.kohei.messaging.packet.*;
import fr.kohei.messaging.subscriber.*;
import fr.kohei.tasks.TabListTask;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.item.CustomItemListener;
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
    private static ServerCache serverCache;
    @Getter
    private static CommonAPI commonAPI;
    @Getter
    private static ChatReportManager chatReportManager;
    @Getter
    private static PunishmentManager punishmentManager;

    @Override
    public void onEnable() {
        plugin = this;

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        commonAPI = CommonAPI.create();
        menuAPI = new MenuAPI(plugin);
        commandHandler = new CommandHandler(plugin);
        serverCache = new ServerCache(plugin);
        chatReportManager = new ChatReportManager();
        punishmentManager = new PunishmentManager();

        plugin.getServer().getPluginManager().registerEvents(new CustomItemListener(plugin), plugin);
        this.loadRedis();
        this.registerCommands();
        this.registerListeners();

        this.getServer().getScheduler().runTaskLater(this, CommandHandler::deleteCommands, 5 * 20);
        new TabListTask(this).runTaskTimer(this, 0, 20);
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
        commonAPI.getMessaging().registerAdapter(LobbyUpdatePacket.class, new LobbyUpdateSubscriber());
        commonAPI.getMessaging().registerAdapter(UHCUpdatePacket.class, new UHCUpdateSubscriber());
        commonAPI.getMessaging().registerAdapter(CountUpdatePacket.class, new CountUpdateSubscriber());
        commonAPI.getMessaging().registerAdapter(ServerDeletePacket.class, new ServerDeleteSubscriber());
        commonAPI.getMessaging().registerAdapter(CTFUpdatePacket.class, new CTFUpdateSubscriber());
        commonAPI.getMessaging().registerAdapter(PunishmentAskPacket.class, new PunishmentAskSubscriber());
        commonAPI.getMessaging().registerAdapter(PunishmentPacket.class, new PunishmentSubscriber());
        commonAPI.getMessaging().registerAdapter(UpdatePlayersPacket.class, new UpdatePlayersSubscriber());
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

    @Getter
    @Setter
    private static int normalPlayers;
    @Getter
    @Setter
    private static int totalPlayers;
}
