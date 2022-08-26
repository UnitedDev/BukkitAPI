package fr.uniteduhc.messaging.subscriber;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.uniteduhc.common.utils.messaging.pigdin.PacketListener;
import fr.uniteduhc.messaging.packet.MessagePacket;
import org.bukkit.Bukkit;

public class MessageSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(MessagePacket packet) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> BukkitAPI.getCommonAPI().getProfile(player.getUniqueId()).getRank().getPermissionPower() >= packet.getPower())
                .forEach(player -> packet.getMessages().forEach(message -> player.spigot().sendMessage(message)));
    }

}
