package fr.kohei.punishment.impl.gameplay;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class CapePunishment extends AbstractPunishment {
    public CapePunishment() {
        super(Punishments.CAPE);
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentType.BAN, 1));
    }
}
