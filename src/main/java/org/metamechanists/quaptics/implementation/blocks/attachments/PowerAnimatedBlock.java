package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;
import org.bukkit.entity.Display.Brightness;
import org.metamechanists.quaptics.implementation.blocks.base.QuapticBlock;
import org.metamechanists.quaptics.utils.Utils;


@FunctionalInterface
public interface PowerAnimatedBlock {
    default void brightnessAnimation(final Location location, final String name, final boolean powered) {
        QuapticBlock.getDisplay(location, name).ifPresent(value -> value.setBrightness(new Brightness(powered ? Utils.BRIGHTNESS_ON : Utils.BRIGHTNESS_OFF, 0)));
    }
    default void visibilityAnimation(final Location location, final String name, final boolean powered) {
        QuapticBlock.getDisplay(location, name).ifPresent(value -> value.setViewRange(powered ? Utils.VIEW_RANGE_ON : Utils.VIEW_RANGE_OFF));
    }

    @SuppressWarnings("unused")
    void onPoweredAnimation(final Location location, final boolean powered);
}
