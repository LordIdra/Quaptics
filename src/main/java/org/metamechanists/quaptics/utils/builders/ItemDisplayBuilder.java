package org.metamechanists.quaptics.utils.builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

@SuppressWarnings("unused")
public class ItemDisplayBuilder {
    private final Location location;
    private Material material;
    private ItemStack itemStack;
    private Matrix4f transformation;
    private Integer brightness;
    private Color glowColor;
    private Billboard billboard;
    private Float viewRange;

    public ItemDisplayBuilder(final Location location) {
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
                display.setBrightness(new Brightness(brightness, 0));
            }
            if (billboard != null) {
                display.setBillboard(billboard);
            }
            if (viewRange != null) {
                display.setViewRange(viewRange);
            }
            display.setDisplayWidth(0);
            display.setDisplayHeight(0);
        });
    }

    public ItemDisplayBuilder setMaterial(final Material material) {
        this.material = material;
        return this;
    }
    public ItemDisplayBuilder setItemStack(final ItemStack itemStack) {
        // Overrides material
        this.itemStack = itemStack;
        return this;
    }
    public ItemDisplayBuilder setTransformation(final Matrix4f transformation) {
        this.transformation = transformation;
        return this;
    }
    public ItemDisplayBuilder setBrightness(final int brightness) {
        this.brightness = brightness;
        return this;
    }
    public ItemDisplayBuilder setGlow(final Color glowColor) {
        this.glowColor = glowColor;
        return this;
    }
    public ItemDisplayBuilder setBillboard(final Billboard billboard) {
        this.billboard = billboard;
        return this;
    }
    public ItemDisplayBuilder setViewRange(final float viewRange) {
        this.viewRange = viewRange;
        return this;
    }
}
