package fr.uniteduhc.manager.grant;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.rank.Grant;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.pagination.ConfirmationMenu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
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
public class GrantsMenu extends PaginatedMenu {
    private final ProfileData targetProfile;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Grants " + targetProfile.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Grant grant : BukkitAPI.getCommonAPI().getGrants(targetProfile.getUniqueId())) {
            buttons.put(buttons.size(), new GrantButton(grant));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class GrantButton extends Button {
        private final Grant grant;

        @Override
        public ItemStack getButtonItem(Player p0) {
            ProfileData data = null;
            if (!grant.getIssuer().toString().contains("00000")) {
                data = BukkitAPI.getCommonAPI().getProfile(grant.getIssuer());
            }
            return new ItemBuilder(Material.WOOL).setDurability(grant.isActive() ? 5 : 14)
                    .setName("&c" + grant.getGrantId().toString().substring(0, 6))
                    .setLore(
                            "&8❘ &7Exécuteur: &c" + (data == null ? "&cConsole" : data.getDisplayName()),
                            "&8❘ &7Date: &c" + TimeUtil.formatDate(grant.getExecutedAt()),
                            "&8❘ &7Actif: " + (grant.isActive() ? "&aOui" : "&cNon"),
                            "&8❘ &7Grade: &c" + grant.getRank().getToken(),
                            "&8❘ &7Durée: &c" + TimeUtil.getReallyNiceTime2(grant.getDuration()),
                            "",
                            "&f&l» &cTouche drop pour supprimer"
                    ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (clickType != ClickType.DROP) return;

            new ConfirmationMenu(() ->
                    BukkitAPI.getCommonAPI().removeGrant(grant), getButtonItem(player), new GrantsMenu(targetProfile)
            ).openMenu(player);
        }
    }
}
