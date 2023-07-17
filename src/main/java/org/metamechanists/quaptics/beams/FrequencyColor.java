package org.metamechanists.quaptics.beams;

import org.bukkit.Material;

import java.util.List;

public final class FrequencyColor {

    private static final FrequencyColor errorColor = new FrequencyColor(0, Material.WOODEN_PICKAXE);
    private static final List<FrequencyColor> COLORS = List.of(
            new FrequencyColor(0, Material.RED_CONCRETE),
            new FrequencyColor(5, Material.ORANGE_CONCRETE),
            new FrequencyColor(10, Material.YELLOW_CONCRETE),
            new FrequencyColor(40, Material.LIME_CONCRETE),
            new FrequencyColor(200, Material.GREEN_CONCRETE),
            new FrequencyColor(1000, Material.CYAN_CONCRETE),
            new FrequencyColor(6000, Material.LIGHT_BLUE_CONCRETE),
            new FrequencyColor(40000, Material.BLUE_CONCRETE),
            new FrequencyColor(80000, Material.PURPLE_CONCRETE)
            //Material.RED_CONCRETE,
            //Material.RED_TERRACOTTA,
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
            //Material.PINK_CONCRETE,
    );

    private final double frequencyThreshold;
    private final Material material;

    private FrequencyColor(final double frequencyThreshold, final Material material) {
        this.frequencyThreshold = frequencyThreshold;
        this.material = material;
    }

    public static Material getMaterial(final double frequency) {
        for (int i = COLORS.size()-1; i >= 0; i--) {
            final FrequencyColor color = COLORS.get(i);
            if (frequency >= color.frequencyThreshold) {
                return color.material;
            }
        }
        return errorColor.material;
    }
}
