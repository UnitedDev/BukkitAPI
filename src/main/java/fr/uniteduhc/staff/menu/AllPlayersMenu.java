package fr.uniteduhc.staff.menu;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.pagination.ConfirmationMenu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllPlayersMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Joueurs";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 0;
        for (UUID s : BukkitAPI.getCommonAPI().getServerCache().getPlayers().keySet()) {
            ProfileData profile = BukkitAPI.getCommonAPI().getProfile(s);
            buttons.put(i++, new PlayersButton(profile.getDisplayName(), s));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class PlayersButton extends Button {
        private final String s;
        private final UUID uuid;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal()).setSkullOwner(s).setName("&6&l" + s).setLore(
                    "&fCe joueur est actuellement connecté sur",
                    "&fle serveur &c" + BukkitAPI.getCommonAPI().getServerCache().getPlayers().get(uuid),
                    " ",
                    "&f&l» &eCliquez-ici pour vous téléporter à lui"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ConfirmationMenu(() ->
                    BukkitAPI.sendToServer(player, BukkitAPI.getCommonAPI().getServerCache().getPlayers().get(uuid))
            , getButtonItem(player), new AllPlayersMenu()).openMenu(player);
        }
    }
}