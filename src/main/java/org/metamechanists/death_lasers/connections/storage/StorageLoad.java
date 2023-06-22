package org.metamechanists.death_lasers.connections.storage;

import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.SerializationUtils;

import java.io.File;
import java.util.Map;

public class StorageLoad {
    public static void load() {

        final File file = new File(DEATH_LASERS.getInstance().getDataFolder(), StorageSave.FILE_NAME);
        final FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            DEATH_LASERS.getInstance().getLogger().warning(ChatColors.color(Language.getLanguageEntry("load.no-data-file")));
            return;
        }

        try {
            Map<Location, ConnectionGroup> groups = SerializationUtils.deserializeMap(
                    data.getConfigurationSection("groups").getValues(false),
                    "location", "connectionGroup");

            Map<Location, Location> groupIdsFromPointLocations = SerializationUtils.deserializeMap(
                    data.getConfigurationSection("groupIdsFromPointLocations").getValues(false),
                    "groupLocation", "pointLocation");

            ConnectionPointStorage.setGroups(groups);
            ConnectionPointStorage.setGroupIdsFromPointLocations(groupIdsFromPointLocations);

        } catch (Exception e) {
            DEATH_LASERS.getInstance().getLogger().warning(ChatColors.color(Language.getLanguageEntry("load.failed")));
            e.printStackTrace();
            throw new RuntimeException("Failed to load laser data");
        }

        DEATH_LASERS.getInstance().getLogger().warning(ChatColors.color(Language.getLanguageEntry("load.success")));
    }
}
