package org.metamechanists.quaptics;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.storage.scheduler.BlockUpdateScheduler;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.storage.QuapticStorage;
import org.metamechanists.quaptics.storage.scheduler.PointPanelUpdateScheduler;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.Optional;

public class QuapticTicker extends BukkitRunnable {
    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static final int INTERVAL_TICKS = 1;
    public static final int QUAPTIC_TICKS_PER_SECOND = 20;

    @Override
    public void run() {
        QuapticStorage.getLoadedGroups().stream()
                .map(ConnectionGroupId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(ConnectionGroup::tick);
        DeprecatedBeamStorage.tick();
        BlockUpdateScheduler.tick();
        PointPanelUpdateScheduler.tick();
    }
}