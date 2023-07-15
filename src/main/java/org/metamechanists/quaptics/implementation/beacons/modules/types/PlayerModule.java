package org.metamechanists.quaptics.implementation.beacons.modules.types;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


@FunctionalInterface
public interface PlayerModule {
    void apply(@NotNull Collection<Player> players);
}
