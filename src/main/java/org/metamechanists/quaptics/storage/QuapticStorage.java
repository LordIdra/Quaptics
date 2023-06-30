package org.metamechanists.quaptics.storage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuapticStorage {
    public static final Set<ConnectionGroupID> groupIDs = new HashSet<>();

    public static void addGroup(ConnectionGroupID groupID) {
        groupIDs.add(groupID);
    }

    public static Set<ConnectionGroupID> getLoadedGroups() {
        return groupIDs.stream()
                .filter(ID -> ID.get() != null)
                .collect(Collectors.toSet());
    }

    private static List<String> serializeGroupIDs() {
        return groupIDs.stream().map(ID -> ID.getUUID().toString()).toList();
    }

    private static void deserializeGroupIDs(@NotNull List<String> stringIDs) {
        groupIDs.addAll(stringIDs.stream().map(ConnectionGroupID::new).toList());
    }

    private static void createIfNotExists(@NotNull File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void save() {
        final File file = new File(Quaptics.getInstance().getDataFolder(), "../../data-storage/quaptics/tickers.yml");

        createIfNotExists(file);

        final YamlConfiguration tickers = new YamlConfiguration();
        tickers.set("tickers", serializeGroupIDs());

        try {
            tickers.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
