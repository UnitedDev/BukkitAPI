package fr.kohei.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import fr.kohei.BukkitAPI;
import fr.kohei.command.param.Param;
import fr.kohei.command.param.ParameterData;
import fr.kohei.command.param.ParameterType;
import fr.kohei.command.param.defaults.*;
import fr.kohei.common.cache.ProfileData;
import fr.kohei.tasks.TabListTask;
import fr.kohei.utils.ChatUtil;
import fr.kohei.utils.Reflection;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandHandler implements Listener {

    private final JavaPlugin plugin;

    @Getter
    public static final List<CommandData> commands = new ArrayList<>();
    static final Map<Class<?>, ParameterType<?>> parameterTypes = new HashMap<>();
    static boolean initiated = false;

    public CommandHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Register a custom parameter adapter.
     *
     * @param transforms    The class this parameter type will return (IE Profile.class, Player.class, etc.)
     * @param parameterType The ParameterType object which will perform the transformation.
     */
    public static void registerParameterType(Class<?> transforms, ParameterType<?> parameterType) {
        parameterTypes.put(transforms, parameterType);
    }

    /**
     * Registers a single class with the command handler.
     *
     * @param registeredClass The class to scan/register.
     */
    public void registerClass(Class<?> registeredClass) {

        for (Method method : registeredClass.getMethods()) {
            if (method.getAnnotation(Command.class) != null) {
                registerMethod(method);
            }
        }
    }

    /**
     * Registers a single method with the command handler.
     *
     * @param method The method to register (if applicable)
     */
    private static void registerMethod(Method method) {
        Command commandAnnotation = method.getAnnotation(Command.class);
        List<ParameterData> parameterData = new ArrayList<>();

        // Offset of 1 here for the sender parameter.
        for (int parameterIndex = 1; parameterIndex < method.getParameterTypes().length; parameterIndex++) {
            Param paramAnnotation = null;

            for (Annotation annotation : method.getParameterAnnotations()[parameterIndex]) {
                if (annotation instanceof Param) {
                    paramAnnotation = (Param) annotation;
                    break;
                }
            }

            if (paramAnnotation != null) {
                parameterData.add(new ParameterData(paramAnnotation, method.getParameterTypes()[parameterIndex]));
            } else {
                return;
            }
        }

        commands.add(new CommandData(commandAnnotation, parameterData, method, method.getParameterTypes()[0].isAssignableFrom(Player.class)));

        // We sort here so to ensure that our commands are matched properly.
        // The way we process commands (see onCommandPreProcess) requires the commands list
        // be sorted by the length of the commands.
        // It's easier (and more efficient) to do that sort here than on command.
        commands.sort((o1, o2) -> (o2.getName().length() - o1.getName().length()));
    }

    /**
     * @return the full command line input of a player before running or tab completing a Core command
     */
    public static String[] getParameters(Player player) {
        return CommandMap.parameters.get(player.getUniqueId());
    }

    /**
     * Process a command (permission checks, argument validation, etc.)
     *
     * @param sender  The CommandSender executing this command.
     *                It should be noted that any non-player sender is treated with full permissions.
     * @param command The command to process (without a prepended '/')
     * @return The Command executed
     */
    public CommandData evalCommand(final CommandSender sender, String command) {
        String[] args = new String[]{};
        CommandData found = null;

        CommandLoop:
        for (CommandData commandData : commands) {
            for (String alias : commandData.getNames()) {
                String messageString = command.toLowerCase() + " "; // Add a space.
                String aliasString = alias.toLowerCase() + " "; // Add a space.
                // The space is added so '/pluginslol' doesn't match '/plugins'

                if (messageString.startsWith(aliasString)) {
                    found = commandData;

                    if (messageString.length() > aliasString.length()) {
                        if (found.getParameters().size() == 0) {
                            continue;
                        }
                    }

                    // If there's 'space' after the command, parse args.
                    // The +1 is there to account for a space after the command if there's parameters
                    if (command.length() > alias.length() + 1) {
                        // See above as to... why this works.
                        args = (command.substring(alias.length() + 1)).split(" ");
                    }

                    // We break to the command loop as we have 2 for loops here.
                    break CommandLoop;
                }
            }
        }

        if (found == null) {
            return (null);
        }

        if (!(sender instanceof Player) && !found.isConsoleAllowed()) {
            sender.sendMessage(ChatColor.RED + "This command does not support execution from the console.");
            return (found);
        }

        if (!found.canAccess(sender)) {
            sender.sendMessage(ChatUtil.prefix("&cVous n'avez pas la permission d'executer cette commande."));
            return (found);
        }

        if (found.isAsync()) {
            final CommandData foundClone = found;
            final String[] argsClone = args;

            new BukkitRunnable() {
                public void run() {
                    foundClone.execute(sender, argsClone);
                }

            }.runTaskAsynchronously(plugin);
        } else {
            found.execute(sender, args);
        }

        return (found);
    }

    /**
     * Transforms a parameter.
     *
     * @param sender      The CommandSender executing the command (or whoever we should transform 'for')
     * @param parameter   The String to transform ('' if none)
     * @param transformTo The class we should use to fetch our ParameterType (which we delegate transforming down to)
     * @return The Object that we've transformed the parameter to.
     */
    static Object transformParameter(CommandSender sender, String parameter, Class<?> transformTo) {
        // Special-case Strings as they never need transforming.
        if (transformTo.equals(String.class)) {
            return (parameter);
        }

        // This will throw a NullPointerException if there's no registered
        // parameter type, but that's fine -- as that's what we'd do anyway.
        return (parameterTypes.get(transformTo).transform(sender, parameter));
    }

    /**
     * Tab completes a parameter.
     *
     * @param sender           The Player tab completing the command (not CommandSender as tab completion is for players only)
     * @param parameter        The last thing the player typed in their chat box before hitting tab ('' if none)
     * @param transformTo      The class we should use to fetch our ParameterType (which we delegate tab completing down to)
     * @param tabCompleteFlags The list of custom flags to use when tab completing this parameter.
     * @return A List<String> of available tab completions. (empty if none)
     */
    static List<String> tabCompleteParameter(Player sender, String parameter, Class<?> transformTo, String[] tabCompleteFlags) {
        if (!parameterTypes.containsKey(transformTo)) {
            return (new ArrayList<>());
        }

        return (parameterTypes.get(transformTo).tabComplete(sender, ImmutableSet.copyOf(tabCompleteFlags), parameter));
    }

    /**
     * Initiates the command handler.
     * This can only be called once, and is called automatically when Core enables.
     */
    public void hook() {
        // Only allow the CoreCommandHandler to be initiated once.
        // Note the '!' in the .checkState call.
        Preconditions.checkState(!initiated);
        initiated = true;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        registerParameterType(boolean.class, new BooleanParameterType());
        registerParameterType(float.class, new FloatParameterType());
        registerParameterType(double.class, new DoubleParameterType());
        registerParameterType(int.class, new IntegerParameterType());
        registerParameterType(OfflinePlayer.class, new OfflinePlayerParameterType());
        registerParameterType(Player.class, new PlayerParameterType());
        registerParameterType(String.class, new StringParameterType());

        // Run this on a delay so everything is registered.
        // Not really needed, but it's nice to play it safe.
        new BukkitRunnable() {

            public void run() {
                try {
                    // Command map field (we have to use reflection to get this)
                    Field commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);

                    Object oldCommandMap = commandMapField.get(plugin.getServer());
                    CommandMap newCommandMap = new CommandMap(plugin.getServer());

                    // Start copying the knownCommands field over
                    // (so any commands registered before we hook in are kept)
                    Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                    knownCommandsField.setAccessible(true);

                    // The knownCommands field is final,
                    // so to be able to set it in the new command map we have to remove it.
                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(knownCommandsField, knownCommandsField.getModifiers() & ~Modifier.FINAL);

                    knownCommandsField.set(newCommandMap, knownCommandsField.get(oldCommandMap));
                    // End coping the knownCommands field over

                    commandMapField.set(plugin.getServer(), newCommandMap);
                } catch (Exception e) {
                    // Shouldn't happen, so we can just
                    // printout the exception (and do nothing else)
                    e.printStackTrace();
                }
            }

        }.runTaskLater(plugin, 5L);

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    // Allow command cancellation.
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {

        // The substring is to chop off the '/' that Bukkit gives us here
        String command = event.getMessage().substring(1);

        CommandMap.parameters.put(event.getPlayer().getUniqueId(), command.split(" "));

        if (evalCommand(event.getPlayer(), command) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onConsoleCommand(ServerCommandEvent event) {
        if (evalCommand(event.getSender(), event.getCommand()) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if(event.getReason().equalsIgnoreCase("disconnect.spam")) event.setCancelled(true);
        if(event.getReason().contains("Flying is not enabled")) event.setCancelled(true);
    }

    public static class SimpleCommand extends org.bukkit.command.Command {
        public SimpleCommand(String name) {
            super(name);
        }

        @Override
        public boolean execute(CommandSender commandSender, String s, String[] strings) {
            return false;
        }
    }

    @SneakyThrows
    public static SimpleCommandMap getCommandMap() {
        final Class<?> craftServerClass = Reflection.getOBCClass("CraftServer");
        assert craftServerClass != null;
        final Method getCommandMapMethod = craftServerClass.getMethod("getCommandMap");
        return (SimpleCommandMap) getCommandMapMethod.invoke(craftServerClass.cast(Bukkit.getServer()), new Object[0]);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static void deleteCommands() {
        Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        ConcurrentHashMap<String, org.bukkit.command.Command> commands = new ConcurrentHashMap<>((HashMap<String, org.bukkit.command.Command>) field.get(getCommandMap()));

        commands.keySet().forEach(s -> {
            org.bukkit.command.Command command = commands.get(s);
            if(command.getName().equalsIgnoreCase("mumble")) return;
            command.unregister(getCommandMap());
            commands.remove(s);
        });

        field.set(getCommandMap(), commands);
        BukkitAPI.getCommandHandler().hook();

        CommandHandler.getCommands().forEach(commandData ->
            getCommandMap().register("", new SimpleCommand(commandData.getName().split(" ")[0]))
        );
        getCommandMap().register("", new SimpleCommand("ignore"));
        getCommandMap().register("", new SimpleCommand("msg"));
        getCommandMap().register("", new SimpleCommand("message"));
        getCommandMap().register("", new SimpleCommand("r"));
        getCommandMap().register("", new SimpleCommand("reply"));
        getCommandMap().register("", new SimpleCommand("host"));
        getCommandMap().register("", new SimpleCommand("coins"));
        getCommandMap().register("", new SimpleCommand("exp"));
        getCommandMap().register("", new SimpleCommand("rank"));
        getCommandMap().register("", new SimpleCommand("maintenance"));
        getCommandMap().register("", new SimpleCommand("grant"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());

        String message = event.getMessage();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if(message.contains(onlinePlayer.getName())) {
                ProfileData targetProfile = BukkitAPI.getCommonAPI().getProfile(onlinePlayer.getUniqueId());
                if(targetProfile.isNotifications()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 0.5f, 0.5f);
                }
                message = message.replace(onlinePlayer.getName(), "§b@" + onlinePlayer.getName() + "§f");
            }
        }

        TextComponent text = new TextComponent("§c⚠ ");
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatUtil.prefix("&cSignaler &l" + player.getName()))}));
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatreport name:" + player.getName() + " message:" + message.replace("§b", "").replace("§f", "")));
        String finalMessage = message;

        Bukkit.getOnlinePlayers().forEach(player1 -> player1.spigot().sendMessage(text, new TextComponent(
                profile.getRank().getTabPrefix().replace("&", "§") + " " + player.getName() + " §8§l» §f" + finalMessage
        )));

        event.setCancelled(true);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        TabListTask.sendTabList(player);

        ProfileData profile = BukkitAPI.getCommonAPI().getProfile(player.getUniqueId());
        if(!profile.getDisplayName().equalsIgnoreCase(player.getDisplayName())) {
            profile.setDisplayName(player.getDisplayName());
            BukkitAPI.getCommonAPI().saveProfile(player.getUniqueId(), profile);
        }

        if(profile.getRank().permissionPower() >= 1000) {
            player.setOp(true);
        }

        if (BukkitAPI.getPunishmentManager().getBan(player.getUniqueId()) != null) {
            event.setKickMessage(BukkitAPI.getPunishmentManager().getKickMessage(BukkitAPI.getPunishmentManager().getBan(player.getUniqueId())));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
        if (BukkitAPI.getPunishmentManager().getBlacklist(player.getUniqueId()) != null) {
            event.setKickMessage(BukkitAPI.getPunishmentManager().getKickMessage(BukkitAPI.getPunishmentManager().getBlacklist(player.getUniqueId())));
            event.setResult(PlayerLoginEvent.Result.KICK_BANNED);
        }
    }
}