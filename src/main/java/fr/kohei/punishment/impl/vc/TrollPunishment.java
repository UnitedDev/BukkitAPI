package fr.kohei.punishment.impl.vc;

import fr.kohei.punishment.AbstractPunishment;
import fr.kohei.punishment.Punishments;

public class TrollPunishment extends AbstractPunishment {
    public TrollPunishment() {
        super(Punishments.TROLL);
        punishmentAdapters.add(new PunishmentAdapter("1d", PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter("3d", PunishmentType.BAN, 2));
        punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentType.BAN, 3));
        punishmentAdapters.add(new PunishmentAdapter("14d", PunishmentType.BAN, 4));
        punishmentAdapters.add(new PunishmentAdapter("30d", PunishmentType.BAN, 5));
        punishmentAdapters.add(new PunishmentAdapter("60d", PunishmentType.BAN, 6));
        punishmentAdapters.add(new PunishmentAdapter("120d", PunishmentType.BAN, 7));
    }
}
