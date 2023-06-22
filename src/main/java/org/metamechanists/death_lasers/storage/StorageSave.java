package org.metamechanists.death_lasers.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.storage.v1.Storage;
import org.metamechanists.death_lasers.utils.Language;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class StorageSave extends BukkitRunnable {
    public static final int INTERVAl_TICKS = 3000;
    public static final String FILE_NAME = "data_DO_NOT_EDIT.yml";
    private static final Logger logger = DEATH_LASERS.getInstance().getLogger();

    public void run() {
        final FileConfiguration data = new YamlConfiguration();
        final File file = new File(DEATH_LASERS.getInstance().getDataFolder(), FILE_NAME);

        data.set("version", Storage.VERSION);
        Storage.write(data);

        try {
            data.save(file);
        } catch (IOException e) {
            logger.warning(Language.getLanguageEntry("save.failed"));
            throw new RuntimeException(e);
        }

        logger.info(Language.getLanguageEntry("save.success"));
    }
}
