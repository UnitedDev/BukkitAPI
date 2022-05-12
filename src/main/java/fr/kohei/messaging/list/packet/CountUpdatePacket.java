package fr.kohei.messaging.list.packet;

import fr.kohei.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CountUpdatePacket implements Packet {

    private final int normalPlayers;
    private final int totalPlayers;

}
