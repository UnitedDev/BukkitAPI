package fr.kohei.punishment.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.command.impl.PlayerCommands;
import fr.kohei.common.CommonProvider;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.common.cache.data.Report;
import fr.kohei.menu.Button;
import fr.kohei.menu.pagination.ConfirmationMenu;
import fr.kohei.menu.pagination.PaginatedMenu;
import fr.kohei.utils.Heads;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
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
public class ReportsMenu extends PaginatedMenu {
    private final ProfileData profile;
    private boolean all;

    @Override
    public String getPrePaginatedTitle(Player p0) {
        return "Reports " + (profile != null ? profile.getDisplayName() : "");
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (profile != null)
            buttons.put(4, new Button() {
                @Override
                public ItemStack getButtonItem(Player p0) {
                    return new ItemBuilder(Heads.SETTINGS.toItemStack()).setName("&cTout les reports &8(&c" + (all ? "&aActivés" : "&cDésactivés") + "&8)").setLore(
                            "&fPermet d'accéder à tous les reports",
                            "",
                            "&f&l» &cCliquez-ici pour y accéder"
                    ).toItemStack();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                    all = !all;
                }

                @Override
                public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                    return true;
                }
            });

        return buttons;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player p0) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (profile == null) {
            List<UUID> uuids = new ArrayList<>();
            BukkitAPI.getCommonAPI().getReports().stream()
                    .filter(report -> !uuids.contains(report.getUuid()))
                    .filter(report -> !report.isResolved())
                    .forEach(report -> uuids.add(report.getUuid()));

            for (UUID uuid : uuids) {
                ProfileData data = BukkitAPI.getCommonAPI().getProfile(uuid);
                buttons.put(buttons.size(), new PlayerDisplayButton(data, uuid));
            }
        } else {
            final UUID uuid = PlayerCommands.fromString(profile.getDisplayName());
            for (Report report : BukkitAPI.getCommonAPI().getReports().stream().filter(report -> report.getUuid().equals(uuid)).collect(Collectors.toList())) {
                if (report.isResolved() && !all) continue;
                buttons.put(buttons.size(), new ReportButton(report));
            }

        }
        return buttons;
    }

    @RequiredArgsConstructor
    private class ReportButton extends Button {
        private final Report report;

        @Override
        public ItemStack getButtonItem(Player p0) {
            ItemBuilder builder = new ItemBuilder(Material.PAPER);
            if (report.isResolved()) builder.setAmount(0);
            else builder.addEnchant(Enchantment.DAMAGE_ALL, 1);
            builder.hideItemFlags();
            builder.setName("§c" + report.getReportId().toString().substring(0, 6));
            builder.addLoreLine(" ");
            builder.addLoreLine("§8❘ §7Reporteur: §f" + CommonProvider.getInstance().getProfile(report.getReporter()).getDisplayName());
            builder.addLoreLine("§8❘ §7Date: §6" + TimeUtil.formatDate(report.getDate().getTime()));

            builder.addLoreLine("§8❘ §7Message:");
            String message = report.getMessage();
            // create an array for the message so there is max 40 char per line
            String[] lines = message.split("(?<=\\G.{40})");
            for (String line : lines) {
                builder.addLoreLine(" §f" + line);
            }
            builder.addLoreLine("§f§l» §cClic droit pour résoudre");
            builder.addLoreLine("§f§l» §cClic gauche pour sanctionner");

            return builder.toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {

            if (clickType == ClickType.RIGHT) {
                new ConfirmationMenu(() -> {
                    report.setResolved(true);
                    BukkitAPI.getCommonAPI().updateReport(report);
                    new ReportsMenu(profile).openMenu(player);
                }, getButtonItem(player), new ReportsMenu(profile)).openMenu(player);
            } else {
                new PunishmentCategoryMenu(profile, new ReportsMenu(profile)).openMenu(player);
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
                            "&f&l» &cCliquez-ici pour y accéder").toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ReportsMenu(data).openMenu(player);
        }
    }
}
