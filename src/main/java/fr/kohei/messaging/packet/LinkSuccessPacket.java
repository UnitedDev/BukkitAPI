package fr.kohei.messaging.packet;

import fr.kohei.common.cache.ProfileData;
import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LinkSuccessPacket implements Packet {

    private final ProfileData data;
    private final String discordId;

}
