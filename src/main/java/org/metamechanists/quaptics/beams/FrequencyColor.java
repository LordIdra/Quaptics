package org.metamechanists.quaptics.beams;

import org.bukkit.Material;

import java.util.List;

public final class FrequencyColor {

    private static final FrequencyColor errorColor = new FrequencyColor(0, Material.ANVIL);
    private static final List<FrequencyColor> COLORS = List.of(
            new FrequencyColor(0, Material.RED_CONCRETE),
            new FrequencyColor(0.2, Material.RED_TERRACOTTA)
            //Material.TERRACOTTA,
            //Material.ORANGE_TERRACOTTA,
            //Material.ORANGE_CONCRETE,
            //Material.YELLOW_TERRACOTTA,
            //Material.YELLOW_CONCRETE,
            //Material.LIME_CONCRETE,
            //Material.LIME_TERRACOTTA,
            //Material.GREEN_TERRACOTTA,
            //Material.GREEN_CONCRETE,
            //Material.CYAN_TERRACOTTA,
            //Material.CYAN_CONCRETE,
            //Material.LIGHT_BLUE_CONCRETE,
            //Material.LIGHT_BLUE_TERRACOTTA,
            //Material.BLUE_CONCRETE,
            //Material.BLUE_TERRACOTTA,
            //Material.PURPLE_TERRACOTTA,
            //Material.PURPLE_CONCRETE,
            //Material.MAGENTA_TERRACOTTA,
            //Material.MAGENTA_CONCRETE,
            //Material.PINK_TERRACOTTA,
            //Material.PINK_CONCRETE
    );

    private final double frequencyThreshold;
    private final Material material;

    private FrequencyColor(final double frequencyThreshold, final Material material) {
        this.frequencyThreshold = frequencyThreshold;
        this.material = material;
    }

    public static Material getMaterial(final double frequency) {
        for (final FrequencyColor color : COLORS) {
            if (frequency >= color.frequencyThreshold) {
                return color.material;
            }
        }
        return errorColor.material;
    }
}
