package org.metamechanists.quaptics.storage;

import org.bukkit.scheduler.BukkitRunnable;
import org.metamechanists.quaptics.beams.DeprecatedBeamStorage;
import org.metamechanists.quaptics.schedulers.BlockUpdateScheduler;
import org.metamechanists.quaptics.schedulers.PointPanelUpdateScheduler;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

public class QuapticTicker extends BukkitRunnable {
    @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
    public static final int INTERVAL_TICKS = 1;
    public static final int QUAPTIC_TICKS_PER_SECOND = 20;

    @Override
    public void run() {
        QuapticStorage.getGroupIDs().stream()
                .map(ConnectionGroupId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(group -> {
                    try {
                        group.tick();
                    } catch (final RuntimeException exception) {
                        QuapticStorage.removeGroup(group.getId());
                        exception.printStackTrace();
                    }
                });
        DeprecatedBeamStorage.tick();
        BlockUpdateScheduler.tick();
        PointPanelUpdateScheduler.tick();
    }
}