package org.metamechanists.quaptics.implementation.beacons.modules.types;

import org.bukkit.entity.Player;

import java.util.Collection;


@FunctionalInterface
public interface PlayerModule {
    void apply(Collection<Player> players);
}
