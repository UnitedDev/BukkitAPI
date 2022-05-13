package fr.kohei.punishment.impl.cheat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class XRayPunishment extends AbstractPunishment {
    public XRayPunishment() {
        super(Punishments.XRAY);
        this.punishmentAdapters.add(new PunishmentAdapter("3M", PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("9M", PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentType.BAN, 3));
    }
}
