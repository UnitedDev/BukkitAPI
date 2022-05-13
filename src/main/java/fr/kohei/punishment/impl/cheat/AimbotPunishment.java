package fr.kohei.punishment.impl.cheat;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class AimbotPunishment extends AbstractPunishment {
    public AimbotPunishment() {
        super(Punishments.AIM_BOT);
        this.punishmentAdapters.add(new PunishmentAdapter("6M", PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("12M", PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentType.BAN, 3));
    }
}
