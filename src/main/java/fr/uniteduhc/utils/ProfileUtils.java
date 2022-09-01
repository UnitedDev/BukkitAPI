package fr.uniteduhc.utils;

import fr.uniteduhc.common.CommonProvider;
import fr.uniteduhc.common.cache.data.ProfileData;

import java.util.Map;
import java.util.UUID;

public class ProfileUtils {
    public static UUID fromString(String string) {

        for (Map.Entry<UUID, ProfileData> entry : CommonProvider.getInstance().players.entrySet()) {
            UUID uuid = entry.getKey();
            ProfileData profileData = entry.getValue();
            if (profileData.getDisplayName().equalsIgnoreCase(string)) {
                return uuid;
            }
        }

        return null;
    }

    public static ProfileData getProfile(String string) {

        for (Map.Entry<UUID, ProfileData> entry : CommonProvider.getInstance().players.entrySet()) {
            ProfileData profileData = entry.getValue();
            if (profileData.getDisplayName().equalsIgnoreCase(string)) {
                return profileData;
            }
        }

        return null;
    }
}
