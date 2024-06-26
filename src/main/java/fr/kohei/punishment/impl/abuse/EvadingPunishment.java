package fr.kohei.punishment.impl.abuse;

import fr.kohei.common.cache.data.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class EvadingPunishment extends AbstractPunishment {
    public EvadingPunishment() {
        super(Punishments.EVADING_PUNISHMENT);
        this.punishmentAdapters.add(new PunishmentAdapter(-2, PunishmentData.PunishmentType.BAN, 1));
    }
}
