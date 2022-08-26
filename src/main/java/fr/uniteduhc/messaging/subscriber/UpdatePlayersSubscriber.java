package fr.uniteduhc.messaging.subscriber;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.messaging.packet.UpdatePlayersPacket;
import fr.uniteduhc.common.utils.messaging.pigdin.IncomingPacketHandler;
import fr.uniteduhc.common.utils.messaging.pigdin.PacketListener;

public class UpdatePlayersSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(UpdatePlayersPacket packet) {
        BukkitAPI.getCommonAPI().getServerCache().setPlayers(packet.getPlayers());
    }

}
