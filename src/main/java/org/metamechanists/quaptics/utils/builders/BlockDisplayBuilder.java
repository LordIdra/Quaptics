package org.metamechanists.quaptics.utils.builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.joml.Matrix4f;

public class BlockDisplayBuilder {
    private final Location location;
    private Material material;
    private Matrix4f transformation;
    private Integer brightness;
    private Color glowColor;

    public BlockDisplayBuilder(Location location) {
        this.location = location;
    }
    public BlockDisplay build() {
        return location.getWorld().spawn(location, BlockDisplay.class, display -> {
            if (material != null) {
                display.setBlock(material.createBlockData());
            }
            if (transformation != null) {
                display.setTransformationMatrix(transformation);
            }
            if (glowColor != null) {
                display.setGlowing(true);
                display.setGlowColorOverride(glowColor);
            }
            if (brightness != null) {
                display.setBrightness(new Display.Brightness(brightness, 0));
            }
            display.setDisplayWidth(0);
            display.setDisplayHeight(0);
        });
    }

    public BlockDisplayBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }
    public BlockDisplayBuilder setTransformation(Matrix4f transformation) {
        this.transformation = transformation;
        return this;
    }
    public BlockDisplayBuilder setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }
    public BlockDisplayBuilder setGlow(Color glowColor) {
        this.glowColor = glowColor;
        return this;
    }
}
