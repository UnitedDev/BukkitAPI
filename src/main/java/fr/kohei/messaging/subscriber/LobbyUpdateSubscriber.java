package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.LobbyUpdatePacket;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;

public class LobbyUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(LobbyUpdatePacket packet) {
        BukkitAPI.getServerCache().updateLobbyServer(packet.getLobbyServer());
    }

}
