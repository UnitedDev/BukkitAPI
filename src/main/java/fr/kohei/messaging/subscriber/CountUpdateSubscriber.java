package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.CountUpdatePacket;
import fr.kohei.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.utils.messaging.pigdin.PacketListener;

public class CountUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(CountUpdatePacket packet) {
        BukkitAPI.setNormalPlayers(packet.getNormalPlayers());
        BukkitAPI.setTotalPlayers(packet.getTotalPlayers());
    }

}
