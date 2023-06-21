package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;

public class ConnectionInfoDisplay {
    private boolean hidden = true;
    private final ConnectionPoint point;
    private DisplayGroup displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.point = point;
        displayGroup = new InfoDisplayBuilder(point.getLocation()).add("phase").add("frequency").add("power").add("name").build();
        update();
    }

    private double roundTo2Dp(double value) {
        return ((double)Math.round(value*Math.pow(10, 2))) / Math.pow(10, 2);
    }

    private void updateText(String key, String text) {
        final TextDisplay display = (TextDisplay) displayGroup.getDisplays().get(key);
        display.setText(text);
    }
    private void showText(String key) {
        final TextDisplay display = (TextDisplay) displayGroup.getDisplays().get(key);
        if (!hidden) {
            display.setViewRange(15);
        }
    }
    private void hideText(String key) {
        final TextDisplay display = (TextDisplay) displayGroup.getDisplays().get(key);
        display.setViewRange(0);
    }
    private void updateName() {
        updateText("name", ChatColors.color((point.hasLink() ? "&a" : "&c") + point.getName().toUpperCase()));
    }
    private void updatePower() {
        if (point.getLink().getPower() == 0) {
            hideText("power");
            return;
        }

        showText("power");
        updateText("power", ChatColors.color(Lore.powerWithoutAttributeSymbol(roundTo2Dp(point.getLink().getPower()))));
    }
    private void updateFrequency() {
        if (point.getLink().getFrequency() == 0) {
            hideText("frequency");
            return;
        }

        showText("frequency");
        updateText("frequency", ChatColors.color(Lore.frequencyWithoutAttributeSymbol(roundTo2Dp(point.getLink().getFrequency()))));
    }
    private void updatePhase() {
        if (point.getLink().getPhase() == 0) {
            hideText("phase");
            return;
        }

        showText("phase");
        updateText("phase", ChatColors.color(Lore.phaseWithoutAttributeSymbol(point.getLink().getPhase())));
    }

    public void update() {
        if (!point.hasLink()) {
            return;
        }

        updateName();
        updatePower();
        updateFrequency();
        updatePhase();
    }

    public void toggleVisibility() {
        hidden = !hidden;
        update();
    }

    public void remove() {
        displayGroup.getDisplays().values().forEach(Display::remove);
        displayGroup.remove();
        displayGroup = null;
    }
}
