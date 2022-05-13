package fr.kohei.punishment.impl.chat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class DoxPunishment extends AbstractPunishment {
    public DoxPunishment() {
        super(Punishments.DOX);
        punishmentAdapters.add(new PunishmentAdapter("90d", PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentType.BAN, 2));
    }
}
