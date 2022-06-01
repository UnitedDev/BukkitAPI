package fr.kohei.command.impl;

import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.common.RedisProvider;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.common.cache.PunishmentData;
import fr.kohei.messaging.packet.PunishmentAskPacket;
import fr.kohei.messaging.packet.PunishmentPacket;
import fr.kohei.punishment.menu.HistoryMenu;
import fr.kohei.punishment.menu.PunishmentCategoryMenu;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ModCommands {

    @Command(names = {"gamemode"}, power = 40)
    public static void changeGamemode(Player player, @Param(name = "player") String gamemode) {

        GameMode newGamemode = getGamemode(gamemode);
        if (newGamemode == null) {
            player.sendMessage(ChatUtil.prefix("&cVous devez rentrer un gamemode valide."));
            return;
        }

        player.setGameMode(newGamemode);
        player.sendMessage(ChatUtil.prefix("&fVous avez changé votre gamemode en &a" + niceFormat(newGamemode)));
    }

    @Command(names = {"tp"}, power = 39)
    public static void teleport(Player player, @Param(name = "player") Player target) {
        player.teleport(target.getLocation());
        player.sendMessage(ChatUtil.prefix("&fVous vous êtes téléporté à &a" + target.getName()));
    }

    @Command(names = {"tphere"}, power = 39)
    public static void teleportHere(Player player, @Param(name = "player") Player target) {

        target.teleport(player.getLocation());
        player.sendMessage(ChatUtil.prefix("&fVous avez téléporté &a" + target.getName() + " &fà vous"));
    }

    @Command(names = "forceban", power = 39)
    public static void forceBan(Player player, @Param(name = "data", wildcard = true) String data) {
        UUID uuid = UUID.fromString(data);
        String str = BukkitAPI.getPunishmentManager().getGsonAskPunishment().get(uuid);
        PunishmentData punishmentData = RedisProvider.redisProvider.GSON.fromJson(str, PunishmentData.class);

        BukkitAPI.getPunishmentManager().punish(punishmentData, null);
    }

    @Command(names = "ss", power = 39)
    public static void ss(Player player, @Param(name = "player") OfflinePlayer offlinePlayer) {
        new PunishmentCategoryMenu(offlinePlayer, null).openMenu(player);
    }

    @Command(names = {"c", "casier"}, power = 39)
    public static void chech(Player player, @Param(name = "player") OfflinePlayer offlinePlayer) {
        new HistoryMenu(offlinePlayer, null).openMenu(player);
    }

    @Command(names = "declineban", power = 40)
    public static void decline(Player player, @Param(name = "data") String data) {
        UUID uuid = UUID.fromString(data);
        PunishmentAskPacket packet = RedisProvider.redisProvider.GSON.fromJson(
                BukkitAPI.getPunishmentManager().getGsonAskPunishment().get(uuid), PunishmentAskPacket.class
        );

        BukkitAPI.getPunishmentManager().getGsonAskPunishment().remove(uuid);
        player.sendMessage(ChatUtil.prefix("&fVous avez &crefusé &fle bannissement de &7" +
                BukkitAPI.getCommonAPI().getProfile(packet.getExecutor()).getDisplayName()));
    }

    @Command(names = "acceptban", power = 40)
    public static void acceptBan(Player player, @Param(name = "data") String data) {
        UUID uuid = UUID.fromString(data);
        PunishmentAskPacket packet = RedisProvider.redisProvider.GSON.fromJson(
                BukkitAPI.getPunishmentManager().getGsonAskPunishment().get(uuid), PunishmentAskPacket.class
        );

        PunishmentData.PunishmentType type = packet.getPunishmentData().getPunishmentType();
        if (BukkitAPI.getPunishmentManager().getBan(packet.getPunishmentData().getPunished()) != null && type == PunishmentData.PunishmentType.BAN) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un ban actif."));
            return;
        }

        if (BukkitAPI.getPunishmentManager().getBlacklist(packet.getPunishmentData().getPunished()) != null && type == PunishmentData.PunishmentType.BLACKLIST) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà blacklist."));
            return;
        }

        if (BukkitAPI.getPunishmentManager().getMute(packet.getPunishmentData().getPunished()) != null && type == PunishmentData.PunishmentType.MUTE) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un mute actif."));
            return;
        }

        if(BukkitAPI.getCommonAPI().getProfile(player.getUniqueId()).getRank().permissionPower() <= 40 &&
                packet.getPunishmentData().getPunishmentType() == PunishmentData.PunishmentType.BLACKLIST) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission de blacklist un joueur."));
            return;
        }

        BukkitAPI.getPunishmentManager().getGsonAskPunishment().remove(uuid);
        BukkitAPI.getPunishmentManager().punish(packet.getPunishmentData(), player.getName());
    }

    @Command(names = "ban", power = 40)
    public static void ban(Player sender, @Param(name = "player") OfflinePlayer target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getBan(target.getUniqueId()) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà banni."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.BAN,
                target.getUniqueId(),
                sender.getUniqueId(),
                reason,
                -1L
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "tempban", power = 40)
    public static void ban(Player sender, @Param(name = "player") OfflinePlayer target, @Param(name = "durée") String duration, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getBan(target.getUniqueId()) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà banni."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.BAN,
                target.getUniqueId(),
                sender.getUniqueId(),
                reason,
                TimeUtil.getDuration(duration)
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "unban", power = 40)
    public static void unban(Player sender, @Param(name = "player") OfflinePlayer target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getBan(target.getUniqueId()) == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas banni."));
            return;
        }

        PunishmentData punishment = BukkitAPI.getPunishmentManager().getBan(target.getUniqueId());

        punishment.getEdits().add(new PunishmentData.PunishmentEdit(punishment.getDuration(), 1000, reason, sender.getUniqueId()));
        punishment.setDuration(1000);

        BukkitAPI.getCommonAPI().updatePunishment(punishment);
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentPacket(punishment, null, false));

        String executorDisplay = BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getRank().getTabPrefix().substring(0, 2) +
                BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getDisplayName();

        sender.sendMessage(ChatUtil.prefix("&fVous avez débanni " + executorDisplay));
    }

    @Command(names = "stop", power = 50)
    public static void stop(Player player) {
        Bukkit.getServer().shutdown();
    }

    @Command(names = "mute", power = 39)
    public static void mute(Player sender, @Param(name = "player") OfflinePlayer target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getMute(target.getUniqueId()) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà mute."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.MUTE,
                target.getUniqueId(),
                sender.getUniqueId(),
                reason,
                -1L
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "tempmute", power = 39)
    public static void mute(Player sender, @Param(name = "player") OfflinePlayer target, @Param(name = "durée") String duration, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getMute(target.getUniqueId()) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà mute."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.MUTE,
                target.getUniqueId(),
                sender.getUniqueId(),
                reason,
                TimeUtil.getDuration(duration)
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "unmute", power = 40)
    public static void unmute(Player sender, @Param(name = "player") OfflinePlayer target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getMute(target.getUniqueId()) == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas mute."));
            return;
        }

        PunishmentData punishment = BukkitAPI.getPunishmentManager().getMute(target.getUniqueId());

        punishment.getEdits().add(new PunishmentData.PunishmentEdit(punishment.getDuration(), 1000, reason, sender.getUniqueId()));
        punishment.setDuration(1000);

        BukkitAPI.getCommonAPI().updatePunishment(punishment);
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentPacket(punishment, null, false));

        String executorDisplay = BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getRank().getTabPrefix().substring(0, 2) +
                BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getDisplayName();

        sender.sendMessage(ChatUtil.prefix("&fVous avez unmute " + executorDisplay));
    }

    @Command(names = {"kick"}, power = 39)
    public static void unmute(Player player, @Param(name = "player") Player target, @Param(name = "raison") String reason) {

        target.kickPlayer(ChatUtil.prefix(
                "&cVous avez été kick" + "\n"
                        + " " + "\n"
                        + "&fVous avez été &6kick &fpar &6" + player.getName() + " &fpour &6" + reason
        ));

        player.sendMessage(ChatUtil.prefix("&fVous avez kick &c" + target.getName() + " &fpour &c " + reason));

    }

    @Command(names = {"stafflist", "sl"})
    public static void staffList(Player player) {
        player.sendMessage(" ");
        player.sendMessage(ChatUtil.translate("&8» &c&lStaff List"));
        BukkitAPI.getCommonAPI().getServerCache().getPlayers().forEach((uuid, s) -> {
            ProfileData data = BukkitAPI.getCommonAPI().getProfile(uuid);
            String executorDisplay = data.getRank().getTabPrefix() + " " + data.getDisplayName();
            player.sendMessage(ChatUtil.translate(" &7■ " + executorDisplay));
        });
        player.sendMessage(" ");
    }

    public static GameMode getGamemode(String str) {
        if (str.equalsIgnoreCase("creative") || str.equalsIgnoreCase("c") || str.equals("1")) {
            return GameMode.CREATIVE;
        } else if (str.equalsIgnoreCase("survival") || str.equalsIgnoreCase("s") || str.equals("0")) {
            return GameMode.SURVIVAL;
        } else if (str.equalsIgnoreCase("spectator") || str.equalsIgnoreCase("sp") || str.equals("3")) {
            return GameMode.SPECTATOR;
        } else if (str.equalsIgnoreCase("adventure") || str.equalsIgnoreCase("a") || str.equals("2")) {
            return GameMode.ADVENTURE;
        } else {
            return null;
        }
    }

    public static String niceFormat(GameMode gamemode) {
        String name = gamemode.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
