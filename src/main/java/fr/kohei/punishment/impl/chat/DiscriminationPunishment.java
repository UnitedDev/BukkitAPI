package fr.kohei.punishment.impl.chat;

import fr.kohei.common.cache.data.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class DiscriminationPunishment extends AbstractPunishment {
    public DiscriminationPunishment() {
        super(Punishments.DISCRIMINATION);
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentData.PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter("90d", PunishmentData.PunishmentType.BAN, 2));
        punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 3));
    }
}
