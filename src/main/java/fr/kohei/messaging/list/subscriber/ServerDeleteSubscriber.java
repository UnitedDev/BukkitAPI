package fr.kohei.messaging.list.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.list.packet.ServerDeletePacket;
import fr.kohei.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.messaging.pigdin.PacketListener;

public class ServerDeleteSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(ServerDeletePacket packet) {
        BukkitAPI.getServerCache().attemptDeletePort(packet.getPort());
    }

}
