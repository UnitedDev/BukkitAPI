package fr.kohei.punishment.impl.gameplay;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class DevoIndicePunishment extends AbstractPunishment {

    public DevoIndicePunishment() {
        super(Punishments.SPOIL_ITEM);
        this.punishmentAdapters.add(new PunishmentAdapter("3d", PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentType.BAN, 3));
        this.punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentType.BAN, 4));
        this.punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentType.BAN, 5));
    }
}
