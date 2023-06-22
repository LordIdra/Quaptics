package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;
import org.metamechanists.death_lasers.utils.id.ConnectionPointID;
import org.metamechanists.death_lasers.utils.id.DisplayGroupID;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConnectionInfoDisplay implements ConfigurationSerializable {
    @Getter
    private boolean hidden;
    private final ConnectionPointID pointID;
    private DisplayGroupID displayGroupID;

    public ConnectionInfoDisplay(ConnectionPointID pointID, boolean hidden) {
        this.pointID = pointID;
        this.hidden = hidden;
        spawnDisplayGroup();
    }

    private ConnectionInfoDisplay(boolean hidden, ConnectionPointID pointID, DisplayGroupID displayGroupID) {
        this.hidden = hidden;
        this.pointID = pointID;
        this.displayGroupID = displayGroupID;
    }

    private DisplayGroup getDisplayGroupID() {
        return DisplayGroup.fromUUID(displayGroupID.get());
    }

    private ConnectionPoint getPoint() {
        return ConnectionPointStorage.getPoint(pointID);
    }

    private void spawnDisplayGroup() {
        displayGroupID = new DisplayGroupID(new InfoDisplayBuilder(getPoint().getLocation())
                .add("phase").add("frequency").add("power").add("name").build().getParentUUID());
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
        final TextDisplay display = (TextDisplay) getDisplayGroupID().getDisplays().get(key);
        if (!Objects.equals(display.getText(), (text))) {
            display.setText(text);
        }
    }
    private void showText(String key) {
        final TextDisplay display = (TextDisplay) getDisplayGroupID().getDisplays().get(key);
        display.setViewRange(15);
    }
    private void hideText(String key) {
        final TextDisplay display = (TextDisplay) getDisplayGroupID().getDisplays().get(key);
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
        if (!getDisplayGroupID().getLocation().equals(getPoint().getLocation())) {
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
        getDisplayGroupID().getDisplays().values().forEach(Display::remove);
        getDisplayGroupID().remove();
        displayGroupID = null;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("hidden", hidden);
        map.put("pointID", pointID);
        map.put("displayGroupID", new DisplayGroupID(getDisplayGroupID().getParentUUID()));
        return map;
    }

    public static ConnectionInfoDisplay deserialize(Map<String, Object> map) {
        return new ConnectionInfoDisplay(
                (Boolean) map.get("hidden"),
                (ConnectionPointID) map.get("pointID"),
                (DisplayGroupID) map.get("displayGroupID"));
    }
}
