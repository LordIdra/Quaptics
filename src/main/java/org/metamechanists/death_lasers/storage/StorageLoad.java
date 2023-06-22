package org.metamechanists.death_lasers.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.storage.v1.Storage;
import org.metamechanists.death_lasers.utils.Language;

import java.io.File;
import java.util.logging.Logger;

public class StorageLoad {
    public static final String FILE_NAME = "data_DO_NOT_EDIT.yml";
    public static final Logger logger = DEATH_LASERS.getInstance().getLogger();

    public static void load() {
        final File file = new File(DEATH_LASERS.getInstance().getDataFolder(), FILE_NAME);
        if (!file.exists()) {
            logger.warning(Language.getLanguageEntry("load.file-not-found"));
            return;
        }

        final FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        final int version = data.getInt("version");

        switch (version) {
            case 1 -> Storage.read(data);
            default -> {
                logger.severe(Language.getLanguageEntry("load.invalid-config-version", version));
                return;
            }
        }

        logger.info(Language.getLanguageEntry("load.success"));
    }
}
