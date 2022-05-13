package fr.kohei.punishment;

import fr.kohei.common.cache.PunishmentData;
import fr.kohei.utils.TimeUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Punishment adapter mainly used for the
 * punishment menu
 *
 * @author Rhodless
 */
@Getter
@RequiredArgsConstructor
public abstract class AbstractPunishment {

    private final Punishments punishment;
    protected List<PunishmentAdapter> punishmentAdapters = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public static class PunishmentAdapter {
        private final long duration;
        private final PunishmentData.PunishmentType type;
        private final int number;

        public PunishmentAdapter(String duration, PunishmentData.PunishmentType type, int number) {
            this.duration = TimeUtil.getDuration(duration);
            this.type = type;
            this.number = number;
        }
    }

}
