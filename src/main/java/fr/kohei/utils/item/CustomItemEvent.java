package fr.kohei.utils.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class CustomItemEvent {

    private final Player player;
    private final ItemStack itemStack;
    private final boolean rightClick;

}
