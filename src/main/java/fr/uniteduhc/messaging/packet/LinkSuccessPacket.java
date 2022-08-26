package fr.uniteduhc.messaging.packet;

import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.utils.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LinkSuccessPacket implements Packet {

    private final ProfileData data;
    private final String discordId;

}
