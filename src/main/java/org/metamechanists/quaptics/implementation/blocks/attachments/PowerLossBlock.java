package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;

public interface PowerLossBlock {
    default double calculatePowerLoss(final @NotNull Settings settings, final double power) {
        return power * (1 - settings.getPowerLoss());
    }

    default double calculatePowerLoss(final @NotNull Settings settings, @NotNull final Link inputLink) {
        return calculatePowerLoss(settings, inputLink.getPower());
    }

    default double calculateReversePowerLoss(final @NotNull Settings settings, final double outputPower) {
        return outputPower / (1 - settings.getPowerLoss());
    }
}
