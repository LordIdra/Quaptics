package org.metamechanists.quaptics.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import lombok.experimental.UtilityClass;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.utils.id.CustomId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.id.simple.DisplayGroupId;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class BlockStorageAPI {
    public @Nullable SlimefunItem check(final Location location) {
        return BlockStorage.check(location);
    }

    public @Nullable SlimefunItem check(final Block block) {
        return BlockStorage.check(block);
    }

    public void removeData(final Location location) {
        BlockStorage.clearBlockInfo(location);
    }
    private void removeData(final Location location, final String key) {
        BlockStorage.addBlockInfo(location, key, null);
    }

    public boolean hasData(final Location location) {
        return BlockStorage.hasBlockInfo(location);
    }
    public boolean hasData(final Location location, final String key) {
        return hasData(location) && getString(location, key) != null;
    }

    private void set(final Location location, final String key, final String value) {
        BlockStorage.addBlockInfo(location, key, value);
    }
    public void set(final Location location, final String key, final boolean value) {
        set(location, key, Objects.toString(value));
    }
    public void set(final Location location, final String key, final int value) {
        set(location, key, Objects.toString(value));
    }
    public void set(final Location location, final String key, final double value) {
        set(location, key, Objects.toString(value));
    }
    public void set(final Location location, final String key, final @Nullable UUID value) {
        if (value == null) {
            removeData(location, key);
            return;
        }
        set(location, key, value.toString());
    }
    public void set(final Location location, final String key, final @Nullable BlockFace value) {
        if (value == null) {
            removeData(location, key);
            return;
        }
        set(location, key, value.toString());
    }
    public void set(final Location location, final String key, final @Nullable CustomId value) {
        if (value == null) {
            removeData(location, key);
            return;
        }
        set(location, key, value.toString());
    }

    private String getString(final Location location, final String key) {
        return BlockStorage.getLocationInfo(location, key);
    }
    public boolean getBoolean(final Location location, final String key) {
        return "true".equals(getString(location, key));
    }
    public double getDouble(final Location location, final String key) {
        return hasData(location, key)
                ? Double.parseDouble(getString(location, key))
                : 0;
    }
    public int getInt(final Location location, final String key) {
        return hasData(location, key)
                ? Integer.parseInt(getString(location, key))
                : 0;
    }
    public Optional<UUID> getUuid(final Location location, final String key) {
        return hasData(location, key)
                ? Optional.of(UUID.fromString(getString(location, key)))
                : Optional.empty();
    }
    public Optional<BlockFace> getBlockFace(final Location location, final String key) {
        return hasData(location, key)
                ? Optional.of(BlockFace.valueOf(getString(location, key)))
                : Optional.empty();
    }
    public Optional<InfoPanelId> getPanelId(final Location location, final String key) {
        return hasData(location, key)
                ? Optional.of(new InfoPanelId(getString(location, key)))
                : Optional.empty();
    }
    public Optional<DisplayGroupId> getDisplayGroupId(final Location location, final String key) {
        return hasData(location, key)
                ? Optional.of(new DisplayGroupId(getString(location, key)))
                : Optional.empty();
    }
}
