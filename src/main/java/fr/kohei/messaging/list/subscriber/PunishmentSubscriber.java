package fr.kohei.messaging.list.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.messaging.list.packet.PunishmentPacket;
import fr.kohei.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.messaging.pigdin.PacketListener;
import fr.kohei.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PunishmentSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(PunishmentPacket packet) {
        ProfileData executorProfile = BukkitAPI.getCommonAPI().getProfile(packet.getData().getExecutor());
        ProfileData punishedProfile = BukkitAPI.getCommonAPI().getProfile(packet.getData().getPunished());

        String executorDisplay = executorProfile.getRank().getTabPrefix().substring(0, 2) +
                Bukkit.getOfflinePlayer(packet.getData().getExecutor()).getName();
        String punishedDisplay = punishedProfile.getRank().getTabPrefix().substring(0, 2) +
                Bukkit.getOfflinePlayer(packet.getData().getPunished()).getName();

        for (Player player : Bukkit.getOnlinePlayers()) {
            ProfileData profile1 = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

            if (profile1.getRank().getPermissionPower() >= 39) {
                if (packet.getAccepter() == null) {
                    player.sendMessage(ChatUtil.prefix(punishedDisplay + " &fa été sanctionné(e) par " + executorDisplay + " &fpour &c"
                            + packet.getData().getReason()));
                } else {
                    OfflinePlayer accepter = Bukkit.getOfflinePlayer(packet.getAccepter());
                    ProfileData accepterProfile = BukkitAPI.getCommonAPI().getProfile(accepter.getUniqueId());
                    String accepterDisplay = accepterProfile.getRank().getTabPrefix().substring(0, 2) + accepter.getName();

                    player.sendMessage(ChatUtil.prefix(punishedDisplay + " &fa été sanctionné(e) par " + executorDisplay + " &fpour &c"
                            + packet.getData().getReason() + " &f(&c⚑" + accepterDisplay + "&f)"));
                }
            }

            if(packet.getData().getPunished().equals(player.getUniqueId())) {
                player.kickPlayer(BukkitAPI.getPunishmentManager().getKickMessage(packet.getData()));
            }
        }


    }
}
