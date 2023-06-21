package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;

public class ConnectionInfoDisplay {
    private boolean hidden = true;
    private final ConnectionPoint point;
    private DisplayGroup displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.point = point;
        spawnDisplayGroup();
        update();
    }

    private void spawnDisplayGroup() {
        displayGroup = new InfoDisplayBuilder(point.getLocation()).add("phase").add("frequency").add("power").add("name").build();
    }

    private double roundTo2dp(double value) {
        return ((double)Math.round(value*Math.pow(10, 2))) / Math.pow(10, 2);
    }

    private void updateText(String key, String text) {
        final TextDisplay display = (TextDisplay) displayGroup.getDisplays().get(key);
        display.setText(text);
    }
    private void showText(String key) {
        final TextDisplay display = (TextDisplay) displayGroup.getDisplays().get(key);
        display.setViewRange(15);
    }
    private void hideText(String key) {
        final TextDisplay display = (TextDisplay) displayGroup.getDisplays().get(key);
        display.setViewRange(0);
    }
    private void doVisibilityCheck() {
        hideText("name");
        hideText("power");
        hideText("frequency");
        hideText("phase");

        if (hidden) {
            return;
        }

        showText("name");

        if (!point.hasLink()) {
            return;
        }

        if (point.getLink().getPower() == 0) { showText("power"); }
        if (point.getLink().getFrequency() == 0) { showText("frequency"); }
        if (point.getLink().getPhase() == 0) { showText("phase"); }
    }

    public void update() {
        // Point location has changed, so display location will also need to change
        if (displayGroup.getLocation() != point.getLocation()) {
            remove();
            spawnDisplayGroup();
        }

        doVisibilityCheck();

        updateText("name", ChatColors.color((point.hasLink() ? "&a" : "&c") + point.getName().toUpperCase()));

        if (!point.hasLink()) {
            return;
        }

        final Link link = point.getLink();
        updateText("power", ChatColors.color(Lore.powerWithoutAttributeSymbol(roundTo2dp(link.getPower()))));
        updateText("frequency", ChatColors.color(Lore.frequencyWithoutAttributeSymbol(roundTo2dp(link.getFrequency()))));
        updateText("phase", ChatColors.color(Lore.phaseWithoutAttributeSymbol(link.getPhase())));
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
