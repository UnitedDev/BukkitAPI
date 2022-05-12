package fr.kohei.menu;

import org.bukkit.plugin.java.JavaPlugin;

public final class MenuAPI {
    public MenuAPI(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ButtonListener(plugin), plugin);
        new MenuUpdateTask(plugin);
    }
}
