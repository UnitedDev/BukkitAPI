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
import fr.kohei.messaging.Pidgin;
import fr.kohei.messaging.list.packet.*;
import fr.kohei.messaging.list.subscriber.*;
import fr.kohei.tasks.TabListTask;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.item.CustomItemListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import redis.clients.jedis.JedisPool;

public class BukkitAPI extends JavaPlugin implements PluginMessageListener {

    @Getter
    private static JavaPlugin plugin;
    @Getter
    private static MenuAPI menuAPI;
    @Getter
    private static CommandHandler commandHandler;
    @Getter
    private static Pidgin messaging;
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
        messaging = new Pidgin("dev", new JedisPool("localhost"));
        serverCache = new ServerCache(plugin);
        chatReportManager = new ChatReportManager();
        punishmentManager = new PunishmentManager();

        plugin.getServer().getPluginManager().registerEvents(new CustomItemListener(plugin), plugin);
        this.loadRedis();
        this.registerCommands();

        this.getServer().getScheduler().runTaskLater(this, CommandHandler::deleteCommands, 5 * 20);
        new TabListTask(this).runTaskTimer(this, 0, 20);
    }

    public static ServiceInfoSnapshot getFactory(int port) {
        return CloudNetDriver.getInstance().getCloudServiceProvider().getCloudServices().stream()
                .filter(service -> service.getAddress().getPort() == port)
                .findFirst().orElse(null);
    }

    public void registerCommands() {
        commandHandler.registerClass(ModCommands.class);
        commandHandler.registerClass(PlayerCommands.class);
    }

    private void loadRedis() {
        getMessaging().registerPacket(LobbyUpdatePacket.class);
        getMessaging().registerPacket(UHCUpdatePacket.class);
        getMessaging().registerPacket(CountUpdatePacket.class);
        getMessaging().registerPacket(ServerDeletePacket.class);
        getMessaging().registerPacket(CTFUpdatePacket.class);
        getMessaging().registerPacket(ProfileUpdatePacket.class);
        getMessaging().registerPacket(PunishmentAskPacket.class);
        getMessaging().registerPacket(PunishmentPacket.class);

        getMessaging().registerListener(new LobbyUpdateSubscriber());
        getMessaging().registerListener(new CTFUpdateSubscriber());
        getMessaging().registerListener(new UHCUpdateSubscriber());
        getMessaging().registerListener(new CountUpdateSubscriber());
        getMessaging().registerListener(new ServerDeleteSubscriber());
        getMessaging().registerListener(new PunishmentAskSubscriber());
        getMessaging().registerListener(new PunishmentSubscriber());
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
