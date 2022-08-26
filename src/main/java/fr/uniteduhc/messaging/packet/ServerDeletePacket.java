package fr.uniteduhc.messaging.packet;

import fr.uniteduhc.common.utils.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServerDeletePacket implements Packet {

    private final int port;

}
