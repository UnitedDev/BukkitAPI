package fr.kohei.staff;

import com.lunarclient.bukkitapi.LunarClientAPI;
import fr.kohei.BukkitAPI;
import fr.kohei.common.cache.data.ProfileData;
import fr.kohei.menu.pagination.ConfirmationMenu;
import fr.kohei.staff.menu.AllPlayersMenu;
import fr.kohei.staff.menu.ViewInventoryMenu;
import fr.kohei.staff.task.StaffTask;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Cuboid;
import fr.kohei.utils.Heads;
import fr.kohei.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class StaffManager implements Listener {
    private final List<UUID> freezePlayers;

    public StaffManager() {
        Bukkit.getPluginManager().registerEvents(this, BukkitAPI.getPlugin());

        new StaffTask(BukkitAPI.getPlugin());
        this.freezePlayers = new ArrayList<>();
    }

    public void giveInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.getInventory().setItem(0, new ItemBuilder(Material.INK_SACK, 1, (byte) 10)
                .setName("&8❘ &c&lVanish &f(&aActivé&f) &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(1, new ItemBuilder(Material.EYE_OF_ENDER)
                .setName("&8❘ &c&lTous les joueurs &8(&7Clic-droit&8)").toItemStack());

        player.getInventory().setItem(3, new ItemBuilder(Material.CHEST)
                .setName("&8❘ &c&lSanctions &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(4, new ItemBuilder(Material.MAGMA_CREAM)
                .setName("&8❘ &c&lRandom TP &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(5, new ItemBuilder(Material.DIAMOND_ORE)
                .setName("&8❘ &c&lPoser un Diamant &8(&7Clic-droit&8)").toItemStack());

        player.getInventory().setItem(7, new ItemBuilder(Material.PACKED_ICE)
                .setName("&8❘ &c&lFreeze &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(8, new ItemBuilder(Heads.NEXT_PAGE.toItemStack())
                .setName("&8❘ &c&lProchaine Page &8(&7Clic-droit&8)").toItemStack());
    }

    public void giveSecondInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.getInventory().setItem(0, new ItemBuilder(Heads.PREVIOUS_PAGE.toItemStack())
                .setName("&8❘ &c&lPage Précédente &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(1, new ItemBuilder(Material.TNT)
                .setName("&8❘ &c&lExplosion &8(&7Clic-droit&8)").toItemStack());

        player.getInventory().setItem(3, new ItemBuilder(Material.INK_SACK).setDurability(15)
                .setName("&8❘ &c&lEclair &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(4, new ItemBuilder(Material.TRAPPED_CHEST)
                .setName("&8❘ &c&lVoir l'Inventaire &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(5, new ItemBuilder(Material.NETHER_STAR)
                .setName("&8❘ &c&lDisperser &8(&7Clic-droit&8)").toItemStack());


        player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_COMPARATOR)
                .setName("&8❘ &c&lGamemode &8(&7Clic-droit&8)").toItemStack());
        player.getInventory().setItem(8, new ItemBuilder(Material.INK_SACK).setDurability(1)
                .setName("&8❘ &c&lMode Normal &8(&7Clic-droit&8)").toItemStack());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(event.getPlayer().getUniqueId());

        if (!profile.isStaff()) return;
        if (!item.hasItemMeta()) return;

        event.setCancelled(true);

        if (item.getItemMeta().getDisplayName().contains("Page Précédente")) {
            giveInventory(player);
            return;
        }

        if (item.getItemMeta().getDisplayName().contains("Prochaine Page")) {
            giveSecondInventory(player);
            return;
        }

        if (item.getType() == Material.TNT) {
            player.getWorld().createExplosion(player.getLocation(), 2F);
            player.sendMessage(ChatUtil.prefix("&fVous avez créé une &cexplosion&f."));
        }

        if (item.getType() == Material.INK_SACK && item.getDurability() == 1) {
            new ConfirmationMenu(() -> {
                player.closeInventory();
                player.chat("/mod");
            }, new ItemBuilder(Material.INK_SACK).setDurability(1).setName("&cDésactiver le Staff mod").toItemStack(), null)
                    .openMenu(player);
        }

        if (item.getType() == Material.REDSTONE_COMPARATOR) {
            if (player.getGameMode() == GameMode.CREATIVE) {
                player.sendMessage(ChatUtil.prefix("&fVous êtes désormais en mode &aSurvival&f."));
                player.setGameMode(GameMode.SURVIVAL);
            } else {
                player.sendMessage(ChatUtil.prefix("&fVous êtes désormais en mode &aCréatif&f."));
                player.setGameMode(GameMode.CREATIVE);
            }
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        if (item.getType() == Material.INK_SACK && (item.getDurability() == 8 || item.getDurability() == 10)) {
            if (profile.isVanish()) {
                profile.setVanish(false);
                player.getInventory().setItem(0, new ItemBuilder(Material.INK_SACK, 1, (byte) 8)
                        .setName("&8❘ &c&lVanish &f(&cDésactivé&f) &8(&7Clic-droit&8)").toItemStack());
            } else {
                profile.setVanish(true);
                player.getInventory().setItem(0, new ItemBuilder(Material.INK_SACK, 1, (byte) 10)
                        .setName("&8❘ &c&lVanish &f(&aActivé&f) &8(&7Clic-droit&8)").toItemStack());
            }
            this.updateVanish(player, profile);
            player.sendMessage(ChatUtil.prefix("&fVous avez " + (profile.isVanish() ? "&aactivé" : "&cdesactivé") + " &fvotre vanish."));
            BukkitAPI.getCommonAPI().saveProfile(player.getUniqueId(), profile);
        }

        if (item.getType() == Material.EYE_OF_ENDER) {
            new AllPlayersMenu().openMenu(player);
        }

        if (item.getType() == Material.DIAMOND_ORE) {
            Cuboid cuboid = new Cuboid(player.getLocation().clone().add(1, 1, 1), player.getLocation());
            cuboid.getBlockList().forEach(block -> block.setType(Material.DIAMOND_ORE));

            player.sendMessage(ChatUtil.prefix("&fVous avez posé un minerai de &bdiamant&f."));
        }

        if (item.getType() == Material.MAGMA_CREAM) {
            List<Player> players = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !p.getUniqueId().equals(player.getUniqueId()))
                    .collect(Collectors.toList());
            if (players.size() == 0) {
                player.sendMessage(ChatUtil.prefix("&cIl n'y a pas de joueurs sur le serveur."));
                return;
            }
            Collections.shuffle(players);

            Player target = players.get(0);

            player.teleport(target);
            player.sendMessage(ChatUtil.prefix("&fVous vous êtes téléporté à &c" + target.getName() + "&f."));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) return;

        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null) return;

        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(event.getPlayer().getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);

        Player target = (Player) event.getRightClicked();

        if (item.getType() == Material.CHEST) {
            player.chat("/c " + target.getName());
        }

        if (item.getType() == Material.TRAPPED_CHEST) {
            new ViewInventoryMenu(target).openMenu(player);
        }

        if (item.getType() == Material.PACKED_ICE) {
            if(BukkitAPI.getStaffManager().getFreezePlayers().contains(target.getUniqueId())) {
                player.chat("/unfreeze " + target.getName());
                return;
            }
            player.chat("/freeze " + target.getName());
        }

        if (item.getType() == Material.NETHER_STAR) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("UHC");
            if (plugin == null) {
                player.sendMessage(ChatUtil.prefix("&cVous n'êtes pas dans un uhc."));
                return;
            }

            player.chat("/disperse " + target.getName());
        }

        if (item.getType() == Material.INK_SACK && item.getDurability() == 15) {
            target.getWorld().strikeLightning(target.getLocation());
            player.sendMessage(ChatUtil.prefix("&fVous avez créé un &ceclair&f sur &c" + target.getName() + "&f."));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(true);
        giveInventory(player);
        if (LunarClientAPI.getInstance().isRunningLunarClient(player)) LunarClientAPI.getInstance().giveAllStaffModules(player);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        if (!profile.isStaff()) return;

        event.setCancelled(true);
    }

    private void updateVanish(Player player, ProfileData profile) {
        if (profile.isVanish()) {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.hidePlayer(player));
        } else {
            Bukkit.getOnlinePlayers().forEach(player1 -> player1.showPlayer(player));
        }
    }

}
