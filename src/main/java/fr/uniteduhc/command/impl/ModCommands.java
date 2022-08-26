package fr.uniteduhc.command.impl;

import com.lunarclient.bukkitapi.LunarClientAPI;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.command.Command;
import fr.uniteduhc.command.param.Param;
import fr.uniteduhc.common.CommonProvider;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.common.cache.data.PunishmentData;
import fr.uniteduhc.common.utils.gson.GsonProvider;
import fr.uniteduhc.manager.grant.GrantsMenu;
import fr.uniteduhc.messaging.packet.PunishmentAskPacket;
import fr.uniteduhc.messaging.packet.PunishmentPacket;
import fr.uniteduhc.punishment.menu.*;
import fr.uniteduhc.staff.events.StaffDisableEvent;
import fr.uniteduhc.staff.menu.FreezeMenu;
import fr.uniteduhc.utils.ChatUtil;
import fr.uniteduhc.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ModCommands {

    @Command(names = {"gamemode", "gm"}, power = 39)
    public static void changeGamemode(Player player, @Param(name = "gamemode") String gamemode) {

        GameMode newGamemode = getGamemode(gamemode);
        if (newGamemode == null) {
            player.sendMessage(ChatUtil.prefix("&cVous devez rentrer un gamemode valide."));
            return;
        }

        player.setGameMode(newGamemode);
        player.sendMessage(ChatUtil.prefix("&fVous avez changé votre gamemode en &a" + niceFormat(newGamemode)));
    }

    @Command(names = {"tp"}, power = 39)
    public static void teleport(Player player, @Param(name = "player") Player target) {
        player.teleport(target.getLocation());
        player.sendMessage(ChatUtil.prefix("&fVous vous êtes téléporté à &a" + target.getName()));
    }

    @Command(names = {"tphere"}, power = 39)
    public static void teleportHere(Player player, @Param(name = "player") Player target) {

        target.teleport(player.getLocation());
        player.sendMessage(ChatUtil.prefix("&fVous avez téléporté &a" + target.getName() + " &fà vous"));
    }

    @Command(names = "forceban", power = 39)
    public static void forceBan(Player player, @Param(name = "data", wildcard = true) String data) {
        UUID uuid = UUID.fromString(data);
        String str = BukkitAPI.getPunishmentManager().getGsonAskPunishment().get(uuid);
        PunishmentData punishmentData = GsonProvider.GSON.fromJson(str, PunishmentData.class);

        BukkitAPI.getPunishmentManager().punish(punishmentData, null);
    }

    @Command(names = "ss", power = 39)
    public static void ss(Player player, @Param(name = "player") String target) {
        new PunishmentCategoryMenu(PlayerCommands.getProfile(target), null).openMenu(player);
    }

    @Command(names = {"c", "casier"}, power = 39)
    public static void chech(Player player, @Param(name = "player") String target) {
        new HistoryMenu(PlayerCommands.getProfile(target), null).openMenu(player);
    }

    @Command(names = "declineban", power = 40)
    public static void decline(Player player, @Param(name = "data") String data) {
        UUID uuid = UUID.fromString(data);
        PunishmentAskPacket packet = GsonProvider.GSON.fromJson(
                BukkitAPI.getPunishmentManager().getGsonAskPunishment().get(uuid), PunishmentAskPacket.class
        );

        BukkitAPI.getPunishmentManager().getGsonAskPunishment().remove(uuid);
        player.sendMessage(ChatUtil.prefix("&fVous avez &crefusé &fle bannissement de &7" +
                BukkitAPI.getCommonAPI().getProfile(packet.getExecutor()).getDisplayName()));
    }

    @Command(names = "acceptban", power = 40)
    public static void acceptBan(Player player, @Param(name = "data") String data) {
        UUID uuid = UUID.fromString(data);
        PunishmentAskPacket packet = GsonProvider.GSON.fromJson(
                BukkitAPI.getPunishmentManager().getGsonAskPunishment().get(uuid), PunishmentAskPacket.class
        );

        PunishmentData.PunishmentType type = packet.getPunishmentData().getPunishmentType();
        if (BukkitAPI.getPunishmentManager().getBan(packet.getPunishmentData().getPunished()) != null && type == PunishmentData.PunishmentType.BAN) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un ban actif."));
            return;
        }

        if (BukkitAPI.getPunishmentManager().getBlacklist(packet.getPunishmentData().getPunished()) != null && type == PunishmentData.PunishmentType.BLACKLIST) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur est déjà blacklist."));
            return;
        }

        if (BukkitAPI.getPunishmentManager().getMute(packet.getPunishmentData().getPunished()) != null && type == PunishmentData.PunishmentType.MUTE) {
            player.sendMessage(ChatUtil.prefix("&cCe joueur a déjà un mute actif."));
            return;
        }

        if (BukkitAPI.getCommonAPI().getProfile(player.getUniqueId()).getRank().permissionPower() <= 40 &&
                packet.getPunishmentData().getPunishmentType() == PunishmentData.PunishmentType.BLACKLIST) {
            player.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission de blacklist un joueur."));
            return;
        }

        BukkitAPI.getPunishmentManager().getGsonAskPunishment().remove(uuid);
        BukkitAPI.getPunishmentManager().punish(packet.getPunishmentData(), player.getName());
    }

    @Command(names = "ban", power = 40)
    public static void ban(Player sender, @Param(name = "player") String target, @Param(name = "raison", wildcard = true) String reason) {
        UUID uuid = PlayerCommands.fromString(target);

        if (BukkitAPI.getPunishmentManager().getBan(uuid) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà banni."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.BAN,
                uuid,
                sender.getUniqueId(),
                reason,
                -1L
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "tempban", power = 40)
    public static void ban(Player sender, @Param(name = "player") String target, @Param(name = "durée") String duration, @Param(name = "raison", wildcard = true) String reason) {
        UUID uuid = PlayerCommands.fromString(target);
        if (BukkitAPI.getPunishmentManager().getBan(uuid) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà banni."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.BAN,
                uuid,
                sender.getUniqueId(),
                reason,
                TimeUtil.getDuration(duration)
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "reports", power = 39)
    public static void reports(Player sender, @Param(name = "player", defaultValue = "rien") String target) {
        if (target.equalsIgnoreCase("rien"))
            new ReportsMenu(null).openMenu(sender);
        else
            new ReportsMenu(PlayerCommands.getProfile(target)).openMenu(sender);
    }

    @Command(names = "warns", power = 25)
    public static void warns(Player sender, @Param(name = "player") String target) {
        new WarnsMenu(PlayerCommands.getProfile(target)).openMenu(sender);
    }

    @Command(names = "grants", power = 41)
    public static void grants(Player sender, @Param(name = "player") String target) {
        ProfileData targetProfile = PlayerCommands.getProfile(target);
        new GrantsMenu(targetProfile).openMenu(sender);
    }

    @Command(names = {"freeze"}, power = 40)
    public static void freeze(Player sender, @Param(name = "player") Player target) {
        if (BukkitAPI.getStaffManager().getFreezePlayers().contains(target.getUniqueId())) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà freeze."));
            return;
        }

        new FreezeMenu().openMenu(target);
        sender.sendMessage(ChatUtil.prefix("&fVous avez freeze &c" + target.getName()));
        BukkitAPI.getStaffManager().getFreezePlayers().add(target.getUniqueId());
    }

    @Command(names = {"unfreeze"}, power = 40)
    public static void unfreeze(Player sender, @Param(name = "player") Player target) {
        if (!BukkitAPI.getStaffManager().getFreezePlayers().contains(target.getUniqueId())) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas freeze."));
            return;
        }

        BukkitAPI.getStaffManager().getFreezePlayers().remove(target.getUniqueId());
        target.closeInventory();
        target.sendMessage(ChatUtil.prefix("&cVous avez été unfreeze."));
        sender.sendMessage(ChatUtil.prefix("&fVous avez unfreeze &a" + target.getName()));
    }

    @Command(names = "warn", power = 39)
    public static void warn(Player sender, @Param(name = "player") Player target, @Param(name = "raison", wildcard = true, defaultValue = "rien") String data) {
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(sender.getUniqueId());
        ProfileData targetProfile = BukkitAPI.getCommonAPI().getProfile(target.getUniqueId());

        new PunishWarnMenu(target).openMenu(sender);
    }

    @Command(names = {"mod", "staff"}, power = 39)
    public static void staff(Player sender) {
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(sender.getUniqueId());

        ServiceInfoSnapshot server = BukkitAPI.getFactory(Bukkit.getPort());
        if (profile.isStaff()) {
            sender.sendMessage(ChatUtil.prefix("&cVous avez désactivé votre staff mod."));
            if (!server.getName().contains("Lobby")) {
                IPlayerManager manager = CloudNetDriver.getInstance().getServicesRegistry().getFirstService(IPlayerManager.class);
                manager.getPlayerExecutor(sender.getUniqueId()).connectToFallback();
            } else {
                Bukkit.getOnlinePlayers().forEach(player -> player.showPlayer(sender));
                sender.getInventory().clear();
                sender.getInventory().setArmorContents(null);
            }
            Bukkit.getPluginManager().callEvent(new StaffDisableEvent(sender, profile));
            LunarClientAPI.getInstance().disableAllStaffModules(sender);
            Bukkit.getOnlinePlayers().forEach(player -> LunarClientAPI.getInstance().resetNametag(sender, player));

            profile.setStaff(false);
            profile.setVanish(false);
            BukkitAPI.getCommonAPI().saveProfile(sender.getUniqueId(), profile);
            return;
        }
        if (!server.getName().contains("Lobby")) {
            sender.sendMessage(ChatUtil.prefix("&cVous devez être sur un lobby pour activer le staff mod."));
            return;
        }

        sender.setAllowFlight(true);
        BukkitAPI.getStaffManager().giveInventory(sender.getPlayer());
        sender.setGameMode(GameMode.SURVIVAL);
        Bukkit.getOnlinePlayers().forEach(player -> player.hidePlayer(sender));
        profile.setStaff(true);
        profile.setVanish(true);
        BukkitAPI.getCommonAPI().saveProfile(sender.getUniqueId(), profile);
        sender.sendMessage(ChatUtil.prefix("&fVous avez &aactivé &fvotre staff mod."));

        Bukkit.getOnlinePlayers().forEach(target -> {
            if (LunarClientAPI.getInstance().isRunningLunarClient(target))
                LunarClientAPI.getInstance().overrideNametag(sender, Arrays.asList(
                        ChatUtil.translate("&f(&cStaff Mod&f)"),
                        ChatUtil.translate(profile.getRank().getChatPrefix() + " " + sender.getName())
                ), target);
        });
    }

    @Command(names = "unban", power = 40)
    public static void unban(Player sender, @Param(name = "player") String target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getBan(PlayerCommands.fromString(target)) == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas banni."));
            return;
        }

        PunishmentData punishment = BukkitAPI.getPunishmentManager().getBan(PlayerCommands.fromString(target));

        punishment.getEdits().add(new PunishmentData.PunishmentEdit(punishment.getDuration(), 1000, reason, sender.getUniqueId()));
        punishment.setDuration(1000);

        BukkitAPI.getCommonAPI().updatePunishment(punishment);
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentPacket(punishment, null, false));

        String executorDisplay = BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getRank().getTabPrefix().substring(0, 2) +
                BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getDisplayName();

        sender.sendMessage(ChatUtil.prefix("&fVous avez débanni " + executorDisplay));
    }

    @Command(names = "stop", power = 50)
    public static void stop(Player player) {
        Bukkit.getServer().shutdown();
    }

    @Command(names = "mute", power = 39)
    public static void mute(Player sender, @Param(name = "player") String target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getMute(PlayerCommands.fromString(target)) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà mute."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.MUTE,
                PlayerCommands.fromString(target),
                sender.getUniqueId(),
                reason,
                -1L
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "tempmute", power = 39)
    public static void mute(Player sender, @Param(name = "player") String target, @Param(name = "durée") String duration, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getMute(PlayerCommands.fromString(target)) != null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur est déjà mute."));
            return;
        }

        PunishmentData data = new PunishmentData(
                PunishmentData.PunishmentType.MUTE,
                PlayerCommands.fromString(target),
                sender.getUniqueId(),
                reason,
                TimeUtil.getDuration(duration)
        );

        BukkitAPI.getPunishmentManager().attemptPunishment(sender, data);
    }

    @Command(names = "unmute", power = 40)
    public static void unmute(Player sender, @Param(name = "player") String target, @Param(name = "raison", wildcard = true) String reason) {
        if (BukkitAPI.getPunishmentManager().getMute(PlayerCommands.fromString(target)) == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'est pas mute."));
            return;
        }

        PunishmentData punishment = BukkitAPI.getPunishmentManager().getMute(PlayerCommands.fromString(target));

        punishment.getEdits().add(new PunishmentData.PunishmentEdit(punishment.getDuration(), 1000, reason, sender.getUniqueId()));
        punishment.setDuration(1000);

        BukkitAPI.getCommonAPI().updatePunishment(punishment);
        BukkitAPI.getCommonAPI().getMessaging().sendPacket(new PunishmentPacket(punishment, null, false));

        String executorDisplay = BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getRank().getTabPrefix().substring(0, 2) +
                BukkitAPI.getCommonAPI().getProfile(punishment.getPunished()).getDisplayName();

        sender.sendMessage(ChatUtil.prefix("&fVous avez unmute " + executorDisplay));
    }

    @Command(names = {"kick"}, power = 39)
    public static void unmute(Player player, @Param(name = "player") Player target, @Param(name = "raison") String reason) {

        target.kickPlayer(ChatUtil.prefix(
                "&cVous avez été kick" + "\n"
                        + " " + "\n"
                        + "&fVous avez été &6kick &fpar &6" + player.getName() + " &fpour &6" + reason
        ));

        player.sendMessage(ChatUtil.prefix("&fVous avez kick &c" + target.getName() + " &fpour &c " + reason));

    }

    @Command(names = {"stafflist", "sl"})
    public static void staffList(Player player) {
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
        player.sendMessage(ChatUtil.translate("&8┃ &6&lStaff List"));
        List<ProfileData> profiles = BukkitAPI.getCommonAPI().getServerCache().getPlayers().keySet()
                .stream()
                .map(uuid -> BukkitAPI.getCommonAPI().getProfile(uuid))
                .filter(profileData -> profileData.getRank().getPermissionPower() >= 25)
                .sorted(Comparator.comparingInt(o -> ((ProfileData) o).getRank().getPermissionPower()).reversed())
                .collect(Collectors.toList());

        profiles.forEach(data -> {
            UUID uuid = PlayerCommands.fromString(data.getDisplayName());
            String server = BukkitAPI.getCommonAPI().getServerCache().getPlayers().get(uuid);
            String executorDisplay = data.getRank().getTabPrefix() + " " + data.getDisplayName();
            if (!data.isVanish())
                player.sendMessage(ChatUtil.translate("  " + executorDisplay + " &f(&e" + server + "&f)"));
            else if (CommonProvider.getInstance().getProfile(player.getUniqueId()).getRank().getPermissionPower() >= 25) {
                player.sendMessage(ChatUtil.translate("  " + executorDisplay + " &f(&e" + server + "&f) &f(&aVanish&f)"));
            }
        });
        player.sendMessage(ChatUtil.translate("&7&m--------------------------------"));
    }

    public static GameMode getGamemode(String str) {
        if (str.equalsIgnoreCase("creative") || str.equalsIgnoreCase("c") || str.equals("1")) {
            return GameMode.CREATIVE;
        } else if (str.equalsIgnoreCase("survival") || str.equalsIgnoreCase("s") || str.equals("0")) {
            return GameMode.SURVIVAL;
        } else if (str.equalsIgnoreCase("spectator") || str.equalsIgnoreCase("sp") || str.equals("3")) {
            return GameMode.SPECTATOR;
        } else if (str.equalsIgnoreCase("adventure") || str.equalsIgnoreCase("a") || str.equals("2")) {
            return GameMode.ADVENTURE;
        } else {
            return null;
        }
    }

    public static String niceFormat(GameMode gamemode) {
        String name = gamemode.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
