package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.ServerDeletePacket;
import fr.kohei.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.utils.messaging.pigdin.PacketListener;

public class ServerDeleteSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(ServerDeletePacket packet) {
        BukkitAPI.getCommonAPI().getServerCache().attemptDeletePort(packet.getPort());
    }

}
