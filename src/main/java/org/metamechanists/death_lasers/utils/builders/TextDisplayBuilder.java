package org.metamechanists.death_lasers.utils.builders;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;

public class TextDisplayBuilder extends DisplayBuilder {
    private final String text;
    private boolean setViewRange = false;
    private float viewRange = -1;
    private boolean setBillboard = false;
    private Display.Billboard billboard;

    public TextDisplayBuilder(Location location, String text) {
        super(location);
        this.text = text;
    }

    public void viewRange(int viewRange) {
        this.setViewRange = true;
        this.viewRange = viewRange;
    }

    public void setBillboard(Display.Billboard billboard) {
        this.setBillboard = true;
        this.billboard = billboard;
    }

    public TextDisplay build() {
        return location.getWorld().spawn(location, TextDisplay.class, display -> {
            display.setText(text);
            if (setTransformation) { display.setTransformationMatrix(transformation); }
            if (setBillboard) { display.setBillboard(billboard); }
            if (setViewRange) { display.setViewRange(viewRange);}
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
