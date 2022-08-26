package fr.uniteduhc.punishment.impl.abuse;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

/**
 * @author Rhodless
 */
public class BlacklistPunishment extends AbstractPunishment {

    public BlacklistPunishment() {
        super(Punishments.BLACKLIST);
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BLACKLIST, 1));
    }

}
