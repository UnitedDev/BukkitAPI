package fr.uniteduhc.punishment.impl.abuse;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

/**
 * @author Rhodless
 */
public class EvadingPunishment extends AbstractPunishment {
    public EvadingPunishment() {
        super(Punishments.EVADING_PUNISHMENT);
        this.punishmentAdapters.add(new PunishmentAdapter(-2, PunishmentData.PunishmentType.BAN, 1));
    }
}
