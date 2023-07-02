package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;

@FunctionalInterface
public interface PowerAnimatedBlock {
    @SuppressWarnings("unused")
    void onPoweredAnimation(final Location location, final boolean powered);
}
