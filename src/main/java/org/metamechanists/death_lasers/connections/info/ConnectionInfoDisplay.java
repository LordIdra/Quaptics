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

    private void updateText(String key, boolean show) {
        if (show) {
            showText(key);
            return;
        }
        hideText(key);
    }
    private void setText(String key, String text) {
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
        if (hidden) {
            hideText("name");
            return;
        }

        showText("name");

        if (!point.hasLink()) {
            return;
        }

        updateText("power", point.getLink().getPower() != 0);
        updateText("frequency", point.getLink().getFrequency() != 0);
        updateText("phase", point.getLink().getPhase() != 0);
    }

    public void update() {
        // Point location has changed, so display location will also need to change
        if (displayGroup.getLocation() != point.getLocation()) {
            remove();
            spawnDisplayGroup();
        }

        doVisibilityCheck();

        setText("name", ChatColors.color((point.hasLink() ? "&a" : "&c") + point.getName().toUpperCase()));

        if (!point.hasLink()) {
            return;
        }

        final Link link = point.getLink();
        setText("power", ChatColors.color(Lore.powerWithoutAttributeSymbol(roundTo2dp(link.getPower()))));
        setText("frequency", ChatColors.color(Lore.frequencyWithoutAttributeSymbol(roundTo2dp(link.getFrequency()))));
        setText("phase", ChatColors.color(Lore.phaseWithoutAttributeSymbol(link.getPhase())));
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
