package org.metamechanists.quaptics.utils.builders;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;


public interface DisplayBuilder {
    Display build(@NotNull final Location location);
}
