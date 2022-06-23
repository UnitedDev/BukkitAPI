package fr.kohei.staff.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FreezeMenu extends Menu {
    @Override
    public String getTitle(Player paramPlayer) {
        return "Freeze";
    }

    @Override
    public Map<Integer, Button> getButtons(Player paramPlayer) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player paramPlayer) {
                return new ItemBuilder(Material.PAPER).setName("&cVous avez été &lFreeze").setLore(
                        "&fVous avez été freeze par un modérateur.",
                        "&frendez-vous sur notre &9Discord &f(&c.gg/kohei&f).",
                        " ",
                        "&c⚠ Ne vous déconnectez pas du serveur"
                ).toItemStack();
            }
        });

        return buttons;
    }

    @Override
    public void onClose(Player player) {
        if(!BukkitAPI.getStaffManager().getFreezePlayers().contains(player.getUniqueId())) return;
        Bukkit.getScheduler().runTaskLater(BukkitAPI.getPlugin(), () -> new FreezeMenu().openMenu(player), 1L);
    }
}
