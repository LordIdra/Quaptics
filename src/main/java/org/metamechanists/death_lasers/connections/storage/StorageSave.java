package org.metamechanists.death_lasers.connections.storage;

import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.SerializationUtils;

import java.io.File;
import java.io.IOException;

public class StorageSave extends BukkitRunnable {
    public static final String FILE_NAME = "data_DO_NOT_EDIT.yml";

    public void run() {
        final int MAX_ATTEMPTS = 10;
        final FileConfiguration data = new YamlConfiguration();

        data.set("groups", SerializationUtils.serializeMap(ConnectionPointStorage.getGroups(),
                "location", "connectionGroup"));

        data.set("groupIdsFromPointLocations", SerializationUtils.serializeMap(ConnectionPointStorage.getGroupIdsFromPointLocations(),
                "groupLocation", "pointLocation"));

        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try {
                data.save(new File(DEATH_LASERS.getInstance().getDataFolder(), FILE_NAME));
                break;
            } catch (IOException e) {
                DEATH_LASERS.getInstance().getLogger().severe(Language.getLanguageEntry("save.failed-retrying"));
            }

            if (attempt == 10) {
                DEATH_LASERS.getInstance().getLogger().severe(Language.getLanguageEntry("save.failed-you-are-screwed"));
                return;
            }
        }

        DEATH_LASERS.getInstance().getLogger().warning(ChatColors.color(Language.getLanguageEntry("save.success")));
    }
}
