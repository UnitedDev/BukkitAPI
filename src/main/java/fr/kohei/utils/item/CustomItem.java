package fr.kohei.utils.item;

import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class CustomItem {

    @Getter
    private static final List<CustomItem> customItems = new ArrayList<>();

    private final Material material;
    private final ItemStack itemStack;
    private final String name;
    private final boolean interactItem;
    private final Consumer<CustomItemEvent> callable;
    
    public CustomItem(Material material, String name) {
        this.material = material;
        this.name = name;
        this.interactItem = true;
        this.callable = null;
        itemStack = null;
        if(!customItems.contains(this)) customItems.add(this);
    }

    public CustomItem(Material material, String name, boolean interactItem) {
        this.material = material;
        this.name = name;
        this.interactItem = interactItem;
        this.callable = null;
        itemStack = null;
        if(!customItems.contains(this)) customItems.add(this);
    }

    public CustomItem(Material material, String name, Consumer<CustomItemEvent> event) {
        this.material = material;
        this.name = name;
        this.interactItem = true;
        this.callable = event;
        itemStack = null;
        if(!customItems.contains(this)) customItems.add(this);
    }

    public CustomItem(ItemStack is, String name, Consumer<CustomItemEvent> event) {
        this.material = null;
        this.name = name;
        this.interactItem = true;
        this.callable = event;
        itemStack = is;
        if(!customItems.contains(this)) customItems.add(this);
    }

    public CustomItem(Material material, String name, boolean interactItem, Consumer<CustomItemEvent> event) {
        this.material = material;
        this.name = name;
        this.interactItem = interactItem;
        this.callable = event;
        itemStack = null;
        if(!customItems.contains(this)) customItems.add(this);
    }

    public ItemStack toItemStack() {
        if(itemStack != null) {
            return new ItemBuilder(itemStack).setName("&8❘ &c&l" + name + " &8(&7Clic-droit&8)").toItemStack();
        }
        if (this.interactItem)
            return new ItemBuilder(material).setName("&8❘ &c&l" + name + " &8(&7Clic-droit&8)").toItemStack();
        else
            return new ItemBuilder(material).setName("&8❘ &c&l" + name).toItemStack();
    }

    public static CustomItem getCustomItem(ItemStack itemStack) {
        return getCustomItems().stream().filter(c -> itemStack.isSimilar(c.toItemStack())).findFirst().orElse(null);
    }
}
