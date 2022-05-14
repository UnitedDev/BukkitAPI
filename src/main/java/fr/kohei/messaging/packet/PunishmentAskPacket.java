package fr.kohei.messaging.packet;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.common.messaging.pigdin.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PunishmentAskPacket implements Packet {
    private final UUID executor;
    private final PunishmentData punishmentData;
    private final boolean preuve;
}
