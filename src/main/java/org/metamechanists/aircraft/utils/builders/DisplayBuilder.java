package org.metamechanists.aircraft.utils.builders;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;


@FunctionalInterface
public interface DisplayBuilder {
    @SuppressWarnings("unused")
    Display build(@NotNull final Location location);
}
