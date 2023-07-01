package org.metamechanists.quaptics.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public enum Colors {
    QUAPTICS("66CCFF"),

    PRIMITIVE("996633"),
    BASIC("8C8C8C"),
    INTERMEDIATE("FFCC00"),
    ADVANCED("FF6600"),

    POWER("FF0000"),
    FREQUENCY("00ff00"),
    PHASE("FFCC00"),
    CHARGE("87FFFD"),

    COMPONENTS_MISC("993366");

    @Getter
    private static final List<Material> CAN_YOU_TASTE_THE_RAINBOW = new ArrayList<>(List.of(
            Material.RED_CONCRETE,
            Material.RED_TERRACOTTA,
            Material.TERRACOTTA,
            Material.ORANGE_TERRACOTTA,
            Material.ORANGE_CONCRETE,
            Material.YELLOW_TERRACOTTA,
            Material.YELLOW_CONCRETE,
            Material.LIME_CONCRETE,
            Material.LIME_TERRACOTTA,
            Material.GREEN_TERRACOTTA,
            Material.GREEN_CONCRETE,
            Material.CYAN_TERRACOTTA,
            Material.CYAN_CONCRETE,
            Material.LIGHT_BLUE_CONCRETE,
            Material.LIGHT_BLUE_TERRACOTTA,
            Material.BLUE_CONCRETE,
            Material.BLUE_TERRACOTTA,
            Material.PURPLE_TERRACOTTA,
            Material.PURPLE_CONCRETE,
            Material.MAGENTA_TERRACOTTA,
            Material.MAGENTA_CONCRETE,
            Material.PINK_TERRACOTTA,
            Material.PINK_CONCRETE
    ));

    @Getter
    private final String formattedColor;

    Colors(@NotNull final String rawHex) {
        // "#ffffff" -> "&x&f&f&f&f&f&f
        // "#123456" -> "&x&1&2&3&4&5&6
        final StringBuilder colorStringBuilder = new StringBuilder("&x");
        for (final char character : rawHex.toCharArray()) {
            colorStringBuilder.append('&').append(character);
        }

        formattedColor = ColorUtils.formatColors(colorStringBuilder.toString());
    }

    public static Material nextFrequencyColor(@NotNull final Material currentColor) {
        final int index = CAN_YOU_TASTE_THE_RAINBOW.indexOf(currentColor);
        if (index == -1) {
            return CAN_YOU_TASTE_THE_RAINBOW.get(0);
        }

        return index == CAN_YOU_TASTE_THE_RAINBOW.size() - 1
                ? CAN_YOU_TASTE_THE_RAINBOW.get(0)
                : CAN_YOU_TASTE_THE_RAINBOW.get(index + 1);
    }
}
