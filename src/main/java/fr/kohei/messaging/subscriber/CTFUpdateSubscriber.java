package fr.kohei.messaging.subscriber;

import fr.kohei.BukkitAPI;
import fr.kohei.messaging.packet.CTFUpdatePacket;
import fr.kohei.common.messaging.pigdin.IncomingPacketHandler;
import fr.kohei.common.messaging.pigdin.PacketListener;

public class CTFUpdateSubscriber implements PacketListener {

    @IncomingPacketHandler
    public void onReceive(CTFUpdatePacket packet) {
        BukkitAPI.getServerCache().updateCTFServer(packet.getCtfServer());
    }

}
