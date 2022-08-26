package fr.uniteduhc.messaging.packet;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.common.utils.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PunishmentPacket implements Packet {

    private final PunishmentData data;
    private final String accepter;
    private final boolean announce;

}
