package fr.uniteduhc.utils;

import fr.uniteduhc.BukkitAPI;
import fr.uniteduhc.command.CommandHandler;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static fr.uniteduhc.command.CommandHandler.getCommandMap;

public class CommandsRemover {
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static void deleteCommands() {
        Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        ConcurrentHashMap<String, Command> commands = new ConcurrentHashMap<>((HashMap<String, Command>) field.get(getCommandMap()));

        commands.keySet().forEach(s -> {
            org.bukkit.command.Command command = commands.get(s);
            if (command.getName().equalsIgnoreCase("mumble")) return;
            if (command.getName().equalsIgnoreCase("timings")) return;
            if (command.getName().equalsIgnoreCase("joinqueue")) return;
            if (command.getName().equalsIgnoreCase("leavequeue")) return;
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

    public static class SimpleCommand extends org.bukkit.command.Command {
        public SimpleCommand(String name) {
            super(name);
        }

        @Override
        public boolean execute(CommandSender commandSender, String s, String[] strings) {
            return false;
        }
    }
}
