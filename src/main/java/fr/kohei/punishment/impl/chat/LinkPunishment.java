package fr.kohei.punishment.impl.chat;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class LinkPunishment extends AbstractPunishment {
    public LinkPunishment() {
        super(Punishments.LINK);
        punishmentAdapters.add(new PunishmentAdapter("12h", PunishmentData.PunishmentType.MUTE, 1));
        punishmentAdapters.add(new PunishmentAdapter("1d", PunishmentData.PunishmentType.MUTE, 2));
        punishmentAdapters.add(new PunishmentAdapter("3d", PunishmentData.PunishmentType.MUTE, 3));
        punishmentAdapters.add(new PunishmentAdapter("6d", PunishmentData.PunishmentType.MUTE, 4));
        punishmentAdapters.add(new PunishmentAdapter("12d", PunishmentData.PunishmentType.MUTE, 5));
        punishmentAdapters.add(new PunishmentAdapter("24d", PunishmentData.PunishmentType.MUTE, 6));
        punishmentAdapters.add(new PunishmentAdapter("48d", PunishmentData.PunishmentType.MUTE, 7));
        punishmentAdapters.add(new PunishmentAdapter("96d", PunishmentData.PunishmentType.MUTE, 8));
        punishmentAdapters.add(new PunishmentAdapter("192d", PunishmentData.PunishmentType.MUTE, 9));
        punishmentAdapters.add(new PunishmentAdapter("384d", PunishmentData.PunishmentType.MUTE, 10));
    }
}
