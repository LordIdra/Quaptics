package org.metamechanists.quaptics.storage;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.utils.id.complex.DirectBeamId;
import org.metamechanists.quaptics.utils.id.simple.BlockDisplayId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.CustomId;
import org.metamechanists.quaptics.utils.id.simple.DisplayGroupId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;
import org.metamechanists.quaptics.utils.id.complex.LinkId;
import org.metamechanists.quaptics.utils.id.complex.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;
import org.metamechanists.quaptics.utils.id.simple.TextDisplayId;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PersistentDataTraverser {
    private final PersistentDataHolder persistentDataHolder;
    private static final Map<String, NamespacedKey> keys = new ConcurrentHashMap<>();

    public PersistentDataTraverser(@NotNull final CustomId id) {
        this.persistentDataHolder = Bukkit.getEntity(id.getUUID());
    }

    private static @NotNull NamespacedKey getKey(@NotNull final String key) {
        return keys.computeIfAbsent(key, k -> new NamespacedKey(Quaptics.getInstance(), key));
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
    public void set(@NotNull final String key, @Nullable final Vector value) {
        if (value == null) {
            PersistentDataAPI.remove(persistentDataHolder, getKey(key + "x"));
            PersistentDataAPI.remove(persistentDataHolder, getKey(key + "y"));
            PersistentDataAPI.remove(persistentDataHolder, getKey(key + "z"));
            return;
        }

        set(key + "x", value.getX());
        set(key + "y", value.getY());
        set(key + "z", value.getZ());
    }
    public void set(@NotNull final String key, @Nullable final CustomId value) {
        set(key, value == null ? null : value.toString());
    }
    public void set(@NotNull final String key, @NotNull final ConnectionPointType value) {
        set(key, value.toString());
    }
    public void set(@NotNull final String key, @NotNull final Map<String, ? extends CustomId> value) {
        set(key + "length", value.size());
        int i = 0;
        for (final Entry<String, ? extends CustomId> pair : value.entrySet()) {
            set(key + i + "key", pair.getKey());
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
    public @Nullable String getString(@NotNull final String key) {
        return PersistentDataAPI.getString(persistentDataHolder, getKey(key));
    }
    public @Nullable Vector getVector(@NotNull final String key) {
        return new Vector(
                PersistentDataAPI.getDouble(persistentDataHolder, getKey(key + "x")),
                PersistentDataAPI.getDouble(persistentDataHolder, getKey(key + "y")),
                PersistentDataAPI.getDouble(persistentDataHolder, getKey(key + "z")));
    }
    public @Nullable ConnectionPointType getConnectionPointType(@NotNull final String key) {
        return ConnectionPointType.valueOf(getString(key));
    }
    public @Nullable BlockDisplayId getBlockDisplayId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new BlockDisplayId(uuid);
    }
    public @Nullable ConnectionGroupId getConnectionGroupId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new ConnectionGroupId(uuid);
    }
    public @Nullable ConnectionPointId getConnectionPointId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new ConnectionPointId(uuid);
    }
    public @Nullable DisplayGroupId getDisplayGroupId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new DisplayGroupId(uuid);
    }
    public @Nullable InteractionId getInteractionId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new InteractionId(uuid);
    }
    public @Nullable LinkId getLinkId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new LinkId(uuid);
    }
    public @Nullable PanelAttributeId getPanelAttributeId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new PanelAttributeId(uuid);
    }
    public @Nullable PanelId getPanelId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new PanelId(uuid);
    }
    public @Nullable TextDisplayId getTextDisplayId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new TextDisplayId(uuid);
    }
    public @Nullable DirectBeamId getBeamId(@NotNull final String key) {
        final String uuid = getString(key);
        return uuid == null ? null : new DirectBeamId(uuid);
    }
    public @NotNull Map<String, ConnectionPointId> getPointIdMap(@NotNull final String key) {
        final int size = getInt(key + "length");
        final Map<String, ConnectionPointId> list = new HashMap<>();
        IntStream.range(0, size).forEach(i -> {
            final String mapKey = getString(key + i + "key");
            final ConnectionPointId mapValue = getConnectionPointId(key + i + "value");
            list.put(mapKey, mapValue);
        });

        return list;
    }
    
    public @NotNull Map<String, PanelAttributeId> getPanelAttributeIdMap(@NotNull final String key) {
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
