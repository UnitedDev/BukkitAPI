package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.utils.messaging.pigdin.PacketListener;
import fr.kohei.messaging.packet.MessagePacket;
import org.bukkit.Bukkit;

public class MessageSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(MessagePacket packet) {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> BukkitAPI.getCommonAPI().getProfile(player.getUniqueId()).getRank().getPermissionPower() >= packet.getPower())
                .forEach(player -> packet.getMessages().forEach(message -> player.spigot().sendMessage(message)));
    }

}
