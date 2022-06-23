package fr.kohei.punishment.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.menu.Button;
import fr.kohei.menu.Menu;
import fr.kohei.menu.pagination.ConfirmationMenu;
import fr.kohei.manager.report.Report;
import fr.kohei.messaging.packet.MessagePacket;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.C;

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
                BukkitAPI.getChatReportManager().setCooldown(player.getUniqueId());
                fr.kohei.common.cache.data.Report report = new fr.kohei.common.cache.data.Report(
                        new Date(),
                        UUID.randomUUID(),
                        target.getUniqueId(),
                        player.getUniqueId(),
                        " §8▪ §cReport: §f" + value.getDisplay(),
                        false
                );
                BukkitAPI.getCommonAPI().addReport(report);
                player.sendMessage(ChatUtil.prefix("&fVous avez report le joueur &c" + target.getName() + " &fpour &c" + value.getDisplay()));

                String server = BukkitAPI.getCommonAPI().getServerCache().getPlayers().get(target.getUniqueId());
                TextComponent teleport = new TextComponent(ChatUtil.translate("&a&l[TELEPORTATION AU SERVEUR]"));
                teleport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + server));
                BukkitAPI.getCommonAPI().getMessaging().sendPacket(new MessagePacket(
                        Arrays.asList(
                                new TextComponent(ChatUtil.prefix("&c" + target.getName() + " &fvient de recevoir un report pour &c" + value.getDisplay()
                                        + " &fpar &c" + player.getName())),
                                teleport
                        ),
                        30
                ));
                player.closeInventory();
            }, getButtonItem(player), new ReportMenu(target)).openMenu(player);
        }
    }
}
