package fr.kohei.messaging.packet;

import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerDeletePacket implements Packet {

    private final int port;

}
