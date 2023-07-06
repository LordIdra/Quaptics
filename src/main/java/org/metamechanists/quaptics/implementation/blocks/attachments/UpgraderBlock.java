package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.blocks.Settings;

@SuppressWarnings("unused")
@FunctionalInterface
public interface UpgraderBlock {
    double calculateUpgrade(final @NotNull Settings settings, final double frequency);
}
