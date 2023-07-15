package org.metamechanists.quaptics.implementation.attachments;

import org.bukkit.Location;
import org.bukkit.entity.Display.Brightness;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.base.QuapticBlock;
import org.metamechanists.quaptics.utils.Utils;


@FunctionalInterface
public interface PowerAnimatedBlock {
    default void brightnessAnimation(final Location location, final String name, final boolean powered, final int brightnessOn) {
        QuapticBlock.getDisplay(location, name).ifPresent(value -> value.setBrightness(new Brightness(powered ? brightnessOn : Utils.BRIGHTNESS_OFF, 0)));
    }
    default void brightnessAnimation(final Location location, final String name, final boolean powered) {
        brightnessAnimation(location, name, powered, Utils.BRIGHTNESS_ON);
    }
    default void visibilityAnimation(final Location location, final String name, final boolean powered) {
        QuapticBlock.getDisplay(location, name).ifPresent(value -> value.setViewRange(powered ? Utils.VIEW_RANGE_ON : Utils.VIEW_RANGE_OFF));
    }

    @SuppressWarnings("unused")
    void onPoweredAnimation(@NotNull final Location location, final boolean powered);
}
