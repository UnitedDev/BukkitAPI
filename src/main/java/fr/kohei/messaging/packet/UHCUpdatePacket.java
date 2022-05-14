package fr.kohei.messaging.packet;

import fr.kohei.manager.server.UHCServer;
import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UHCUpdatePacket implements Packet {

    private final UHCServer uhcServer;

}
