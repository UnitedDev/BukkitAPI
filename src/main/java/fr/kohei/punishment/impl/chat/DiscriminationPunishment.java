package fr.kohei.punishment.impl.chat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class DiscriminationPunishment extends AbstractPunishment {
    public DiscriminationPunishment() {
        super(Punishments.DISCRIMINATION);
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter("90d", PunishmentType.BAN, 2));
        punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentType.BAN, 3));
    }
}
