package org.metamechanists.quaptics.utils;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public enum Colors {
    QUAPTICS("66CCFF"),

    PRIMITIVE("996633"),
    BASIC("339933"),
    INTERMEDIATE("FFCC00"),
    ADVANCED("FF6600"),

    POWER("FF0000"),
    FREQUENCY("00ff00"),
    PHASE("FFCC00"),
    CHARGE("87FFFD"),

    QUAPTIC_COMPONENTS("993366"),
    BEACONS("B82E8A");

    @Getter
    private final String formattedColor;

    Colors(@NotNull final String rawHex) {
        // "#ffffff" -> "&x&f&f&f&f&f&f
        // "#123456" -> "&x&1&2&3&4&5&6
        final StringBuilder colorStringBuilder = new StringBuilder("&x");
        for (final char character : rawHex.toCharArray()) {
            colorStringBuilder.append('&').append(character);
        }

        formattedColor = colorStringBuilder.toString();
    }
}
