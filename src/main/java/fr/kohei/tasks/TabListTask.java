package fr.kohei.tasks;

import fr.kohei.BukkitAPI;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.TabList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
@RequiredArgsConstructor
public class TabListTask extends BukkitRunnable {

    private final JavaPlugin plugin;

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(TabListTask::sendTabList);
    }

    public static void sendTabList(Player player) {
        String header =
                "&6" + "\n" +
                        "&e  &6&l■ &8┃ &6&lKohei &8● &6Network UHC &8&l» &7&o%online_players%&8/&7&o1000 &8┃ &6&l■" + "\n" +
                        "&8» &7Serveur &8● &6%server% &8«" + "\n" +
                        "&6";
        String footer =
                "&6" + "\n" +
                        "&7Discord &8&l» &9discord.gg/kohei" + "\n" +
                        "&7Twitter &8&l» &b@KoheiNetwork" + "\n" +
                        "&6";

        TabList.send(player, replace(header), replace(footer));
    }

    public static String replace(String str) {
        return ChatUtil.translate(str.replace(
                "%online_players%", "" + BukkitAPI.getTotalPlayers()
        ).replace(
                "%server%",
                BukkitAPI.getFactory(Bukkit.getPort()).getName()
        ));
    }

}
