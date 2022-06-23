package fr.kohei.punishment.menu;

import fr.kohei.common.CommonProvider;
import fr.kohei.common.cache.data.Warn;
import fr.kohei.manager.WarnManager;
import fr.kohei.menu.Button;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PunishWarnMenu extends PaginatedMenu {
    private final Player target;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Warn " + target.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 6;
        for (WarnManager.WarnsCategory warn : WarnManager.WarnsCategory.values()) {
            buttons.put(i++, new WarnButton(warn));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    public class WarnButton extends Button {
        private final WarnManager.WarnsCategory warn;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(warn.getIcon()).setName(warn.getName()).setLore(
                    "&fPermet d'accéder à tous les warns de cette",
                    "&fcatégorie.",
                    "",
                    "&f&l» &cCliquez pour accéder aux warns"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new PunishWarnFinalMenu(target, warn, new PunishWarnMenu(target)).openMenu(player);
        }
    }

}
