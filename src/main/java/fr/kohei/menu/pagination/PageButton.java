package fr.kohei.menu.pagination;

import fr.kohei.BukkitAPI;
import fr.kohei.menu.Button;
import fr.kohei.utils.Heads;
import fr.kohei.utils.InventoryUpdate;
import fr.kohei.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class PageButton extends Button {
    private final int mod;
    private final PaginatedMenu menu;

    public PageButton(final int mod, final PaginatedMenu menu) {
        this.mod = mod;
        this.menu = menu;
    }

    @Override
    public ItemStack getButtonItem(final Player player) {
        ItemBuilder item = (this.mod > 0) ? new ItemBuilder(Heads.NEXT_PAGE.toItemStack()) : new ItemBuilder(Heads.PREVIOUS_PAGE.toItemStack());
        if (this.hasNext(player)) {
            item.setName(ChatColor.translateAlternateColorCodes('&', (this.mod > 0) ? "&8» &7Page suivante" : "&8« &7Page précédente"));
        } else {
            item = new ItemBuilder(Material.AIR);
        }
        return item.toItemStack();
    }

    @Override
    public void clicked(final Player player, final int i, final ClickType clickType, final int hb) {
        if (this.hasNext(player)) {
            this.menu.modPage(player, this.mod);
            Button.playNeutral(player);
            InventoryUpdate.updateInventory(BukkitAPI.getPlugin(), player, menu.getTitle(player));
        }
    }

    private boolean hasNext(final Player player) {
        final int pg = this.menu.getPage() + this.mod;
        return pg > 0 && this.menu.getPages(player) >= pg;
    }
}
