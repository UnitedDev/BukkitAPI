package fr.uniteduhc.punishment.impl.vc;

import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.punishment.AbstractPunishment;
import fr.uniteduhc.punishment.Punishments;

public class DisrespectVCPunishment extends AbstractPunishment {
    public DisrespectVCPunishment() {
        super(Punishments.DISRESPECT_VC);
        punishmentAdapters.add(new PunishmentAdapter("4d", PunishmentData.PunishmentType.BAN, 1));
        punishmentAdapters.add(new PunishmentAdapter("7d", PunishmentData.PunishmentType.BAN, 2));
        punishmentAdapters.add(new PunishmentAdapter("21d", PunishmentData.PunishmentType.BAN, 3));
        punishmentAdapters.add(new PunishmentAdapter("42d", PunishmentData.PunishmentType.BAN, 4));
        punishmentAdapters.add(new PunishmentAdapter("84d", PunishmentData.PunishmentType.BAN, 5));
    }
}
