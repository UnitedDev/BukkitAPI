package fr.uniteduhc.manager.listeners;

import com.lunarclient.bukkitapi.LunarClientAPI;
import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.common.CommonProvider;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.utils.ChatUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Arrays;
import java.util.Date;

public class PlayerListeners implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        String message = event.getMessage();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (message.contains(onlinePlayer.getName())) {
                ProfileData targetProfile = BukkitAPI.getCommonAPI().getProfile(onlinePlayer.getUniqueId());
                if (targetProfile.isNotifications()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
                }
                message = message.replace(onlinePlayer.getName(), "§b@" + onlinePlayer.getName() + "§f");
            }
        }

        TextComponent text = new TextComponent("§c⚠ ");
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatUtil.prefix("&cSignaler &l" + player.getName() + " &c(action effectuée au clic)"))}));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatreport confirm;name:" + player.getName() + " message:" + message.replace("§b", "").replace("§f", "")));
        String finalMessage = message;

        Bukkit.getOnlinePlayers().forEach(player1 -> player1.spigot().sendMessage(text, new TextComponent(
                profile.getRank().getTabPrefix().replace("&", "§") + " " + player.getName() + " §8§l» §f" + finalMessage
        )));

        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());
        if (profile.getDisplayName().equals("") || !profile.getDisplayName().equalsIgnoreCase(player.getDisplayName())) {
            profile.setDisplayName(player.getDisplayName());
            BukkitAPI.getCommonAPI().saveProfile(player.getUniqueId(), profile);
        }

        if (profile.getRank().permissionPower() >= 10) {
            profile.setHosts(-1);
            if (profile.getIps().contains(player.getAddress().getHostName())) {
                profile.getIps().add(player.getAddress().getHostName());
                BukkitAPI.getCommonAPI().saveProfile(player.getUniqueId(), profile);
            }
        }

        profile.setLastLogin(new Date());
        BukkitAPI.getCommonAPI().saveProfile(player.getUniqueId(), profile);

        if (LunarClientAPI.getInstance().isRunningLunarClient(player))
            Bukkit.getOnlinePlayers().stream()
                    .filter(player1 -> CommonProvider.getInstance().getProfile(player1.getUniqueId()).isStaff())
                    .forEach(player1 -> LunarClientAPI.getInstance().overrideNametag(player1, Arrays.asList(
                            ChatUtil.translate("&f(&cStaff Mod&f)"),
                            ChatUtil.translate(profile.getRank().getChatPrefix() + " " + player1.getName())
                    ), player));

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());
        profile.setPlayTime(profile.getPlayTime() - (profile.getLastLogin().getTime() - System.currentTimeMillis()));
        BukkitAPI.getCommonAPI().saveProfile(player.getUniqueId(), profile);

        if (BukkitAPI.getStaffManager().getFreezePlayers().contains(player.getUniqueId())) {
            BukkitAPI.getStaffManager().getFreezePlayers().remove(player.getUniqueId());
            Bukkit.getOnlinePlayers().stream()
                    .filter(player1 -> CommonProvider.getInstance().getProfile(player1.getUniqueId()).isStaff())
                    .forEach(player1 -> {
                        player1.sendMessage(ChatUtil.prefix("&c" + player.getName() + " &fa quitté le serveur en étant &cfreeze&f."));
                    });
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (profile.getRank().permissionPower() >= 1000) {
            player.setOp(true);
        }

        if (BukkitAPI.getPunishmentManager().getBan(player.getUniqueId()) != null) {
            event.setKickMessage(BukkitAPI.getPunishmentManager().getKickMessage(BukkitAPI.getPunishmentManager().getBan(player.getUniqueId())));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
        if (BukkitAPI.getPunishmentManager().getBlacklist(player.getUniqueId()) != null) {
            event.setKickMessage(BukkitAPI.getPunishmentManager().getKickMessage(BukkitAPI.getPunishmentManager().getBlacklist(player.getUniqueId())));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (event.getReason().equalsIgnoreCase("disconnect.spam")) event.setCancelled(true);
        if (event.getReason().contains("Flying is not enabled")) event.setCancelled(true);
    }
}
