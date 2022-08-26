package fr.uniteduhc.punishment.impl.cheat;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

/**
 * @author Rhodless
 */
public class SpeedHackPunishment extends AbstractPunishment {
    public SpeedHackPunishment() {
        super(Punishments.SPEED_HACK);
        this.punishmentAdapters.add(new PunishmentAdapter("6M", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("12M", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 3));
    }
}
