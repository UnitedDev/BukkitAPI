package fr.kohei.messaging.list.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.list.packet.CountUpdatePacket;
import fr.kohei.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.messaging.pigdin.PacketListener;

public class CountUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(CountUpdatePacket packet) {
        BukkitAPI.setNormalPlayers(packet.getNormalPlayers());
        BukkitAPI.setTotalPlayers(packet.getTotalPlayers());
    }

}
