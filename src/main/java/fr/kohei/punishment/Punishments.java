package fr.kohei.punishment;

import fr.kohei.punishment.impl.abuse.*;
import fr.kohei.punishment.impl.chat.*;
import fr.kohei.punishment.impl.cheat.*;
import fr.kohei.punishment.impl.gameplay.*;
import fr.kohei.punishment.impl.vc.DisrespectVCPunishment;
import fr.kohei.punishment.impl.vc.TrollPunishment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;


/**
 * Punishments enum to manager all the punishments.
 * This class is mainly used for the punishment gui.
 *
 * @author Rhodless
 * @see PunishMenu
 */
@Getter
@RequiredArgsConstructor
public enum Punishments {

    /*
     * ABUSE
     */
    BLACKLIST(Material.BEDROCK, PunishCategory.ABUSE, "Blacklist", new BlacklistPunishment()),
    ANTI_JEU(Material.LAVA_BUCKET, PunishCategory.ABUSE, "Anti-Jeu", new AntiJeuPunishment()),
    ABUSE_PERMISSIONS(Material.BOOK, PunishCategory.ABUSE, "Abus de Permissions", new AbusPermsPunishment()),
    USE_BUG(Material.LEASH, PunishCategory.ABUSE, "UseBug", new UseBugPunishment()),
    EVADING_PUNISHMENT(Material.ENCHANTED_BOOK, PunishCategory.ABUSE, "Contournement de Sanction", new EvadingPunishment()),

    /*
     * CHEAT
     */
    XRAY(Material.DIAMOND_ORE, PunishCategory.CHEAT, "X-Ray", new XRayPunishment()),
    REACH(Material.DIAMOND_SWORD, PunishCategory.CHEAT, "Reach", new ReachPunishment()),
    AIM_BOT(Material.COMPASS, PunishCategory.CHEAT, "Aimbot", new AimbotPunishment()),
    KILL_AURA(Material.DIAMOND_AXE, PunishCategory.CHEAT, "Kill-Aura", new KillAuraPunishment()),
    SPEED_HACK(Material.SUGAR, PunishCategory.CHEAT, "Speed-Hack", new SpeedHackPunishment()),
    FLY(Material.FEATHER, PunishCategory.CHEAT, "Fly", new FlyPunishment()),

    /*
     * GAMEPLAY
     */
    GHOST_KILL(Material.BONE, PunishCategory.GAMEPLAY, "Ghost-Kill", new GhostKillPunishment()),
    AFK_KILL(Material.LAVA_BUCKET, PunishCategory.GAMEPLAY, "AFK-Kill", new AFKKillPunishment()),
    FREE_KILL(Material.DIAMOND_SWORD, PunishCategory.GAMEPLAY, "Free-Kill", new FreeKillPunishment()),
    FREE_PUNCH(Material.BOW, PunishCategory.GAMEPLAY, "Free-Punch", new FreePunchPunishment()),
    DISCONNECT(Material.BARRIER, PunishCategory.GAMEPLAY, "Deconnexion", new DeconnexionPunishment()),
    DECO_COMBAT(Material.IRON_SWORD, PunishCategory.GAMEPLAY, "Deco Combat", new DecoCombatPunishment()),
    AFK(Material.COMPASS, PunishCategory.GAMEPLAY, "AFK", new AFKPunishment()),
    SKIN(Material.SKULL_ITEM, PunishCategory.GAMEPLAY, "Skin inapproprié", new SkinPunishment()),
    CAPE(Material.BANNER, PunishCategory.GAMEPLAY, "Cape inapproprié", new CapePunishment()),
    PSEUDO(Material.EYE_OF_ENDER, PunishCategory.GAMEPLAY, "Pseudo inapproprié", new PseudoPunishment()),
    SPEC_HELP(Material.BOOK, PunishCategory.GAMEPLAY, "Deco Combat", new SpecHelpPunishment()),
    RESPECT_HOST(Material.PAPER, PunishCategory.GAMEPLAY, "Non respect des règles de l'host", new RespectHostPunishment()),
    SPOIL_ITEM(Material.GOLD_HOE, PunishCategory.GAMEPLAY, "Devo item", new DevoItemPunishment()),
    SPOIL(Material.LEASH, PunishCategory.GAMEPLAY, "Devo indice", new DevoIndicePunishment()),


    /*
     * CHAT TEXTUEL
     */
    SPAM(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Spam", new SpamPunishment()),
    FLOOD(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Flood", new FloodPunishment()),
    AD(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Publicité", new PubPunishment()),
    LINK(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Lien quelconque", new LinkPunishment()),
    PROVOCATION(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Provocation", new ProvocationPunishment()),
    DISRESPECT(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Insulte", new InsultePunishment()),
    MENACE(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Menace", new MenacePunishment()),
    DISCRIMINATION(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Discrimination", new DiscriminationPunishment()),
    DOX(Material.BOOK_AND_QUILL, PunishCategory.CHAT_IN_GAME, "Divulgation d'informations (DOX)", new DoxPunishment()),


    /*
     * CHAT VOCAL
     */
    TROLL(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Troll", new TrollPunishment()),
    SPAM_VC(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Spam", new TrollPunishment()),
    SOUND_BOARD(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Soundboard", new TrollPunishment()),
    AD_VC(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Publicité", new TrollPunishment()),
    DISCRIMINATION_VC(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Discrimination", new DiscriminationPunishment()),
    DISRESPECT_VC(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Insulte", new DisrespectVCPunishment()),
    PROVOCATION_VC(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Provocation", new DisrespectVCPunishment()),
    BULLY(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Harcèlement", new DisrespectVCPunishment()),
    RACISM(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Incitation à la haine", new DisrespectVCPunishment()),
    DOX_VC(Material.ENCHANTED_BOOK, PunishCategory.VOICE_CHAT, "Divulgation d'informations (DOX)", new DoxPunishment());

    private final Material material;
    private final PunishCategory category;
    private final String display;
    private final AbstractPunishment abstractPunishment;

    /**
     * Category enum for the punishments.
     * This class is mainly used and displayed in the punishment
     * category menu.
     *
     * @author Rhodless
     * @see PunishCategory
     */
    @Getter
    @RequiredArgsConstructor
    public enum PunishCategory {
        ABUSE("Abus"),
        CHEAT("Cheat"),
        GAMEPLAY("Gameplay"),
        CHAT_IN_GAME("Chat Textuel"),
        VOICE_CHAT("Chat Vocal");

        private final String display;
    }

}
