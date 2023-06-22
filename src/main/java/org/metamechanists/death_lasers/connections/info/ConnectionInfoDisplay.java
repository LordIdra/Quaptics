package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.storage.ConnectionPointStorage;
import org.metamechanists.death_lasers.items.Lore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionInfoDisplay implements ConfigurationSerializable {
    private boolean hidden = true;
    private final Location pointLocation;
    private DisplayGroup displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.pointLocation = point.getLocation();
        spawnDisplayGroup();
    }

    private ConnectionInfoDisplay(boolean hidden, Location pointLocation, DisplayGroup displayGroup) {
        this.hidden = hidden;
        this.pointLocation = pointLocation;
        this.displayGroup = displayGroup;
    }

    private ConnectionPoint getPoint() {
        return ConnectionPointStorage.getPoint(pointLocation);
    }

    private void spawnDisplayGroup() {
        displayGroup = new InfoDisplayBuilder(pointLocation).add("phase").add("frequency").add("power").add("name").build();
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

        if (!getPoint().hasLink()) {
            return;
        }

        updateText("power", getPoint().getLink().getPower() != 0);
        updateText("frequency", getPoint().getLink().getFrequency() != 0);
        updateText("phase", getPoint().getLink().getPhase() != 0);
    }

    public void update() {
        // Point location has changed, so display location will also need to change
        if (displayGroup.getLocation() != pointLocation) {
            remove();
            spawnDisplayGroup();
        }

        doVisibilityCheck();

        setText("name", ChatColors.color((getPoint().hasLink() ? "&a" : "&c") + getPoint().getName().toUpperCase()));

        if (!getPoint().hasLink()) {
            return;
        }

        final Link link = getPoint().getLink();
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

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("hidden", hidden);
        map.put("pointLocation", pointLocation);
        map.put("displayGroup", displayGroup.getParentUUID());
        return map;
    }

    public static ConnectionInfoDisplay deserialize(Map<String, Object> map) {
        final Location pointLocation = (Location) map.get("pointLocation");
        final DisplayGroup displayGroup = DisplayGroup.fromUUID((UUID) map.get("displayGroup"));
        return new ConnectionInfoDisplay(
                (Boolean) map.get("hidden"),
                pointLocation,
                displayGroup);
    }
}
