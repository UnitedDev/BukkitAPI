package fr.uniteduhc.punishment.menu;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.menu.Button;
import fr.uniteduhc.menu.GlassMenu;
import fr.uniteduhc.menu.Menu;
import fr.uniteduhc.menu.buttons.ConversationButton;
import fr.uniteduhc.messaging.packet.PunishmentPacket;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.ItemBuilder;
import fr.uniteduhc.utils.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class EditPunishmentMenu extends GlassMenu {
    private final PunishmentData punishment;
    private final ProfileData target;
    private final Menu oldMenu;

    private long newTime;

    public EditPunishmentMenu(PunishmentData punishment, ProfileData target, Menu oldMenu) {
        this.punishment = punishment;
        this.target = target;
        this.oldMenu = oldMenu;
        newTime = punishment.getDuration();
    }

    @Override
    public int getGlassColor() {
        return 5;
    }

    @Override
    public Map<Integer, Button> getAllButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(10, new RemoveTimeButton(TimeUtil.getDuration("7d"), 1));
        buttons.put(11, new RemoveTimeButton(TimeUtil.getDuration("1d"), 14));
        buttons.put(12, new RemoveTimeButton(TimeUtil.getDuration("1h"), 11));
        buttons.put(13, new PermButton());
        buttons.put(4, new UnbanButton());
        buttons.put(14, new AddTimeButton(TimeUtil.getDuration("1h"), 11));
        buttons.put(15, new AddTimeButton(TimeUtil.getDuration("1d"), 10));
        buttons.put(16, new AddTimeButton(TimeUtil.getDuration("7d"), 2));

        buttons.put(22, new ConversationButton<>(
                new ItemBuilder(Material.SLIME_BALL).setName("&a&lConfirmer").toItemStack(),
                player, ChatUtil.prefix("&aMerci de rentrer la raison de cette modification"),
                (name, pair) -> {
                    punishment.getEdits().add(new PunishmentData.PunishmentEdit(punishment.getDuration(), newTime, pair.getRight(), player.getUniqueId()));
                    punishment.setDuration(newTime);
                    BukkitAPI.getCommonAPI().updatePunishment(punishment);
                    BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentPacket(punishment, null, false));
                    new HistoryMenu(target, null).openMenu(player);
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
            return new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&6&lPermanent").setLore(
                    "&fPermet de rendre le bannissement",
                    "&fpermanent.",
                    "",
                    "&8❘ &7Durée: &c" + TimeUtil.getReallyNiceTime2(newTime),
                    "",
                    "&f&l» &eCliquez-ici pour changer"
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
            return new ItemBuilder(Material.BANNER).setDurability(data).setName("&6&l-" + TimeUtil.getReallyNiceTime2(duration)).toItemStack();
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

    private class UnbanButton extends Button {
        @Override
        public ItemStack getButtonItem(Player p0) {
            return new ItemBuilder(Material.COMPASS).setName("&6&lUnban").setLore(
                    "&fPermet de révoquer le banissement du",
                    "&fjoueur",
                    "",
                    "&f&l» &eCliquez-ici pour confirmer"
            ).toItemStack();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            newTime = 1000;
        }

        @Override
        public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
            return true;
        }
    }
}
