package fr.kohei.utils;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Rhodless
 */
public enum Heads {

    NEXT_PAGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzg2MTg1YjFkNTE5YWRlNTg1ZjE4NGMzNGYzZjNlMjBiYjY0MWRlYjg3OWU4MTM3OGU0ZWFmMjA5Mjg3In19fQ=="),
    PREVIOUS_PAGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWQ3M2NmNjZkMzFiODNjZDhiODY0NGMxNTk1OGMxYjczYzhkOTczMjNiODAxMTcwYzFkODg2NGJiNmE4NDZkIn19fQ=="),
    BALLOON_BLUE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg2OGU2YTVjNGE0NDVkNjBhMzA1MGI1YmVjMWQzN2FmMWIyNTk0Mzc0NWQyZDQ3OTgwMGM4NDM2NDg4MDY1YSJ9fX0="),
    BALLOON_RED("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTJkZDExZGEwNDI1MmY3NmI2OTM0YmMyNjYxMmY1NGYyNjRmMzBlZWQ3NGRmODk5NDEyMDllMTkxYmViYzBhMiJ9fX0="),
    BALLOON_ORANGE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTdmMzgxYTIwYTljNjQwNDI4MDc3MDcwY2M3YmQ5NWQ2ODg1OTJkMTEwNGNjYmNkNzEzNjQ5YTQ5ZTQxZWJmYiJ9fX0="),
    BALLOON_GREEN("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyZGYzMTViNDM1ODNiMTg5NjIzMWI3N2JhZTFhNTA3ZGJkN2U0M2FkODZjMWNmYmUzYjJiOGVmMzQzMGU5ZSJ9fX0="),
    COAL_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzY1OGNjYzczNDU1NTllOTMyMWY0OWVlMWFmNjc1MjJlNzA4ZGNhODkzMmIwYTcyMWNjMzQxMzA3MzFlYjU5OCJ9fX0="),
    CHEST_MINECART("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNlZDM0MjExZmVkNDAxMGE4Yzg1NzI0YTI3ZmE1ZmIyMDVkNjc2ODRiM2RhNTE3YjY4MjEyNzljNmI2NWQzZiJ9fX0="),
    COMMAND_BLOCK("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE5MDUzZDIxNjNkMGY1NjExNDVkMzNhNTEzMTQ1ZDRhYzFmOGE0NThiYWE3OTZiZTM4M2U3NTI1YTA1ZjQ1In19fQ=="),
    SPECIAL_CHEST("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTI2MjlmMjY4MmRjZWUzMGY1ODU1YjFlNTQyN2NjNGJlZTczZDE4YTI3NmZhZmM1MjBkNjkzYjQwY2E4MWIyMiJ9fX0="),
    SASUKE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzZiMmVjNjgxNGQxOWQzY2NlNDkzNzFiNWRlMmMyOGI4MjFlNTU3Nzc2MmM1ZWI2OTg4MzEwOGZlZTk4ZGJhYiJ9fX0="),
    LANTERN("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTYyZGRiZWM4ZmE5ZDMyMjhjOTBjZjg3ZTRlN2JjMGE4ZWUyYmNmOGIxYzc3ODk2ZWI5N2YzODMwYTVkNmUifX19"),
    SOON("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ=="),
    ONE_PUNCH_MAN("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk2Y2Q3N2Q3ZmVlMTQyZDU5MmE0ZGZlNzcwMGQzZmZlZGJkMWRkZmY4YWIxMzliZjU4NzAyNWJmMTQzNzk1ZiJ9fX0="),
    YOUR_NAME("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk3YTQ2YjY1ZWIxMjAwY2I5MGIzNzQwNmU4ZjA1ZTJhN2NlNjY3M2MxZjJjZGY5YWUxM2I3YzBlMzZjNjBlNyJ9fX0="),
    DICE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGYxYWVhMDQ3YmNmYzExNzkzOThhYTQ2MDIwNTM5N2RiMDI0NmEyZmM0N2QwMzVlMGU2ZTk3OTIzZmIzY2IxMiJ9fX0="),
    SETTINGS("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmZmMTczM2JmNGU4ZWU4MTA1ZjYwMmQ0NzY3MjU1ODBkOTM1MTI3ZDk4ZjMwYmQyNjQzYzY2ZWM4MjA0NjE5ZiJ9fX0="),
    LUCKY("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM0OTcwZWE5MWFiMDZlY2U1OWQ0NWZjZTc2MDRkMjU1NDMxZjJlMDNhNzM3YjIyNjA4MmM0Y2NlMWFjYTFjNCJ9fX0="),
    RED_F("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODhiYmMzNTIxM2M2NTEwOWZiZDhlOTA3NzFlNGM2ZGI3ODVhOWJlOGU0MDNkMjllZDhlZDJlM2RjZmIxMjgifX19"),
    YELLOW_I("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdjODljZDY1MmIxMjFjNWJlZDQzMTVlO WVmMDRjNmExM2Y2ZGRmNzYyODgxYmZhYzhmNWU1YzZmZDkwIn19fQ=="),
    LIME_PLUS("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTdjODljZDY1MmIxMjFjNWJlZDQzMTVlOWVmMDRjNmExM2Y2ZGRmNzYyODgxYmZhYzhmNWU1YzZmZDkwIn19fQ=="),
    DEKU("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU3ZmI5MGQ3ZGU4MWI5Y2Y1YjBhZTExZmNmN2QyNmNjZWU1MTE3ZGM2MjBkMDhlMTRlMTQ4Nzk0Nzg4MjUifX19"),
    UHC("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGFiZDcwM2U1YjhjODhkNGIxZmNmYTk0YTkzNmEwZDZhNGY2YWJhNDQ1Njk2NjNkMzM5MWQ0ODgzMjIzYzUifX19");

    private final String base;

    Heads(String base) {
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public ItemStack toItemStack() {
        return new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()).setTexture(getBase()).toItemStack();
    }

}
