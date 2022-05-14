package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.UHCUpdatePacket;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;

public class UHCUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(UHCUpdatePacket packet) {
        BukkitAPI.getServerCache().updateUhcServer(packet.getUhcServer());
    }

}
