package org.metamechanists.quaptics.connections.panels;

import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.panel.Panel;
import org.metamechanists.quaptics.utils.panel.PanelBuilder;

import java.util.HashMap;
import java.util.Map;

public class PointPanel implements ConfigurationSerializable {
    private static final Vector POINT_OFFSET = new Vector(0, 0.2, 0);
    private static final float SIZE = 0.25F;
    private final ConnectionPointID pointID;
    private final Panel panel;

    public PointPanel(Location location, ConnectionPointID pointID) {
        this.pointID = pointID;
        this.panel = new PanelBuilder(location.clone().add(POINT_OFFSET), SIZE)
                .addAttribute("phase")
                .addAttribute("frequency")
                .addAttribute("power")
                .addAttribute("name")
                .build();
        panel.setAttributeHidden("name", false);
    }

    private PointPanel(ConnectionPointID pointID, Panel panel) {
        this.pointID = pointID;
        this.panel = panel;
    }

    private ConnectionPoint getPoint() {
        return ConnectionPointStorage.getPoint(pointID);
    }

    private double roundTo2dp(double value) {
        return ((double)Math.round(value*Math.pow(10, 2))) / Math.pow(10, 2);
    }

    public boolean isPanelHidden() {
        return panel.isPanelHidden();
    }
    public void setPanelHidden(boolean hidden) {
        panel.setPanelHidden(hidden);
    }

    public void toggleVisibility() {
        panel.toggleVisibility();
    }

    public void update() {
        panel.setText("name", ChatColors.color((getPoint().hasLink() ? "&a" : "&c") + getPoint().getName().toUpperCase()));

        if (!getPoint().hasLink()) {
            return;
        }

        final Link link = getPoint().getLink();

        panel.setAttributeHidden("power", link.getPower() == 0);
        panel.setAttributeHidden("frequency", link.getFrequency() == 0);
        panel.setAttributeHidden("phase", link.getPhase() == 0);

        panel.setText("power", Lore.powerNoArrow(roundTo2dp(link.getPower())));
        panel.setText("frequency", Lore.frequencyNoArrow(roundTo2dp(link.getFrequency())));
        panel.setText("phase", Lore.phaseNoArrow(link.getPhase()));
    }

    public void remove() {
        panel.remove();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("pointID", pointID);
        map.put("panel", panel);
        return map;
    }

    public static PointPanel deserialize(Map<String, Object> map) {
        return new PointPanel(
                (ConnectionPointID) map.get("pointID"),
                (Panel) map.get("panel"));
    }
}
