package org.metamechanists.death_lasers.utils;

import lombok.Getter;
import org.metamechanists.metalib.utils.ColorUtils;

public enum Colors {
    EMISSION_POWER("990000"),
    MAX_POWER("FF0000"),
    POWER_LOSS("FF6666");

    @Getter
    private final String string;

    Colors(String rawHex) {
        // "#ffffff" -> "&x&f&f&f&f&f&f
        // "#123456" -> "&x&1&2&3&4&5&6
        final StringBuilder colorStringBuilder = new StringBuilder("&x");
        for (char character : rawHex.toCharArray()) {
            colorStringBuilder.append("&").append(character);
        }

        string =  ColorUtils.formatColors(colorStringBuilder.toString());
    }
}
