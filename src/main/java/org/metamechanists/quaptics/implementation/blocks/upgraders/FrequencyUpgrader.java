package org.metamechanists.quaptics.implementation.blocks.upgraders;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.DirectBeam;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.base.Settings;

public interface FrequencyUpgrader {
    default boolean upgradeFrequency(@NotNull final Settings settings, @NotNull final Link link) {
        final boolean checkFrequency = settings.checkFrequency(link.getFrequency());
        if (checkFrequency) {
            link.getBeam().ifPresent(DirectBeam::nextFrequencyColor);
        }
        return checkFrequency;
    }
}
