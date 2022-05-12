package fr.kohei.command.param.defaults;

import fr.kohei.utils.ChatUtil;
import fr.kohei.command.param.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DoubleParameterType implements ParameterType<Double> {

    public Double transform(CommandSender sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(ChatUtil.prefix(ChatColor.RED + source + " n'est pas un nombre valide."));
            return (null);
        }

        try {
            double parsed = Double.parseDouble(source);

            if (Double.isNaN(parsed) || !Double.isFinite(parsed)) {
                sender.sendMessage(ChatUtil.prefix("&cCe nombre n'est pas valide"));
                return (null);
            }

            return (parsed);
        } catch (NumberFormatException exception) {
            sender.sendMessage(ChatUtil.prefix("&cCe nombre n'est pas valide"));
            return (null);
        }
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        return (new ArrayList<>());
    }

}