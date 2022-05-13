package fr.kohei.command.impl;

import fr.kohei.BukkitAPI;
import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
import fr.kohei.common.RedisProvider;
import fr.kohei.common.cache.PunishmentData;
import fr.kohei.messaging.list.packet.PunishmentAskPacket;
import fr.kohei.utils.ChatUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

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
        PunishmentData punishmentData = RedisProvider.redisProvider.GSON.fromJson(data, PunishmentData.class);

        BukkitAPI.getPunishmentManager().punish(punishmentData, null);
    }

    @Command(names = "acceptban", power = 40)
    public static void acceptBan(Player player, @Param(name = "data", wildcard = true) String data) {
        PunishmentAskPacket packet = RedisProvider.redisProvider.GSON.fromJson(data, PunishmentAskPacket.class);

        PunishmentData.PunishmentType type = packet.getPunishmentData().getPunishmentType();
        if (BukkitAPI.getPunishmentManager().getBan(player.getUniqueId()) != null && type == PunishmentData.PunishmentType.BAN) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un ban actif."));
            return;
        }

        if (BukkitAPI.getPunishmentManager().getBlacklist(player.getUniqueId()) != null && type == PunishmentData.PunishmentType.BLACKLIST) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà blacklist."));
            return;
        }

        if (BukkitAPI.getPunishmentManager().getMute(player.getUniqueId()) != null && type == PunishmentData.PunishmentType.MUTE) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un mute actif."));
            return;
        }

        BukkitAPI.getPunishmentManager().punish(packet.getPunishmentData(), player.getName());
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
