package fr.uniteduhc.punishment.menu;

import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.punishment.Punishments;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PunishmentCategoryMenu extends PaginatedMenu {

    private final ProfileData target;
    private final Menu oldMenu;

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "SS " + target.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 5;

        for (Punishments.PunishCategory value : Punishments.PunishCategory.values()) {
            buttons.put(i++, new CategoryButton(value));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class CategoryButton extends Button {
        private final Punishments.PunishCategory category;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(category.getMaterial()).setName("&6&l" + category.getDisplay()).setLore(
                    "&fPermet d'accéder à tous les sanctions de",
                    "&fla catégorie &c" + category.getDisplay(),
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PunishmentMenu(target, category, new PunishmentCategoryMenu(target, oldMenu)).openMenu(player);
        }
    }
}
