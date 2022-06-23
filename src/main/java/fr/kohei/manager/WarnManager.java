package fr.kohei.manager;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.data.Warn;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Heads;
import fr.kohei.utils.Title;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WarnManager {

    public static void warn(Player player, Player target, Warn warn) {
        Title.sendTitle(target, 0, 100, 0,
                "&c⚠ &c&lATTENTION &c⚠",
                "&fVous avez été warn pour la raison &c" + warn.getReason()
        );

        player.sendMessage(ChatUtil.prefix("&fVous avez warn &c" + target.getName() + " &fpour la raison &c" + warn.getReason()));
        BukkitAPI.getCommonAPI().addWarn(warn);

        checkPunishment(target, warn, player);
    }

    private static void checkPunishment(Player target, Warn warn, Player player) {
        List<Warn> warns = BukkitAPI.getCommonAPI().getWarns(target.getUniqueId());
        int count = (int) warns.stream().filter(warn1 -> warn1.getReason().equalsIgnoreCase(warn.getReason())).count();

        if (count >= 3) {
            player.sendMessage(ChatUtil.prefix("&fAttention, ce joueur a un total de &a" + count + " &fwarns pour la raison &c" + warn.getReason()));
            TextComponent textComponent = new TextComponent("§a§l[MENU DES SANCTIONS]");
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatUtil.prefix("&cCliquez ici pour voir les sanctions"))
            }));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ss " + target.getName()));
            player.spigot().sendMessage(textComponent);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum WarnsCategory {
        HOST("&cCatégorie Host", Heads.COMMAND_BLOCK.toItemStack()),
        CHAT("&cCatégorie Chat", new ItemStack(Material.BOOK_AND_QUILL)),
        MUMBLE("&cCatégorie Mumble", new ItemStack(Material.ENCHANTED_BOOK));

        private final String name;
        private final ItemStack icon;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Warns {
        GROUPES("Non respect des groupes", WarnsCategory.HOST),
        RULES("Non respect des règles", WarnsCategory.HOST),
        FK("FreeKill", WarnsCategory.HOST),
        FP("FreePunch", WarnsCategory.HOST),
        DEVO("Devo", WarnsCategory.HOST),
        FDEVO("Fake Devo", WarnsCategory.HOST),

        ML("Mauvais langage", WarnsCategory.CHAT),
        SPAM("Spam", WarnsCategory.CHAT),
        FLOOD("Flood", WarnsCategory.CHAT),
        PROVOC("Provocation", WarnsCategory.CHAT),

        MDR("Manque de respect", WarnsCategory.MUMBLE),
        TROLL("Troll", WarnsCategory.MUMBLE),
        SPAMV("Spam vocal", WarnsCategory.MUMBLE),
        PUB("Publicité", WarnsCategory.MUMBLE),
        HOMO("Homophobie", WarnsCategory.MUMBLE),
        RACISME("Racisme", WarnsCategory.MUMBLE),
        PROVOC_M("Provocation", WarnsCategory.MUMBLE),
        INSULTES("Insultes", WarnsCategory.MUMBLE);

        private final String name;
        private final WarnsCategory category;
    }

}
