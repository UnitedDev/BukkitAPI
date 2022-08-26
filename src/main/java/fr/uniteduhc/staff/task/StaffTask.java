package fr.uniteduhc.staff.task;

import fr.uniteduhc.BukkitAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

public class StaffTask extends BukkitRunnable {
    public StaffTask(Plugin plugin) {
        this.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        List<Player> staffs = Bukkit.getOnlinePlayers().stream()
                .filter(player -> BukkitAPI.getCommonAPI().getProfile(player.getUniqueId()).isStaff())
                .filter(player -> BukkitAPI.getCommonAPI().getProfile(player.getUniqueId()).isVanish())
                .collect(Collectors.toList());

        Bukkit.getOnlinePlayers().forEach(player -> staffs.forEach(player1 -> {
            if (player.canSee(player1)) player.hidePlayer(player1);
        }));
    }
}
