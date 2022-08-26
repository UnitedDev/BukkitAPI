package fr.uniteduhc.punishment.impl.gameplay;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

/**
 * @author Rhodless
 */
public class AFKKillPunishment extends AbstractPunishment {

    public AFKKillPunishment() {
        super(Punishments.AFK_KILL);
        this.punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentData.PunishmentType.BAN, 3));
        this.punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentData.PunishmentType.BAN, 4));
        this.punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentData.PunishmentType.BAN, 5));
    }
}