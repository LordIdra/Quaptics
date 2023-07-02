package org.metamechanists.quaptics.storage;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.utils.id.BeamId;
import org.metamechanists.quaptics.utils.id.BlockDisplayId;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.CustomId;
import org.metamechanists.quaptics.utils.id.DisplayGroupId;
import org.metamechanists.quaptics.utils.id.InteractionId;
import org.metamechanists.quaptics.utils.id.LinkId;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.PanelId;
import org.metamechanists.quaptics.utils.id.TextDisplayId;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PersistentDataTraverser {
    private final PersistentDataHolder persistentDataHolder;
    private final Map<String, NamespacedKey> keys = new ConcurrentHashMap<>();

    public PersistentDataTraverser(@NotNull final CustomId id) {
        this.persistentDataHolder = Bukkit.getEntity(id.getUUID());
    }

    private @NotNull NamespacedKey getKey(@NotNull final String key) {
        keys.computeIfAbsent(key, k -> keys.put(key, new NamespacedKey(Quaptics.getInstance(), key)));
        return keys.get(key);
    }

    public void set(@NotNull final String key, final int value) {
        PersistentDataAPI.setInt(persistentDataHolder, getKey(key), value);
    }
    public void set(@NotNull final String key, final double value) {
        PersistentDataAPI.setDouble(persistentDataHolder, getKey(key), value);
    }
    public void set(@NotNull final String key, final boolean value) {
        PersistentDataAPI.setBoolean(persistentDataHolder, getKey(key), value);
    }
    public void set(@NotNull final String key, @Nullable final String value) {
        if (value == null) {
            PersistentDataAPI.remove(persistentDataHolder, getKey(key));
            return;
        }
        PersistentDataAPI.setString(persistentDataHolder, getKey(key), value);
    }
    public void set(@NotNull final String key, final @Nullable CustomId value) {
        set(key, value == null ? null : value.toString());
    }
    public void set(@NotNull final String key, final @NotNull Map<String, ? extends CustomId> value) {
        set(key + "length", value.size());
        int i = 0;
        for (final Entry<String, ? extends CustomId> pair : value.entrySet()) {
            set(key + i + "key", pair.getValue().toString());
            set(key + i + "value", pair.getValue().toString());
            i++;
        }
    }

    public int getInt(@NotNull final String key) {
        return PersistentDataAPI.getInt(persistentDataHolder, getKey(key));
    }
    public double getDouble(@NotNull final String key) {
        return PersistentDataAPI.getDouble(persistentDataHolder, getKey(key));
    }
    public boolean getBoolean(@NotNull final String key) {
        return PersistentDataAPI.getBoolean(persistentDataHolder, getKey(key));
    }
    public @NotNull String getString(@NotNull final String key) {
        return PersistentDataAPI.getString(persistentDataHolder, getKey(key));
    }
    public @Nullable BlockDisplayId getBlockDisplayId(@NotNull final String key) {
        return new BlockDisplayId(getString(key));
    }
    public @Nullable ConnectionGroupId getConnectionGroupId(@NotNull final String key) {
        return new ConnectionGroupId(getString(key));
    }
    public @Nullable ConnectionPointId getConnectionPointId(@NotNull final String key) {
        return new ConnectionPointId(getString(key));
    }
    public @Nullable DisplayGroupId getDisplayGroupId(@NotNull final String key) {
        return new DisplayGroupId(getString(key));
    }
    public @Nullable InteractionId getInteractionId(@NotNull final String key) {
        return new InteractionId(getString(key));
    }
    public @Nullable LinkId getLinkId(@NotNull final String key) {
        return new LinkId(getString(key));
    }
    public @Nullable PanelAttributeId getPanelAttributeId(@NotNull final String key) {
        return new PanelAttributeId(getString(key));
    }
    public @Nullable PanelId getPanelId(@NotNull final String key) {
        return new PanelId(getString(key));
    }
    public @Nullable TextDisplayId getTextDisplayId(@NotNull final String key) {
        return new TextDisplayId(getString(key));
    }
    public @Nullable BeamId getBeamId(@NotNull final String key) {
        return new BeamId(getString(key));
    }
    public @Nullable Map<String, ConnectionPointId> getPointIdMap(@NotNull final String key) {
        final int size = getInt(key + "length");
        final Map<String, ConnectionPointId> list = new HashMap<>();
        IntStream.range(0, size).forEach(i -> {
            final String mapKey = getString(key + i + "key");
            final ConnectionPointId mapValue = getConnectionPointId(key + i + "value");
            list.put(mapKey, mapValue);
        });

        return list;
    }
    
    public @Nullable Map<String, PanelAttributeId> getPanelAttributeIdMap(@NotNull final String key) {
        final int size = getInt(key + "length");
        final Map<String, PanelAttributeId> list = new HashMap<>();
        IntStream.range(0, size).forEach(i -> {
            final String mapKey = getString(key + i + "key");
            final PanelAttributeId mapValue = getPanelAttributeId(key + i + "value");
            list.put(mapKey, mapValue);
        });

        return list;
    }
}
