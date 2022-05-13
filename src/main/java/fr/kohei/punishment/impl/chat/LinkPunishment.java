package fr.kohei.punishment.impl.chat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class LinkPunishment extends AbstractPunishment {
    public LinkPunishment() {
        super(Punishments.LINK);
        punishmentAdapters.add(new PunishmentAdapter("12h", PunishmentType.MUTE, 1));
        punishmentAdapters.add(new PunishmentAdapter("1d", PunishmentType.MUTE, 2));
        punishmentAdapters.add(new PunishmentAdapter("3d", PunishmentType.MUTE, 3));
        punishmentAdapters.add(new PunishmentAdapter("6d", PunishmentType.MUTE, 4));
        punishmentAdapters.add(new PunishmentAdapter("12d", PunishmentType.MUTE, 5));
        punishmentAdapters.add(new PunishmentAdapter("24d", PunishmentType.MUTE, 6));
        punishmentAdapters.add(new PunishmentAdapter("48d", PunishmentType.MUTE, 7));
        punishmentAdapters.add(new PunishmentAdapter("96d", PunishmentType.MUTE, 8));
        punishmentAdapters.add(new PunishmentAdapter("192d", PunishmentType.MUTE, 9));
        punishmentAdapters.add(new PunishmentAdapter("384d", PunishmentType.MUTE, 10));
    }
}
