package fr.uniteduhc.punishment.impl.cheat;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

/**
 * @author Rhodless
 */
public class XRayPunishment extends AbstractPunishment {
    public XRayPunishment() {
        super(Punishments.XRAY);
        this.punishmentAdapters.add(new PunishmentAdapter("3M", PunishmentData.PunishmentType.BAN, 1));
        this.punishmentAdapters.add(new PunishmentAdapter("9M", PunishmentData.PunishmentType.BAN, 2));
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BAN, 3));
    }
}
