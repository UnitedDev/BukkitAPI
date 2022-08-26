package fr.uniteduhc.messaging.subscriber;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.messaging.packet.ServerDeletePacket;
import fr.uniteduhc.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.uniteduhc.common.utils.messaging.pigdin.PacketListener;

public class ServerDeleteSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(ServerDeletePacket packet) {
        BukkitAPI.getCommonAPI().getServerCache().attemptDeletePort(packet.getPort());
    }

}
