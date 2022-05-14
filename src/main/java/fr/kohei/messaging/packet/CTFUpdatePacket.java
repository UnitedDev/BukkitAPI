package fr.kohei.messaging.packet;

import fr.kohei.manager.server.CTFServer;
import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CTFUpdatePacket implements Packet {

    private final CTFServer ctfServer;

}
