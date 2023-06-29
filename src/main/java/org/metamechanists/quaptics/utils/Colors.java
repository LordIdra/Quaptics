package org.metamechanists.quaptics.utils;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.utils.ColorUtils;

public enum Colors {
    QUAPTICS("66CCFF"),

    PRIMITIVE("996633"),
    BASIC("8C8C8C"),
    INTERMEDIATE("FFCC00"),
    ADVANCED("FF6600"),

    POWER("FF0000"),
    FREQUENCY("00ff00"),
    PHASE("FFCC00"),

    COMPONENTS_MISC("993366");

    @Getter
    private final String string;

    Colors(@NotNull String rawHex) {
        // "#ffffff" -> "&x&f&f&f&f&f&f
        // "#123456" -> "&x&1&2&3&4&5&6
        final StringBuilder colorStringBuilder = new StringBuilder("&x");
        for (char character : rawHex.toCharArray()) {
            colorStringBuilder.append("&").append(character);
        }

        string =  ColorUtils.formatColors(colorStringBuilder.toString());
    }
}
