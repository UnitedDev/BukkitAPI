package fr.uniteduhc.punishment.menu;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.manager.commands.PlayerCommands;
import fr.uniteduhc.common.CommonProvider;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.data.Report;
import fr.uniteduhc.common.cache.data.Warn;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.pagination.ConfirmationMenu;
import fr.uniteduhc.menu.pagination.PaginatedMenu;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class WarnsMenu extends PaginatedMenu {
    private final ProfileData profile;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Warns " + profile.getDisplayName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        final UUID uuid = profile.getUniqueId();
        for (Warn warn : BukkitAPI.getCommonAPI().getWarns(uuid)) {
            buttons.put(buttons.size(), new WarnButton(warn));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private static class WarnButton extends Button {
        private final Warn warn;

        @Override
        public ItemStack getButtonItem(Player p0) {
            ItemBuilder builder = new ItemBuilder(Material.PAPER);
            builder.addEnchant(Enchantment.DAMAGE_ALL, 1);
            builder.hideItemFlags();

            builder.setName("§c" + warn.getWarnId().toString().substring(0, 6));
            builder.addLoreLine(" ");
            builder.addLoreLine("§8❘ §7Modérateur: §f" + CommonProvider.getInstance().getProfile(warn.getIssuerId()).getDisplayName());
            builder.addLoreLine("§8❘ §7Date: §6" + TimeUtil.formatDate(warn.getDate().getTime()));

            builder.addLoreLine("§8❘ §7Raison: §c" + warn.getReason());

            builder.addLoreLine("§f§l» §cClic drop pour supprimer");

            return builder.toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            ProfileData profile = CommonProvider.getInstance().getProfile(player.getUniqueId());
            // if the profile rank power is less then 41, the player can't delete warns
            if (profile.getRank().getPermissionPower() < 41) {
                player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission de supprimer des avertissements."));
                return;
            }

            if (clickType == ClickType.DROP) {
                new ConfirmationMenu(() -> {
                    BukkitAPI.getCommonAPI().removeWarn(warn);
                    player.sendMessage(ChatUtil.prefix("&aVous avez supprimé l'avertissement §c" + warn.getWarnId().toString().substring(0, 6)));
                }, getButtonItem(player), new WarnsMenu(profile)).openMenu(player);
            }
        }
    }

    @RequiredArgsConstructor
    private class PlayerDisplayButton extends Button {
        private final ProfileData data;
        private final UUID uuid;

        @Override
        public ItemStack getButtonItem(Player p0) {
            List<Report> reports = BukkitAPI.getCommonAPI().getReports().stream()
                    .filter(report -> report.getUuid().equals(uuid))
                    .filter(report -> !report.isResolved()).collect(Collectors.toList());
            int allReports = (int) BukkitAPI.getCommonAPI().getReports().stream()
                    .filter(report -> report.getUuid().equals(uuid))
                    .count();

            return new ItemBuilder(Material.SKULL_ITEM).setDurability(SkullType.PLAYER.ordinal())
                    .setSkullOwner(data.getDisplayName())
                    .setName("§c" + data.getDisplayName())
                    .setLore(
                            "&fCe joueur a un total de &c" + reports.size() + " &freports",
                            "&fnon résolus (&c" + allReports + " &fen tout)",
                            "",
                            "&f&l» &eCliquez-ici pour y accéder").toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new WarnsMenu(data).openMenu(player);
        }
    }
}
