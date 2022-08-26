package fr.uniteduhc.messaging.subscriber;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.messaging.packet.CountUpdatePacket;
import fr.uniteduhc.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.uniteduhc.common.utils.messaging.pigdin.PacketListener;

public class CountUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(CountUpdatePacket packet) {
        BukkitAPI.setNormalPlayers(packet.getNormalPlayers());
        BukkitAPI.setTotalPlayers(packet.getTotalPlayers());
    }

}
