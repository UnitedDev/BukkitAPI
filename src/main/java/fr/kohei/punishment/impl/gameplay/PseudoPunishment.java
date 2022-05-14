package fr.kohei.punishment.impl.gameplay;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class PseudoPunishment extends AbstractPunishment {
    public PseudoPunishment() {
        super(Punishments.PSEUDO);
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 1));
    }
}
