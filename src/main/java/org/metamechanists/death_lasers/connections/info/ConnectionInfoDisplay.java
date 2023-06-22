package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;
import org.metamechanists.death_lasers.storage.SerializationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectionInfoDisplay implements ConfigurationSerializable {
    private boolean hidden = true;
    private final Location pointLocation;
    private UUID displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.pointLocation = point.getLocation();
        spawnDisplayGroup();
    }

    private ConnectionInfoDisplay(boolean hidden, Location pointLocation, UUID displayGroup) {
        this.hidden = hidden;
        this.pointLocation = pointLocation;
        this.displayGroup = displayGroup;
    }

    private DisplayGroup getDisplayGroup() {
        return DisplayGroup.fromUUID(displayGroup);
    }

    private ConnectionPoint getPoint() {
        return ConnectionPointStorage.getPoint(pointLocation);
    }

    private void spawnDisplayGroup() {
        displayGroup = new InfoDisplayBuilder(pointLocation).add("phase").add("frequency").add("power").add("name").build().getParentUUID();
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
        final TextDisplay display = (TextDisplay) getDisplayGroup().getDisplays().get(key);
        display.setText(text);
    }
    private void showText(String key) {
        final TextDisplay display = (TextDisplay) getDisplayGroup().getDisplays().get(key);
        display.setViewRange(15);
    }
    private void hideText(String key) {
        final TextDisplay display = (TextDisplay) getDisplayGroup().getDisplays().get(key);
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
        if (getDisplayGroup().getLocation() != pointLocation) {
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
        getDisplayGroup().getDisplays().values().forEach(Display::remove);
        getDisplayGroup().remove();
        displayGroup = null;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("hidden", hidden);
        map.put("pointLocation", pointLocation);
        map.put("displayGroup", SerializationUtils.serializeUUID(getDisplayGroup().getParentUUID()));
        return map;
    }

    public static ConnectionInfoDisplay deserialize(Map<String, Object> map) {
        return new ConnectionInfoDisplay(
                (Boolean) map.get("hidden"),
                (Location) map.get("pointLocation"),
                SerializationUtils.deserializeUUID((Map<String, Object>) map.get("displayGroup")));
    }
}
