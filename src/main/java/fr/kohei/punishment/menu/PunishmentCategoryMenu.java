package fr.kohei.punishment.menu;

import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.punishment.Punishments;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PunishmentCategoryMenu extends PaginatedMenu {

    private final OfflinePlayer target;
    private final Menu oldMenu;

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "SS " + target.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Punishments.PunishCategory value : Punishments.PunishCategory.values()) {
            buttons.put(buttons.size(), new CategoryButton(value));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class CategoryButton extends Button {
        private final Punishments.PunishCategory category;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.BOOK).setName("&c" + category.getDisplay()).setLore(
                    "&fPermet d'accéder à tous les sanctions de",
                    "&fla catégorie &c" + category.getDisplay(),
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PunishmentMenu(target, category, new PunishmentCategoryMenu(target, oldMenu)).openMenu(player);
        }
    }
}
