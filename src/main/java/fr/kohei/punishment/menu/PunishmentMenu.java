package fr.kohei.punishment.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.PunishmentData;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.pagination.ConfirmationMenu;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
public class PunishmentMenu extends PaginatedMenu {
    private final OfflinePlayer target;
    private final Punishments.PunishCategory category;
    private final Menu oldMenu;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "SS " + target.getName();
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
            return new ItemBuilder(punishments.getMaterial()).setName("&c" + punishments.getDisplay()).setLore(
                    "&fCliquez-ici pour sanctionner &c" + target.getName(),
                    "",
                    "&8❘ &7Catégorie: &c" + getDisplay(punishments.getAbstractPunishment().getPunishmentAdapters().get(0).getType()),
                    "",
                    "&f&l» &cCliquez-ici pour y accéder"
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
