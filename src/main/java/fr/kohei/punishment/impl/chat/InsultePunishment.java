package fr.kohei.punishment.impl.chat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class InsultePunishment extends AbstractPunishment {
    public InsultePunishment() {
        super(Punishments.DISRESPECT);
        punishmentAdapters.add(new PunishmentAdapter("4h", PunishmentType.MUTE, 1));
        punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentType.MUTE, 2));
        punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentType.MUTE, 3));
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentType.MUTE, 4));
        punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentType.MUTE, 5));
        punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentType.MUTE, 6));
        punishmentAdapters.add(new PunishmentAdapter("240d", PunishmentType.MUTE, 7));
    }
}
