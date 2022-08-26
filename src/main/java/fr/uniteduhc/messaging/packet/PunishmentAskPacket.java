package fr.uniteduhc.messaging.packet;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.common.utils.messaging.pigdin.Packet;
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
