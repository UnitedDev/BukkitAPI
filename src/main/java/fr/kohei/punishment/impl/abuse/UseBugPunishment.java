package fr.kohei.punishment.impl.abuse;

import fr.kohei.common.cache.data.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class UseBugPunishment extends AbstractPunishment {

    public UseBugPunishment() {
        super(Punishments.USE_BUG);
        this.punishmentAdapters.add(new PunishmentAdapter("4d", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter("20d", PunishmentData.PunishmentType.BAN, 3));
        this.punishmentAdapters.add(new PunishmentAdapter("40d", PunishmentData.PunishmentType.BAN, 4));
        this.punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentData.PunishmentType.BAN, 5));
        this.punishmentAdapters.add(new PunishmentAdapter("80d", PunishmentData.PunishmentType.BAN, 6));
    }
}
