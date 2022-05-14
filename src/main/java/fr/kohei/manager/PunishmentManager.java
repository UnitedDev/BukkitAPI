package fr.kohei.manager;

import com.sun.java.browser.net.ProxyService;
import fr.kohei.BukkitAPI;
import fr.kohei.common.RedisProvider;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.common.cache.PunishmentData;
import fr.kohei.manager.server.UHCServer;
import fr.kohei.messaging.packet.PunishmentAskPacket;
import fr.kohei.messaging.packet.PunishmentPacket;
import fr.kohei.punishment.menu.PunishmentAskMenu;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.DiscordWebhook;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PunishmentManager {

    private final HashMap<UUID, String> gsonAskPunishment = new HashMap<>();

    public PunishmentData getMute(UUID uuid) {
        return BukkitAPI.getCommonAPI().getPunishments(uuid).stream()
                .filter(data -> data.getPunishmentType() == PunishmentData.PunishmentType.MUTE)
                .filter(this::isValid)
                .findFirst().orElse(null);
    }

    public PunishmentData getBan(UUID uuid) {
        return BukkitAPI.getCommonAPI().getPunishments(uuid).stream()
                .filter(data -> data.getPunishmentType() == PunishmentData.PunishmentType.BAN)
                .filter(this::isValid)
                .findFirst().orElse(null);
    }

    public PunishmentData getBlacklist(UUID uuid) {
        return BukkitAPI.getCommonAPI().getPunishments(uuid).stream()
                .filter(data -> data.getPunishmentType() == PunishmentData.PunishmentType.BLACKLIST)
                .filter(this::isValid)
                .findFirst().orElse(null);
    }

    public boolean isValid(PunishmentData data) {
        return data.getDuration() < 0 || System.currentTimeMillis() < data.getDate().getTime() + data.getDuration();
    }

    public void attemptPunishment(Player player, PunishmentData punishment) {
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (profile.getRank().getPermissionPower() < 40 && punishment.getPunishmentType() != PunishmentData.PunishmentType.MUTE) {
            boolean online = false;
            for (UUID uuid : BukkitAPI.getServerCache().getPlayers().keySet()) {
                ProfileData uProfile = BukkitAPI.getCommonAPI().getProfile(uuid);

                if (uProfile.getRank().permissionPower() >= 40) {
                    online = true;
                    break;
                }
            }

            if (!online) {
                player.sendMessage(ChatUtil.prefix("&cIl semblerait qu'aucun modérateur est connecté."));

                TextComponent text = new TextComponent(ChatUtil.translate("&a&l[CONFIRMER LE BANNISSEMENT]"));
                text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                        new TextComponent(ChatUtil.translate("&c⚠ Action effectuée au clic"))
                }));
                UUID uuid = UUID.randomUUID();
                BukkitAPI.getPunishmentManager().getGsonAskPunishment().put(uuid, RedisProvider.redisProvider.GSON.toJson(punishment));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                        "/forceban " + uuid
                ));

                player.spigot().sendMessage(text);
                return;
            }

            new PunishmentAskMenu(punishment).openMenu(player);
            return;
        }

        int found = -1;

        for (Map.Entry<Integer, UHCServer> entry : BukkitAPI.getServerCache().getUhcServers().entrySet()) {
            Integer integer = entry.getKey();
            UHCServer uhcServer = entry.getValue();
            if (uhcServer.getStatus() == UHCServer.ServerStatus.PLAYING && uhcServer.getUuids().contains(punishment.getPunished())) {
                found = integer;
                break;
            }
        }

        if (found != -1) {
            player.sendMessage(ChatUtil.prefix("&c" + BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getDisplayName() +
                    " est en jeu sur le serveur uhc-" + found + ", voulez-vous tout de même le sanctionner ?"));
            TextComponent text = new TextComponent(ChatUtil.translate("&a&l[CONFIRMER LE BANNISSEMENT]"));
            text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatUtil.translate("&c⚠ Action effectuée au clic"))
            }));
            UUID uuid = UUID.randomUUID();
            BukkitAPI.getPunishmentManager().getGsonAskPunishment().put(uuid, RedisProvider.redisProvider.GSON.toJson(punishment));
            text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    "/forceban " + uuid
            ));
            return;
        }

        this.punish(punishment, null);
    }

    public void ask(Player player, PunishmentData data, boolean preuve) {
        player.sendMessage(ChatUtil.prefix("&fVous avez fait une demande de &asanction&f."));
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentAskPacket(player.getUniqueId(), data, preuve));
    }

    public void punish(PunishmentData data, String accepter) {
        BukkitAPI.getCommonAPI().updatePunishment(data);
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentPacket(data, accepter, true));

        DiscordWebhook webhook = new DiscordWebhook(DiscordWebhook.PUNISH_CHANNEL);

        DiscordWebhook.EmbedObject object = new DiscordWebhook.EmbedObject();
        object.setFooter("Sanctions", DiscordWebhook.LOGO_URL);
        object.setTitle("Nouvelle Sanction (" + Bukkit.getPort() + ")");
        object.addField("Raison", data.getReason(), true);
        object.addField("Date", TimeUtil.formatDate(data.getDate().getTime()), true);
        object.addField("Durée", TimeUtil.getReallyNiceTime(data.getDuration()), true);
        object.addField("Joueur", BukkitAPI.getCommonAPI().getProfile(data.getPunished()).getDisplayName(), true);
        object.addField("Executeur", BukkitAPI.getCommonAPI().getProfile(data.getExecutor()).getDisplayName(), true);
        if(accepter != null) object.addField("Accepteur", accepter, true);

        webhook.addEmbed(object);
        webhook.execute();
    }

    public String getKickMessage(PunishmentData punishment) {
        if (punishment.getPunishmentType() == PunishmentData.PunishmentType.BLACKLIST) {
            return ChatUtil.translate(" " + "\n" +
                    "&cVous êtes blacklist de ce serveur" + "\n" +
                    " " + "\n" +
                    "&7» &fRaison: &c" + punishment.getReason() + "\n" +
                    "&7» &fBanni le: &c" + TimeUtil.formatDate(punishment.getDate().getTime()) + "\n" +
                    "&7» &fBanni par: &c" + BukkitAPI.getCommonAPI().getProfile(punishment.getExecutor()).getDisplayName() + "\n" +
                    " " + "\n" +
                    "&fSi vous pensez qu'il s'agit d'une erreur," + "\n" +
                    "&frendez-vous sur &cNotre Discord" + "\n"
            );
        }
        return ChatUtil.translate(" " + "\n" +
                "&cVous êtes banni de ce serveur" + "\n" +
                " " + "\n" +
                "&7» &fRaison: &c" + punishment.getReason() + "\n" +
                "&7» &fBanni le: &c" + TimeUtil.formatDate(punishment.getDate().getTime()) + "\n" +
                " " + "\n" +
                "&7» &fBanni par: &c" + BukkitAPI.getCommonAPI().getProfile(punishment.getExecutor()).getDisplayName() + "\n" +
                "&7» &fDurée: &c" + TimeUtil.getReallyNiceTime(punishment.getDuration()) + "\n" +
                " " + "\n" +
                "&fSi vous pensez qu'il s'agit d'une erreur," + "\n" +
                "&frendez-vous sur &cNotre Discord" + "\n"
        );
    }

}
