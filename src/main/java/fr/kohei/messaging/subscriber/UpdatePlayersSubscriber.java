package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.UpdatePlayersPacket;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;

public class UpdatePlayersSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(UpdatePlayersPacket packet) {
        BukkitAPI.getServerCache().setPlayers(packet.getPlayers());
    }

}
