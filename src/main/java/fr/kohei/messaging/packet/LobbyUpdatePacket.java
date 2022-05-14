package fr.kohei.messaging.packet;

import fr.kohei.manager.server.LobbyServer;
import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LobbyUpdatePacket implements Packet {

    private final LobbyServer lobbyServer;

}
