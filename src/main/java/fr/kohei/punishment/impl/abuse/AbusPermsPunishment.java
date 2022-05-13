package fr.kohei.punishment.impl.abuse;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class AbusPermsPunishment extends AbstractPunishment {

    public AbusPermsPunishment() {
        super(Punishments.ABUSE_PERMISSIONS);
        this.punishmentAdapters.add(new PunishmentAdapter("4d", PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter("20d", PunishmentType.BAN, 3));
        this.punishmentAdapters.add(new PunishmentAdapter("40d", PunishmentType.BAN, 4));
        this.punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentType.BAN, 5));
        this.punishmentAdapters.add(new PunishmentAdapter("80d", PunishmentType.BAN, 6));
    }
}
