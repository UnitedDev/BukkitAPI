package fr.kohei.punishment.menu;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.PunishmentData;
import fr.kohei.menu.Button;
import fr.kohei.menu.GlassMenu;
import fr.kohei.menu.Menu;
import fr.kohei.menu.buttons.ConversationButton;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.ItemBuilder;
import fr.kohei.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class EditPunishmentMenu extends GlassMenu {
    private final PunishmentData punishment;
    private final OfflinePlayer target;
    private final Menu oldMenu;

    private long newTime = punishment.getDuration();

    @Override
    public int getGlassColor() {
        return 5;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new RemoveTimeButton(TimeUtil.getDuration("1d"), 1));
        buttons.put(11, new RemoveTimeButton(TimeUtil.getDuration("1h"), 14));
        buttons.put(12, new RemoveTimeButton(TimeUtil.getDuration("30m"), 11));
        buttons.put(13, new PermButton());
        buttons.put(14, new AddTimeButton(TimeUtil.getDuration("30m"), 11));
        buttons.put(15, new AddTimeButton(TimeUtil.getDuration("1h"), 10));
        buttons.put(16, new AddTimeButton(TimeUtil.getDuration("1d"), 2));

        buttons.put(22, new ConversationButton<>(
                new ItemBuilder(Material.SLIME_BALL).setName("&a&lConfirmer").toItemStack(),
                player, ChatUtil.prefix("&aMerci de rentrer la raison de cette modification"),
                (name, pair) -> {
                    punishment.getEdits().add(new PunishmentData.PunishmentEdit(punishment.getDuration(), newTime, pair.getRight(), player.getUniqueId()));
                    BukkitAPI.getCommonAPI().updatePunishment(punishment);
                    new HistoryMenu(target, null);
                }
        ));

        return buttons;
    }

    @Override
    public String getTitle(Player paramPlayer) {
        return "Modifier la sanction";
    }

    private class PermButton extends Button {
        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&cPermanent").setLore(
                    "&fPermet de rendre le bannissement",
                    "&fpermanent.",
                    "",
                    "&8❘ &7Durée: &c" + TimeUtil.getReallyNiceTime2(newTime),
                    "",
                    "&f&l» &cCliquez-ici pour changer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            newTime = -1L;
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @RequiredArgsConstructor
    private class RemoveTimeButton extends Button {
        private final long duration;
        private final int data;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.BANNER).setDurability(data).setName("&c-" + TimeUtil.getReallyNiceTime2(duration)).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            if (newTime - duration <= 1000) newTime = 1000;
            else newTime -= duration;
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }

    @RequiredArgsConstructor
    private class AddTimeButton extends Button {
        private final long duration;
        private final int data;

        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.BANNER).setDurability(data).setName("&a+" + TimeUtil.getReallyNiceTime2(duration)).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            newTime += duration;
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
