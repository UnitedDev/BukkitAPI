package fr.uniteduhc.punishment.impl.gameplay;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

/**
 * @author Rhodless
 */
public class PseudoPunishment extends AbstractPunishment {
    public PseudoPunishment() {
        super(Punishments.PSEUDO);
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 1));
    }
}
