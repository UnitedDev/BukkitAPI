package fr.kohei.punishment.impl.cheat;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class ReachPunishment extends AbstractPunishment {
    public ReachPunishment() {
        super(Punishments.REACH);
        this.punishmentAdapters.add(new PunishmentAdapter("6M", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("12M", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 3));
    }
}
