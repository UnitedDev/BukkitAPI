package fr.uniteduhc.punishment.menu;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.manager.commands.PlayerCommands;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.Heads;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class HistoryMenu extends PaginatedMenu {

    private final ProfileData target;
    private final Menu oldMenu;

    private PunishmentData.PunishmentType type = PunishmentData.PunishmentType.BAN;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Historique " + target.getDisplayName();
    }

    @Override
    public Menu backButton() {
        return oldMenu;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new Button() {
            @Override
            public ItemStack getButtonItem(Player p0) {
                List<String> lore = new ArrayList<>();
                lore.add("&7&m»&7 Blacklists");
                lore.add("&7&m»&7 Bannissements");
                lore.add("&7&m»&7 Mutes");

                if (type == PunishmentData.PunishmentType.BLACKLIST) lore.set(0, "&a&l» &aBlacklists");
                if (type == PunishmentData.PunishmentType.BAN) lore.set(1, "&a&l» &aBannissements");
                if (type == PunishmentData.PunishmentType.MUTE) lore.set(2, "&a&l» &aMutes");

                return new ItemBuilder(Heads.SETTINGS.toItemStack()).setName("&6&lTriage").setLore(lore).toItemStack();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                if (type == PunishmentData.PunishmentType.BLACKLIST) type = PunishmentData.PunishmentType.BAN;
                else if (type == PunishmentData.PunishmentType.BAN) type = PunishmentData.PunishmentType.MUTE;
                else type = PunishmentData.PunishmentType.BLACKLIST;
            }

            @Override
            public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                return true;
            }
        });

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (PunishmentData punishment : BukkitAPI.getCommonAPI().getPunishments(target.getUniqueId()))
            if (type == punishment.getPunishmentType())
                buttons.put(buttons.size(), new PunishmentButton(punishment));


        return buttons;
    }

    @RequiredArgsConstructor
    private class PunishmentButton extends Button {
        private final PunishmentData punishment;

        @Override
        public ItemStack getButtonItem(Player p0) {
            List<String> lore = new ArrayList<>();

            lore.add("");
            lore.add("&8❘ &7Auteur: &6" + BukkitAPI.getCommonAPI().getProfile(punishment.getExecutor()).getDisplayName());
            lore.add("&8❘ &7Date: &6" + TimeUtil.formatDate(punishment.getDate().getTime()));
            lore.add("&8❘ &7Expiration: &6" + TimeUtil.formatDate(punishment.getDate().getTime() + punishment.getDuration()));
            lore.add("&8❘ &7Type: &6" + PunishmentMenu.getDisplay(punishment.getPunishmentType()));
            lore.add("&8❘ &7Raison: &6" + punishment.getReason());
            if(punishment.getEdits().size() != 0) lore.add("&8❘ &7Modifications:");
            for (PunishmentData.PunishmentEdit edit : punishment.getEdits()) {
                lore.add(" &6" + BukkitAPI.getCommonAPI().getProfile(edit.getExecutor()).getDisplayName() + ":");
                lore.add(" &8- &7Raison: &6" + edit.getReason());
                lore.add(" &8- &6" + TimeUtil.getReallyNiceTime2(edit.getOldDuration()) + " &8» &6" + TimeUtil.getReallyNiceTime2(edit.getNewDuration()));
            }
            lore.add(" ");
            lore.add("&f&l» &eCliquez-ici pour modifier");

            ItemBuilder builder = new ItemBuilder(Material.PAPER).setName("&6&l" + punishment.getPunishmentId().toString().substring(0, 8))
                    .setLore(lore);

            if (BukkitAPI.getPunishmentManager().isValid(punishment)) {
                builder.addEnchant(Enchantment.DAMAGE_ALL, 1);
            } else {
                builder.setAmount(0);
            }
            builder.hideItemFlags();

            return builder.toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new EditPunishmentMenu(punishment, target, oldMenu).openMenu(player);
        }
    }
}
