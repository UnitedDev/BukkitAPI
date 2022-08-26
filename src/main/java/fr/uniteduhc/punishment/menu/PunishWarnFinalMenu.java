package fr.uniteduhc.punishment.menu;

import fr.uniteduhc.common.cache.data.Warn;
import fr.uniteduhc.manager.WarnManager;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.ConfirmationMenu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class PunishWarnFinalMenu extends PaginatedMenu {
    private final Player target;
    private final WarnManager.WarnsCategory warn;
    private final Menu oldMenu;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Warn " + target.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        Arrays.stream(WarnManager.Warns.values()).filter(warns -> warns.getCategory() == warn).forEach(warns -> {
            buttons.put(buttons.size(), new WarnButton(warns));
        });

        return buttons;
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @RequiredArgsConstructor
    private class WarnButton extends Button {
        private final WarnManager.Warns warns;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.BOOK).setName("&6&l" + warns.getName()).setLore(
                    "&fPermet de sanctionner le joueur avec un warn",
                    "&fde catégorie " + warns.getCategory().getName(),
                    "",
                    "&f&l» &eCliquez pour warn ce joueur"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ConfirmationMenu(() -> {
                player.closeInventory();
                WarnManager.warn(player, target, new Warn(
                        UUID.randomUUID(),
                        target.getUniqueId(),
                        warns.getName(),
                        player.getUniqueId(),
                        new Date()
                ));
            }, getButtonItem(player), new PunishWarnFinalMenu(target, warn, oldMenu)).openMenu(player);
        }
    }
}
