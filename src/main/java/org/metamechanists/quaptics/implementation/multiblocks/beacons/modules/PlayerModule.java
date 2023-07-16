package org.metamechanists.quaptics.implementation.multiblocks.beacons.modules;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController;

import java.util.Collection;


@FunctionalInterface
public interface PlayerModule {
    void apply(@NotNull BeaconController controller, @NotNull Location controllerLocation, @NotNull Collection<Player> players);
}
