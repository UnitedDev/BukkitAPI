package fr.kohei.command.impl;

import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.common.RedisProvider;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.DiscordWebhook;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.management.ManagementFactory;

public class PlayerCommands {

    @Command(names = "chatreport")
    public static void report(Player sender, @Param(name = "name") String name, @Param(name = "message", wildcard = true) String message) {
        if (!name.startsWith("name:")) return;
        if (!message.startsWith("message:")) return;

        name = name.replaceFirst("name:", "");
        message = message.replaceFirst("message:", "");

        if (BukkitAPI.getChatReportManager().getCooldown().getOrDefault(sender.getUniqueId(), false)) {
            sender.sendMessage(ChatUtil.prefix("&cVeuillez attendre 120 secondes avant de report un autre message."));
            return;
        }

        BukkitAPI.getChatReportManager().setCooldown(sender.getUniqueId());
        sender.sendMessage(ChatUtil.prefix("&fVous avez &6report &fle message de &a" + name));

        DiscordWebhook webhook = new DiscordWebhook(DiscordWebhook.REPORT_CHANNEL);
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Nouveau Report")
                .setDescription("**Message:** " + message)
                .addField("Fautif", name, true)
                .addField("Joueurs", sender.getName(), true)
                .setFooter("Chat Report", DiscordWebhook.LOGO_URL)
        );
        webhook.execute();
    }

    @Command( names = {"ping"})
    public static void execute(Player sender, @Param(name = "target", defaultValue = "self") Player target) {
        if(target == sender) {
            sender.sendMessage(ChatUtil.translate("&7▎ &fVotre Ping: &a" + getPing(sender) + "ms&f."));
        } else {
            sender.sendMessage(ChatUtil.translate("&7▎ &fLe ping de &a" + target.getName() + " &fest de &a" + getPing(target) + "ms&f."));
            sender.sendMessage(ChatUtil.translate("&7▎ &fDifference de ping: &a" + (Math.max(getPing(sender), getPing(target)) - Math.min(getPing(sender), getPing(target)) + "ms") + "&f."));
        }
    }

    @Command(names = "hub")
    public static void hub(Player sender) {
        if (BukkitAPI.getServerCache().findBestLobbyFor(sender.getUniqueId()) == null) BukkitAPI.sendToServer(sender, "Limbo");
        else BukkitAPI.sendToServer(sender, BukkitAPI.getFactory(BukkitAPI.getServerCache().findBestLobbyFor(sender.getUniqueId()).getPort()).getName());
    }

    public static int getPing(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        return entityPlayer.ping;
    }

    @Command(names = {"lag", "tps"}, power = 39)
    public static void execute(Player sender) {
        StringBuilder sb = new StringBuilder(" ");
        for (double tps : MinecraftServer.getServer().recentTps)
        {
            sb.append(format(tps));
            sb.append( ", " );
        }

        long serverTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        String uptime = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - serverTime, true, true);

        String tps = sb.substring( 0, sb.length() - 2 );

        sender.sendMessage(" ");
        sender.sendMessage(ChatUtil.translate("&6&lServer Info&7:"));
        sender.sendMessage(ChatUtil.translate(" &7* &eTPS&7: &6" + tps));
        sender.sendMessage(ChatUtil.translate(" &7* &eUptime&7: &6" + uptime));
        sender.sendMessage(ChatUtil.translate(" &7* &eMemoire Max&7: &6" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " &eMB"));
        sender.sendMessage(ChatUtil.translate(" &7* &eMemoire Alloué&7: &6" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + " &eMB"));
        sender.sendMessage(ChatUtil.translate(" &7* &eMemoire Libre&7: &6" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + " &eMB"));
        sender.sendMessage("");
        sender.sendMessage(ChatUtil.translate("&e&lMondes&7:"));

        for(World world : Bukkit.getWorlds()) {
            sender.sendMessage(ChatUtil.translate(" &7* &6" + world.getName() + "&7: &eChunks Load&7: &6" + world.getLoadedChunks().length + "&7, &eEntités&7: &6" + world.getEntities().size()));
        }
        sender.sendMessage(" ");
    }
    static String format(double tps)
    {
        return ( ( tps > 18.0 ) ? ChatColor.GREEN : ( tps > 16.0 ) ? ChatColor.YELLOW : ChatColor.RED )
                + ( ( tps > 20.0 ) ? "*" : "" ) + Math.min( Math.round( tps * 100.0 ) / 100.0, 20.0 );
    }

}
