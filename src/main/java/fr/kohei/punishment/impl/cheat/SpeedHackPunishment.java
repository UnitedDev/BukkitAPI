package fr.kohei.punishment.impl.cheat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class SpeedHackPunishment extends AbstractPunishment {
    public SpeedHackPunishment() {
        super(Punishments.SPEED_HACK);
        this.punishmentAdapters.add(new PunishmentAdapter("6M", PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("12M", PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentType.BAN, 3));
    }
}
