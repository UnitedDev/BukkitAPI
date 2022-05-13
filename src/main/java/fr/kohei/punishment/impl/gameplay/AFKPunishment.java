package fr.kohei.punishment.impl.gameplay;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class AFKPunishment extends AbstractPunishment {

    public AFKPunishment() {
        super(Punishments.AFK);
        this.punishmentAdapters.add(new PunishmentAdapter("2d", PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("4d", PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentType.BAN, 3));
        this.punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentType.BAN, 4));
        this.punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentType.BAN, 5));
        this.punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentType.BAN, 6));
        this.punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentType.BAN, 7));
    }
}
