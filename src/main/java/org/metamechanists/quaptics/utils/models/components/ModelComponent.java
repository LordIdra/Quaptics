package org.metamechanists.quaptics.utils.models.components;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a single component of a model, composed of one or multiple Displays.
 */
public interface ModelComponent {
    Display build(@NotNull final Location origin);
    Display build(@NotNull final Block block);
}
