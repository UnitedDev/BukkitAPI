package fr.kohei.messaging.list.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.list.packet.LobbyUpdatePacket;
import fr.kohei.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.messaging.pigdin.PacketListener;

public class LobbyUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(LobbyUpdatePacket packet) {
        BukkitAPI.getServerCache().updateLobbyServer(packet.getLobbyServer());
        }

}
