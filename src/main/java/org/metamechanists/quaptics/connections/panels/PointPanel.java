package org.metamechanists.quaptics.connections.panels;

import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.PanelID;
import org.metamechanists.quaptics.utils.id.PointPanelID;
import org.metamechanists.quaptics.utils.panel.Panel;
import org.metamechanists.quaptics.utils.panel.PanelBuilder;

public class PointPanel {
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
        panel.getAttribute("name").setHidden(false);
    }

    public PointPanel(PointPanelID panelID, ConnectionPointID pointID) {
        this.pointID = pointID;
        this.panel = Panel.fromID(new PanelID(panelID.get()));
    }

    public PointPanelID getID() {
        return new PointPanelID(panel.getID().get());
    }

    private ConnectionPoint getPoint() {
        return ConnectionPoint.fromID(pointID);
    }

    private double roundTo2dp(double value) {
        return ((double)Math.round(value*Math.pow(10, 2))) / Math.pow(10, 2);
    }

    public boolean isPanelHidden() {
        return panel.isHidden();
    }
    public void setPanelHidden(boolean hidden) {
        panel.setHidden(hidden);
    }

    public void toggleVisibility() {
        panel.toggleHidden();
    }

    public void update() {
        panel.getAttribute("name").setText(ChatColors.color((getPoint().hasLink() ? "&a" : "&c") + getPoint().getName().toUpperCase()));

        if (!getPoint().hasLink()) {
            return;
        }

        final Link link = getPoint().getLink();

        panel.getAttribute("power").setHidden(link.getPower() == 0);
        panel.getAttribute("frequency").setHidden(link.getFrequency() == 0);
        panel.getAttribute("phase").setHidden(link.getPhase() == 0);

        panel.getAttribute("power").setText(Lore.powerNoArrow(roundTo2dp(link.getPower())));
        panel.getAttribute("frequency").setText(Lore.frequencyNoArrow(roundTo2dp(link.getFrequency())));
        panel.getAttribute("phase").setText(Lore.phaseNoArrow(link.getPhase()));
    }

    public void remove() {
        panel.remove();
    }
}
