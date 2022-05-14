package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.messaging.packet.PunishmentPacket;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;
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
                BukkitAPI.getCommonAPI().getProfile(packet.getData().getExecutor()).getDisplayName();
        String punishedDisplay = punishedProfile.getRank().getTabPrefix().substring(0, 2) +
                BukkitAPI.getCommonAPI().getProfile(packet.getData().getPunished()).getDisplayName();

        for (Player player : Bukkit.getOnlinePlayers()) {
            ProfileData profile1 = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

            if(!packet.isAnnounce()) break;

            if (profile1.getRank().getPermissionPower() >= 39) {
                if (packet.getAccepter() == null) {
                    player.sendMessage(ChatUtil.prefix(punishedDisplay + " &fa été sanctionné(e) par " + executorDisplay + " &fpour &c"
                            + packet.getData().getReason()));
                } else {

                    player.sendMessage(ChatUtil.prefix(punishedDisplay + " &fa été sanctionné(e) par " + executorDisplay + " &fpour &c"
                            + packet.getData().getReason() + " &f(&c⚑&9" + packet.getAccepter() + "&f)"));
                }
            }

            if(packet.getData().getPunished().equals(player.getUniqueId())) {
                Bukkit.getScheduler().runTask(BukkitAPI.getPlugin(), () -> player.kickPlayer(BukkitAPI.getPunishmentManager().getKickMessage(packet.getData())));
            }
        }


    }
}
