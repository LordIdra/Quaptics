package org.metamechanists.death_lasers.utils.builders;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;

public class BlockDisplayBuilder extends DisplayBuilder {
    private final Material material;

    public BlockDisplayBuilder(Location location, Material material) {
        super(location);
        this.material = material;
    }

    public BlockDisplay build() {
        return location.getWorld().spawn(location, BlockDisplay.class, display -> {
            display.setBlock(material.createBlockData());
            if (setTransformation) { display.setTransformationMatrix(transformation); }
            if (setGlow) {
                display.setGlowing(true);
                display.setGlowColorOverride(glowColor);
            }
            if (setBrightness) { display.setBrightness(new Display.Brightness(brightness, 0)); }
            display.setDisplayWidth(0);
            display.setDisplayHeight(0);
        });
    }
}
