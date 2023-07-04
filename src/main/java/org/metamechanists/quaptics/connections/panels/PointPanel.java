package org.metamechanists.quaptics.connections.panels;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.Panel;
import org.metamechanists.quaptics.panels.PanelBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

public class PointPanel {
    private static final Vector POINT_OFFSET = new Vector(0.0, 0.2, 0.0);
    private static final float SIZE = 0.25F;
    private final ConnectionPointId pointId;
    private final Panel panel;

    public PointPanel(@NotNull final Location location, final ConnectionPointId pointId) {
        this.pointId = pointId;
        this.panel = new PanelBuilder(location.clone().add(POINT_OFFSET), SIZE)
                .addAttribute("frequency", true)
                .addAttribute("power", true)
                .addAttribute("name", true)
                .build();
        panel.setAttributeHidden("name", false);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public PointPanel(@NotNull final PanelId panelId, final ConnectionPointId pointId) {
        this.pointId = pointId;
        this.panel = panelId.get().get();
    }

    public PanelId getId() {
        return panel.getId();
    }

    private Optional<ConnectionPoint> getPoint() {
        return pointId.get();
    }

    private static double roundTo2dp(final double value) {
        return Math.round(value*Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public boolean isPanelHidden() {
        return panel.isHidden();
    }

    public void setPanelHidden(final boolean hidden) {
        panel.setHidden(hidden);
    }

    public void changeLocation(final @NotNull Location location) {
        panel.changeLocation(location.clone().add(POINT_OFFSET));
    }

    public void toggleHidden() {
        panel.toggleHidden();
    }

    public void update() {
        if (getPoint().isEmpty()) {
            remove();
            return;
        }

        final ConnectionPoint point = getPoint().get();

        panel.setText("name", (point.getLink().isPresent() ? "&a" : "&c") + point.getName().toUpperCase());

        if (point.getLink().isEmpty()) {
            panel.setAttributeHidden("power", true);
            panel.setAttributeHidden("frequency", true);
            return;
        }

        final Link link = point.getLink().get();

        panel.setAttributeHidden("power", link.getPower() == 0);
        panel.setAttributeHidden("frequency", link.getFrequency() == 0);

        panel.setText("power", Lore.panelPower(roundTo2dp(link.getPower())));
        panel.setText("frequency", Lore.panelFrequency(roundTo2dp(link.getFrequency())));
    }

    public void remove() {
        panel.remove();
    }
}
