package org.metamechanists.quaptics.utils.builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.joml.Matrix4f;

@SuppressWarnings("unused")
public class TextDisplayBuilder {
    private final Location location;
    private String text;
    private Matrix4f transformation;
    private Integer brightness;
    private Color glowColor;
    private Float viewRange;
    private Billboard billboard;
    private TextAlignment alignment;
    private Color backgroundColor;

    public TextDisplayBuilder(final Location location) {
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
                display.setBrightness(new Brightness(brightness, 0));
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
            if (alignment != null) {
                display.setAlignment(alignment);
            }
            if (backgroundColor != null) {
                display.setBackgroundColor(backgroundColor);
            }
            display.setDisplayWidth(0);
            display.setDisplayHeight(0);
        });
    }

    public TextDisplayBuilder setText(final String text) {
        this.text = text;
        return this;
    }
    public TextDisplayBuilder setTransformation(final Matrix4f transformation) {
        this.transformation = transformation;
        return this;
    }
    public TextDisplayBuilder setBrightness(final int brightness) {
        this.brightness = brightness;
        return this;
    }
    public TextDisplayBuilder setGlow(final Color glowColor) {
        this.glowColor = glowColor;
        return this;
    }
    public TextDisplayBuilder setViewRange(final float viewRange) {
        this.viewRange = viewRange;
        return this;
    }
    public TextDisplayBuilder setBillboard(final Billboard billboard) {
        this.billboard = billboard;
        return this;
    }
    public TextDisplayBuilder setAlignment(final TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }
    public TextDisplayBuilder setBackgroundColor(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}