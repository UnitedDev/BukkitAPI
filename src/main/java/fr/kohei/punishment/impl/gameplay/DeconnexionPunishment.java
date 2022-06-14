package fr.kohei.punishment.impl.gameplay;

import fr.kohei.common.cache.data.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class DeconnexionPunishment extends AbstractPunishment {

    public DeconnexionPunishment() {
        super(Punishments.DISCONNECT);
        this.punishmentAdapters.add(new PunishmentAdapter("2d", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("4d", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentData.PunishmentType.BAN, 3));
        this.punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentData.PunishmentType.BAN, 4));
        this.punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentData.PunishmentType.BAN, 5));
        this.punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentData.PunishmentType.BAN, 6));
        this.punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentData.PunishmentType.BAN, 7));
    }
}
