package fr.kohei.messaging.packet;

import fr.kohei.common.cache.ProfileData;
import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AttemptLinkPacket implements Packet {

    private final ProfileData profileData;
    private final String discordId;
    private final String discordDisplay;

}
