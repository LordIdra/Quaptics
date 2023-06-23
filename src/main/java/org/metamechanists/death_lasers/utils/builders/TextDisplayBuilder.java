package org.metamechanists.death_lasers.utils.builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.joml.Matrix4f;

public class TextDisplayBuilder {
    private final Location location;
    private String text;
    private Matrix4f transformation;
    private Integer brightness;
    private Color glowColor;
    private Float viewRange;
    private Display.Billboard billboard;
    private Color backgroundColor;

    public TextDisplayBuilder(Location location) {
        this.location = location;
    }
    public TextDisplay build() {
        return location.getWorld().spawn(location, TextDisplay.class, display -> {
            if (text != null) {
                display.setText(text);
            }
            if (transformation != null) {
                display.setTransformationMatrix(transformation);
            }
            if (brightness != null) {
                display.setBrightness(new Display.Brightness(brightness, 0));
            }
            if (glowColor != null) {
                display.setGlowing(true);
                display.setGlowColorOverride(glowColor);
            }
            if (viewRange != null) {
                display.setViewRange(viewRange);
            }
            if (billboard != null) {
                display.setBillboard(billboard);
            }
            if (backgroundColor != null) {
                display.setBackgroundColor(backgroundColor);
            }
            display.setDisplayWidth(0);
            display.setDisplayHeight(0);
        });
    }

    public TextDisplayBuilder setText(String text) {
        this.text = text;
        return this;
    }
    public TextDisplayBuilder setTransformation(Matrix4f transformation) {
        this.transformation = transformation;
        return this;
    }
    public TextDisplayBuilder setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }
    public TextDisplayBuilder setGlow(Color glowColor) {
        this.glowColor = glowColor;
        return this;
    }
    public TextDisplayBuilder setViewRange(float viewRange) {
        this.viewRange = viewRange;
        return this;
    }
    public TextDisplayBuilder setBillboard(Display.Billboard billboard) {
        this.billboard = billboard;
        return this;
    }
    public TextDisplayBuilder setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}