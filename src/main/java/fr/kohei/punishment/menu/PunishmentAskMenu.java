package fr.kohei.punishment.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.PunishmentData;
import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PunishmentAskMenu extends GlassMenu {
    private final PunishmentData data;

    private boolean preuve = false;

    @Override
    public int getGlassColor() {
        return 14;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new SanctionDisplayButton());
        buttons.put(14, new PreuveButton());
        buttons.put(22, new ConfirmationButton());

        return buttons;
    }

    @Override
    public String getTitle(Player paramPlayer) {
        return "Demande de sanction";
    }

    private class SanctionDisplayButton extends Button {
        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.BOOK).setName("&c" + data.getReason()).toItemStack();
        }
    }

    private class PreuveButton extends Button {
        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.INK_SACK).setDurability(preuve ? 10 : 1).setName(
                    "&7Preuve: " + (preuve ? "&aOui" : "&cNon")
            ).setLore(
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            preuve = !preuve;
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    private class ConfirmationButton extends Button {
        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.SLIME_BALL).setName("&a&lConfirmer").setLore(
                    "",
                    "&f&l» &cCliquez-ici pour confirmer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            BukkitAPI.getPunishmentManager().ask(player, data, preuve);
            player.closeInventory();
        }
    }
}
