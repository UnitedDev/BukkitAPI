package fr.kohei.utils;

import net.md_5.bungee.api.ChatColor;

public class ChatUtil {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String prefix(String message) {
        return translate("&6Kohei &8» &f" + message);
    }

}
