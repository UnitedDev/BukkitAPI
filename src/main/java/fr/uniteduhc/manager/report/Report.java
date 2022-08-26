package fr.uniteduhc.manager.report;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Report {

    CHAT("Chat", Material.BOOK_AND_QUILL, "Spam", "Publicité", "Insulte"),
    COMBAT("Combat", Material.GOLD_SWORD, "Forcefield / Kill Aura", "Anti-knockback", "Reach augmentée", "Aimbot / Aimbow"),
    MOUVEMENT("Mouvement", Material.FEATHER, "Fly Hack", "Speed Hack", "Spider / Jump"),
    ANTI_JEU("Anti-Jeu", Material.FLINT_AND_STEEL, "Crossteam", "Digdow", "Tower"),
    AUTRE("Autre", Material.BOAT, "Skin innaproprié", "Pseudo innaproprié", "Autre..."),
    XRAY("xRay", Material.DIAMOND_ORE, "xRay"),
    ABUS("Abus", Material.ANVIL, "Abus de permissions"),

    ;

    private final String display;
    private final Material material;
    private final List<String> lore;

    Report(String display, Material material, String... lore) {
        this.display = display;
        this.material = material;
        this.lore = Arrays.asList(lore);
    }
}
