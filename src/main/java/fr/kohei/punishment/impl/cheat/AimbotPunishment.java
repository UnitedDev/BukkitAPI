package fr.kohei.punishment.impl.cheat;

import fr.kohei.common.cache.data.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class AimbotPunishment extends AbstractPunishment {
    public AimbotPunishment() {
        super(Punishments.AIM_BOT);
        this.punishmentAdapters.add(new PunishmentAdapter("6M", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("12M", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 3));
    }
}
