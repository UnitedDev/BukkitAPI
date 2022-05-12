package fr.kohei.menu.pagination;

import fr.kohei.utils.ItemBuilder;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.BackButton;
import fr.kohei.menu.buttons.DisplayButton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ConfirmationMenu extends Menu {

    private final Runnable runnable;
    private final ItemStack is;
    private final Menu oldMenu;

    @Override
    public String getTitle(Player paramPlayer) {
        return "Confirmation";
    }

    @Override
    public Map<Integer, Button> getButtons(Player paramPlayer) {
        HashMap<Integer, Button> buttons = new HashMap<>();

        for (int i : new int[]{1, 2, 3, 10, 11, 12, 19, 20, 21}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(13).setName(" ").toItemStack()));
        }

        buttons.put(13, new DisplayButton(is));

        for (int i : new int[]{5, 6, 7, 14, 15, 16, 23, 24, 25}) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability(14).setName(" ").toItemStack()));
        }

        buttons.put(11, new ConfirmationButton());
        buttons.put(15, new CancelButton());

        if(oldMenu != null) {
            buttons.put(22, new BackButton(oldMenu));
        }

        return buttons;
    }

    public class ConfirmationButton extends Button {

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.SLIME_BALL).setName("&a&lConfirmer").toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
            runnable.run();
        }
    }

    public class CancelButton extends Button {

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&c&lAnnuler").toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            oldMenu.openMenu(player);
        }
    }

}
