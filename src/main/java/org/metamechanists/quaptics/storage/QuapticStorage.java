package org.metamechanists.quaptics.storage;

import lombok.experimental.UtilityClass;
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

@UtilityClass
public class QuapticStorage {
    private final Set<ConnectionGroupId> groupIDs = new HashSet<>();

    public @NotNull Set<ConnectionGroupId> getLoadedGroups() {
        return groupIDs.stream()
                .filter(id -> id.get().isPresent())
                .collect(Collectors.toSet());
    }

    private @NotNull List<String> serializeGroupIDs() {
        return groupIDs.stream().map(id -> id.getUUID().toString()).toList();
    }

    public void addGroup(final ConnectionGroupId groupId) {
        groupIDs.add(groupId);
    }

    private void deserializeGroupIDs(@NotNull final List<String> stringIDs) {
        groupIDs.addAll(stringIDs.stream().map(ConnectionGroupId::new).toList());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDirectoryIfNotExists(@NotNull final File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFileIfNotExists(@NotNull final File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create ticker file");
            }
        }
    }

    public void save() {
        final File directory = new File(Quaptics.getInstance().getDataFolder(), "../../data-storage/quaptics/");
        createDirectoryIfNotExists(directory);

        final File file = new File(directory, "tickers.yml");
        createFileIfNotExists(file);

        final YamlConfiguration tickers = new YamlConfiguration();
        tickers.set("tickers", serializeGroupIDs());

        try {
            tickers.save(file);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save ticker file");
        }
    }

    public void load() {
        final File file = new File(Quaptics.getInstance().getDataFolder(), "../../data-storage/quaptics/tickers.yml");

        if (!file.exists()) {
            Quaptics.getInstance().getLogger().severe(Language.getLanguageEntry("load.failed"));
            return;
        }

        final YamlConfiguration tickers = YamlConfiguration.loadConfiguration(file);
        deserializeGroupIDs(tickers.getStringList("tickers"));
    }
}
