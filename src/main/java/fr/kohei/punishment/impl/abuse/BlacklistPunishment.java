package fr.kohei.punishment.impl.abuse;

import fr.kohei.common.cache.data.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

/**
 * @author Rhodless
 */
public class BlacklistPunishment extends AbstractPunishment {

    public BlacklistPunishment() {
        super(Punishments.BLACKLIST);
        this.punishmentAdapters.add(new PunishmentAdapter(-1, PunishmentData.PunishmentType.BLACKLIST, 1));
    }

}
