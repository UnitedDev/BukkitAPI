package fr.uniteduhc.punishment.menu;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.manager.commands.PlayerCommands;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.ConfirmationMenu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public class PunishmentMenu extends PaginatedMenu {
    private final ProfileData target;
    private final Punishments.PunishCategory category;
    private final Menu oldMenu;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "SS " + target.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Punishments value : Punishments.values()) {
            if(value.getCategory().equals(category)) {
                buttons.put(buttons.size(), new PunishButton(value));
            }
        }

        return buttons;
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @RequiredArgsConstructor
    private class PunishButton extends Button {
        private final Punishments punishments;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(punishments.getMaterial()).setName("&6&l" + punishments.getDisplay()).setLore(
                    "&fCliquez-ici pour sanctionner &c" + target.getDisplayName(),
                    "",
                    "&8❘ &7Catégorie: &c" + getDisplay(punishments.getAbstractPunishment().getPunishmentAdapters().get(0).getType()),
                    "",
                    "&f&l» &eCliquez-ici pour y accéder"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            PunishmentData.PunishmentType type = punishments.getAbstractPunishment().getPunishmentAdapters().get(0).getType();
            if(BukkitAPI.getPunishmentManager().getBan(player.getUniqueId()) != null && type == PunishmentData.PunishmentType.BAN) {
                player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un ban actif."));
                return;
            }

            if(BukkitAPI.getPunishmentManager().getBlacklist(player.getUniqueId()) != null && type == PunishmentData.PunishmentType.BLACKLIST) {
                player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà blacklist."));
                return;
            }

            if(BukkitAPI.getPunishmentManager().getMute(player.getUniqueId()) != null && type == PunishmentData.PunishmentType.MUTE) {
                player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un mute actif."));
                return;
            }

            new ConfirmationMenu(() -> {
                int count = (int) BukkitAPI.getCommonAPI().getPunishments(target.getUniqueId()).stream()
                        .filter(pd -> pd.getReason().equalsIgnoreCase(punishments.getDisplay()))
                        .count();

                List<AbstractPunishment.PunishmentAdapter> punishmentAdapters = punishments.getAbstractPunishment().getPunishmentAdapters();

                if(count + 1 >= punishmentAdapters.size()) {
                    count = punishmentAdapters.size() - 1;
                }

                AbstractPunishment.PunishmentAdapter punishmentAdapter = punishmentAdapters.get(count);

                PunishmentData punishmentData = new PunishmentData(
                        punishmentAdapter.getType(),
                        target.getUniqueId(),
                        player.getUniqueId(),
                        punishments.getDisplay(),
                        punishmentAdapter.getDuration()
                );

                BukkitAPI.getPunishmentManager().attemptPunishment(player, punishmentData);
            }, getButtonItem(player), new PunishmentMenu(target, category, oldMenu)).openMenu(player);
        }
    }

    public static String getDisplay(PunishmentData.PunishmentType cat) {
        return cat.name().substring(0, 1).toUpperCase(Locale.ROOT) + cat.name().substring(1).toLowerCase();
    }
}
