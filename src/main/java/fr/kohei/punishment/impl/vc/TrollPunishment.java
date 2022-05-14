package fr.kohei.punishment.impl.vc;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class TrollPunishment extends AbstractPunishment {
    public TrollPunishment() {
        super(Punishments.TROLL);
        punishmentAdapters.add(new PunishmentAdapter("1d", PunishmentData.PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter("3d", PunishmentData.PunishmentType.BAN, 2));
        punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentData.PunishmentType.BAN, 3));
        punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentData.PunishmentType.BAN, 4));
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentData.PunishmentType.BAN, 5));
        punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentData.PunishmentType.BAN, 6));
        punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentData.PunishmentType.BAN, 7));
    }
}
