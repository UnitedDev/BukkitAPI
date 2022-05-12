package fr.kohei.commands;

import fr.kohei.command.Command;
import fr.kohei.command.param.Param;
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

    public static GameMode getGamemode(String str) {
        if(str.equalsIgnoreCase("creative") || str.equalsIgnoreCase("c") || str.equals("1")) {
            return GameMode.CREATIVE;
        } else if(str.equalsIgnoreCase("survival") || str.equalsIgnoreCase("s") || str.equals("0")) {
            return GameMode.SURVIVAL;
        } else if(str.equalsIgnoreCase("spectator") || str.equalsIgnoreCase("sp") || str.equals("3")) {
            return GameMode.SPECTATOR;
        } else if(str.equalsIgnoreCase("adventure") || str.equalsIgnoreCase("a") || str.equals("2")) {
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
