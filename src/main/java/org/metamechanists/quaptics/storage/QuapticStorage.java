package org.metamechanists.quaptics.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuapticStorage {
    protected static final Set<ConnectionGroupId> groupIDs = new HashSet<>();

    public static void addGroup(ConnectionGroupId groupId) {
        groupIDs.add(groupId);
    }

    public static Set<ConnectionGroupId> getLoadedGroups() {
        return groupIDs.stream()
                .filter(id -> id.get() != null)
                .collect(Collectors.toSet());
    }

    private static List<String> serializeGroupIDs() {
        return groupIDs.stream().map(id -> id.getUUID().toString()).toList();
    }

    private static void deserializeGroupIDs(@NotNull List<String> stringIDs) {
        groupIDs.addAll(stringIDs.stream().map(ConnectionGroupId::new).toList());
    }

    private static void createDirectoryIfNotExists(@NotNull File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    private static void createFileIfNotExists(@NotNull File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create ticker file");
            }
        }
    }

    public static void save() {
        final File directory = new File(Quaptics.getInstance().getDataFolder(), "../../data-storage/quaptics/");
        createDirectoryIfNotExists(directory);

        final File file = new File(directory, "tickers.yml");
        createFileIfNotExists(file);

        final YamlConfiguration tickers = new YamlConfiguration();
        tickers.set("tickers", serializeGroupIDs());

        try {
            tickers.save(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save ticker file");
        }
    }

    public static void load() {
        final File file = new File(Quaptics.getInstance().getDataFolder(), "../../data-storage/quaptics/tickers.yml");

        if (!file.exists()) {
            Quaptics.getInstance().getLogger().severe(Language.getLanguageEntry("load.failed"));
            return;
        }

        final YamlConfiguration tickers = YamlConfiguration.loadConfiguration(file);
        deserializeGroupIDs(tickers.getStringList("tickers"));
    }
}
