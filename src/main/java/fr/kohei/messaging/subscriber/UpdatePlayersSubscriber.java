package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.UpdatePlayersPacket;
import fr.kohei.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.utils.messaging.pigdin.PacketListener;

public class UpdatePlayersSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(UpdatePlayersPacket packet) {
        BukkitAPI.getCommonAPI().getServerCache().setPlayers(packet.getPlayers());
    }

}
