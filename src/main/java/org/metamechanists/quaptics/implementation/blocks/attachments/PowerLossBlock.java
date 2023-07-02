package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;

public interface PowerLossBlock {
    default double doPowerLoss(final @NotNull Settings settings, final double power) {
        return power * (1 - settings.getPowerLoss());
    }

    default double doPowerLoss(final @NotNull Settings settings, @NotNull final Link inputLink) {
        return doPowerLoss(settings, inputLink.getPower());
    }
}
