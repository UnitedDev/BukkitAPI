package fr.uniteduhc.command.annotations.defaults;

import fr.uniteduhc.command.annotations.ParameterType;
import fr.uniteduhc.common.CommonProvider;
import fr.uniteduhc.common.cache.data.ProfileData;
import fr.uniteduhc.utils.ChatUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileParameterType implements ParameterType<ProfileData> {
    public ProfileData transform(CommandSender sender, String source) {
        ProfileData profile = null;
        for (Map.Entry<UUID, ProfileData> entry : CommonProvider.getInstance().players.entrySet()) {
            ProfileData profileData = entry.getValue();
            if (profileData.getDisplayName().equalsIgnoreCase(source)) {
                profile = profileData;
            }
        }
        if (profile == null) {
            sender.sendMessage(ChatUtil.prefix("&cCe joueur n'existe pas."));
            return null;
        }

        return profile;
    }


    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        for (String name : CommonProvider.getInstance().getPlayers().values().stream().map(ProfileData::getDisplayName).collect(Collectors.toList())) {
            if (StringUtils.startsWithIgnoreCase(name, source)) {
                completions.add(name);
            }
        }

        return (completions);
    }
}
