package fr.kohei.punishment.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.pagination.ConfirmationMenu;
import fr.kohei.report.Report;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@RequiredArgsConstructor
public class ReportMenu extends Menu {
    private final Player target;

    @Override
    public String getTitle(Player paramPlayer) {
        return "Report " + target.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player paramPlayer) {
        Map<Integer, Button> buttons = new HashMap<>();

        int i = 1;
        for (Report value : Report.values()) {
            buttons.put(i++, new ReportButton(value));
        }

        return buttons;
    }

    @RequiredArgsConstructor
    private class ReportButton extends Button {
        private final Report value;

        @Override
        public ItemStack getButtonItem(Player p0) {
            List<String> lore = new ArrayList<>();
            lore.add("&7Si un joueur réalise une infraction au");
            lore.add("&7règlement, report le joueur.");
            lore.add(" ");
            for (String s : value.getLore()) {
                lore.add("&8▪ &a" + s);
            }
            lore.add(" ");
            lore.add("&c⚠ Tout abus sera sanctionné");
            lore.add(" ");
            lore.add("&f&l» &cCliquez-ici pour report");

            return new ItemBuilder(value.getMaterial()).setName("&c" + value.getDisplay()).setLore(lore).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            new ConfirmationMenu(() -> {
                fr.kohei.common.cache.Report report = new fr.kohei.common.cache.Report(
                        new Date(),
                        UUID.randomUUID(),
                        target.getUniqueId(),
                        player.getUniqueId(),
                        " §8▪ §cReport: §f" + value.getDisplay(),
                        false
                );
                BukkitAPI.getCommonAPI().addReport(report);
                player.sendMessage(ChatUtil.prefix("&fVous avez report le joueur &c" + target.getName() + " &fpour &c" + value.getDisplay()));
                player.closeInventory();
            }, getButtonItem(player), new ReportMenu(target)).openMenu(player);
        }
    }
}
