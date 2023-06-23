package org.metamechanists.death_lasers.utils.builders;

import org.bukkit.Color;
import org.bukkit.Location;
import org.joml.Matrix4f;

public abstract class DisplayBuilder {
    protected final Location location;
    protected boolean setTransformation = false;
    protected Matrix4f transformation = new Matrix4f();
    protected boolean setBrightness = false;
    protected int brightness;
    protected boolean setGlow = false;
    protected Color glowColor;

    public DisplayBuilder(Location location) {
        this.location = location;
    }

    public void transformation(Matrix4f transformation) {
        this.setTransformation = true;
        this.transformation = transformation;
    }

    public void brightness(int brightness) {
        this.setBrightness = true;
        this.brightness = brightness;
    }

    public void glow(Color glowColor) {
        this.setGlow = true;
        this.glowColor = glowColor;
    }
}
