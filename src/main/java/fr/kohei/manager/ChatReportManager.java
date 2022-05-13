package fr.kohei.manager;

import fr.kohei.BukkitAPI;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class ChatReportManager {

    private final HashMap<UUID, Boolean> cooldown;

    public ChatReportManager() {
        this.cooldown = new HashMap<>();
    }

    public void setCooldown(UUID uuid) {
        cooldown.put(uuid, true);
        Bukkit.getScheduler().runTaskLaterAsynchronously(BukkitAPI.getPlugin(), () -> {
            cooldown.put(uuid, false);
        }, 120 * 20);
    }

}
