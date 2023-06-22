package org.metamechanists.death_lasers.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.storage.versions.StorageV1;
import org.metamechanists.death_lasers.utils.Language;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Storage {
    public static final String FILE_NAME = "data_DO_NOT_EDIT.yml";
    public static final Logger logger = DEATH_LASERS.getInstance().getLogger();

    public static void save() {
        final FileConfiguration data = new YamlConfiguration();
        final File file = new File(DEATH_LASERS.getInstance().getDataFolder(), FILE_NAME);

        data.set("version", StorageV1.VERSION);
        StorageV1.write(data);

        try {
            data.save(file);
        } catch (IOException e) {
            logger.warning(Language.getLanguageEntry("save.failed"));
            throw new RuntimeException(e);
        }

        logger.info(Language.getLanguageEntry("save.success"));
    }

    public static void load() {
        final File file = new File(DEATH_LASERS.getInstance().getDataFolder(), FILE_NAME);
        if (!file.exists()) {
            logger.warning(Language.getLanguageEntry("load.file-not-found"));
            return;
        }

        final FileConfiguration data = YamlConfiguration.loadConfiguration(file);
        final int version = data.getInt("version");

        switch (version) {
            case 1 -> StorageV1.read(data);
            default -> {
                logger.severe(Language.getLanguageEntry("load.invalid-config-version", version));
                return;
            }
        }

        logger.info(Language.getLanguageEntry("load.success"));
    }
}
