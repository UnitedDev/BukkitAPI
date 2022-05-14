package fr.kohei.punishment.impl.chat;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class ProvocationPunishment extends AbstractPunishment {
    public ProvocationPunishment() {
        super(Punishments.PROVOCATION);
        punishmentAdapters.add(new PunishmentAdapter("4h", PunishmentData.PunishmentType.MUTE, 1));
        punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentData.PunishmentType.MUTE, 2));
        punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentData.PunishmentType.MUTE, 3));
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentData.PunishmentType.MUTE, 4));
        punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentData.PunishmentType.MUTE, 5));
        punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentData.PunishmentType.MUTE, 6));
        punishmentAdapters.add(new PunishmentAdapter("240d", PunishmentData.PunishmentType.MUTE, 7));
    }
}
