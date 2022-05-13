package fr.kohei.messaging.list.packet;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PunishmentPacket implements Packet {

    private final PunishmentData data;
    private final String accepter;

}
