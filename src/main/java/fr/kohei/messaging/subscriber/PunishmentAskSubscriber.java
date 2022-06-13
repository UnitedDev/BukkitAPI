package fr.kohei.messaging.subscriber;

import com.google.common.io.BaseEncoding;
import fr.kohei.BukkitAPI;
import fr.kohei.common.RedisProvider;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.messaging.packet.PunishmentAskPacket;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;
import fr.kohei.utils.ChatUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PunishmentAskSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(PunishmentAskPacket packet) {
        ProfileData executorProfile = BukkitAPI.getCommonAPI().getProfile(packet.getExecutor());
        ProfileData targetProfile = BukkitAPI.getCommonAPI().getProfile(packet.getPunishmentData().getPunished());

        String executorDisplay = executorProfile.getRank().getTabPrefix().substring(0, 2) + executorProfile.getDisplayName();
        String targetDisplay = targetProfile.getRank().getTabPrefix().substring(0, 2) + targetProfile.getDisplayName();

        Bukkit.getOnlinePlayers().forEach(player -> {
            ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());
            if (profile.getRank().permissionPower() < 40) return;

            player.sendMessage(ChatUtil.prefix(executorDisplay + " &fveut sanctionner " + targetDisplay + " &fpour &c" +
                    packet.getPunishmentData().getReason() + " &f(&7Preuve: " + (packet.isPreuve() ? "&aOui" : "&cNon")));

            TextComponent accept = new TextComponent(ChatUtil.translate("&a&l[VALIDER] "));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatUtil.translate("&c⚠ &cAttention: &cVous devez vous assurer que la sanction a lieu d'être ! Si ce n'est pas le cas, vous serez tenu comme responsable."))
            }));
            UUID random = UUID.randomUUID();
            BukkitAPI.getPunishmentManager().getGsonAskPunishment().put(random, RedisProvider.redisProvider.GSON.toJson(packet));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptban " + random));

            TextComponent decline = new TextComponent(ChatUtil.translate("&c&l[REFUSER]"));
            decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatUtil.translate("&c⚠ Action effectuée au clic"))
            }));
            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/declineban " + random));

            player.spigot().sendMessage(accept, decline);
        });
    }

}
