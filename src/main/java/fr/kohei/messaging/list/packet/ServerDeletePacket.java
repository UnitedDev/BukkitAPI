package fr.kohei.messaging.list.packet;

import fr.kohei.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerDeletePacket implements Packet {

    private final int port;

}
