package fr.kohei.punishment.impl.chat;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class DoxPunishment extends AbstractPunishment {
    public DoxPunishment() {
        super(Punishments.DOX);
        punishmentAdapters.add(new PunishmentAdapter("90d", PunishmentData.PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 2));
    }
}
