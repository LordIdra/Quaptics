package org.metamechanists.quaptics.implementation.blocks.attachments;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.metamechanists.quaptics.utils.Keys;

import java.util.Objects;

public interface PoweredBlock {
    default boolean isPowered(final Location location) {
        return "true".equals(BlockStorage.getLocationInfo(location, Keys.BS_POWERED));
    }

    default void setPowered(final Location location, final boolean powered) {
        BlockStorage.addBlockInfo(location, Keys.BS_POWERED, Objects.toString(powered));
    }
}
