package fr.kohei.messaging.list.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.common.RedisProvider;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.messaging.list.packet.PunishmentAskPacket;
import fr.kohei.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.messaging.pigdin.PacketListener;
import fr.kohei.utils.ChatUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.print.attribute.standard.RequestingUserName;

public class PunishmentAskSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(PunishmentAskPacket packet) {
        OfflinePlayer executor = Bukkit.getOfflinePlayer(packet.getExecutor());
        OfflinePlayer target = Bukkit.getOfflinePlayer(packet.getPunishmentData().getPunished());

        ProfileData executorProfile = BukkitAPI.getCommonAPI().getProfile(executor.getUniqueId());
        ProfileData targetProfile = BukkitAPI.getCommonAPI().getProfile(target.getUniqueId());

        String executorDisplay = executorProfile.getRank().getTabPrefix().substring(0, 2) + executor.getName();
        String targetDisplay = targetProfile.getRank().getTabPrefix().substring(0, 2) + target.getName();

        Bukkit.getOnlinePlayers().forEach(player -> {
            ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());
            if (profile.getRank().permissionPower() < 40) return;

            player.sendMessage(ChatUtil.prefix(executorDisplay + " &fveut sanctionner " + targetDisplay + " &fpour &c" +
                    packet.getPunishmentData().getReason() + " &f(&7Preuve: " + (packet.isPreuve() ? "&aOui" : "&cNon")));

            TextComponent accept = new TextComponent(ChatUtil.translate("&a&l[VALIDER] "));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatUtil.translate("&c⚠ &cAttention: &cVous devez vous assurer que la sanction a lieu d'être ! Si ce n'est pas le cas, vous serez tenu comme responsable."))
            }));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acceptban " + RedisProvider.redisProvider.GSON.toJson(packet)));

            TextComponent decline = new TextComponent(ChatUtil.translate("&c&l[REFUSER]"));
            decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{
                    new TextComponent(ChatUtil.translate("&c⚠ Action effectuée au clic"))
            }));
            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/declineban " + RedisProvider.redisProvider.GSON.toJson(packet)));

            player.spigot().sendMessage(accept, decline);
        });
    }

}
