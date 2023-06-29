package org.metamechanists.quaptics.utils.builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

public class ItemDisplayBuilder {
    private final Location location;
    private Material material;
    private ItemStack itemStack;
    private Matrix4f transformation;
    private Integer brightness;
    private Color glowColor;

    public ItemDisplayBuilder(Location location) {
        this.location = location;
    }
    public ItemDisplay build() {
        return location.getWorld().spawn(location, ItemDisplay.class, display -> {
            if (material != null) {
                display.setItemStack(new ItemStack(material));
            }
            if (itemStack != null) {
                display.setItemStack(itemStack);
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

    public ItemDisplayBuilder setMaterial(Material material) {
        this.material = material;
        return this;
    }
    public ItemDisplayBuilder setItemStack(ItemStack itemStack) {
        // Overrides material
        this.itemStack = itemStack;
        return this;
    }
    public ItemDisplayBuilder setTransformation(Matrix4f transformation) {
        this.transformation = transformation;
        return this;
    }
    public ItemDisplayBuilder setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }
    public ItemDisplayBuilder setGlow(Color glowColor) {
        this.glowColor = glowColor;
        return this;
    }
}
