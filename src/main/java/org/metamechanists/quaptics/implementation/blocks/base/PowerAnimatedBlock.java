package org.metamechanists.quaptics.implementation.blocks.base;

import org.bukkit.Location;

@FunctionalInterface
public interface PowerAnimatedBlock {
    void onPoweredAnimation(final Location location, final boolean powered);
}
