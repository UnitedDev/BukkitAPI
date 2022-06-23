package fr.kohei.staff.events;

import fr.kohei.common.cache.data.ProfileData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class StaffDisableEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final ProfileData profile;

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
