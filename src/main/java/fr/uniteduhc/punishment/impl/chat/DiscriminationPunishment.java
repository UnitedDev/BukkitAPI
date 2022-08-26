package fr.uniteduhc.punishment.impl.chat;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

public class DiscriminationPunishment extends AbstractPunishment {
    public DiscriminationPunishment() {
        super(Punishments.DISCRIMINATION);
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentData.PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter("90d", PunishmentData.PunishmentType.BAN, 2));
        punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 3));
    }
}
