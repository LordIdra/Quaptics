package org.metamechanists.quaptics.panels.implementation;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.PanelBuilder;
import org.metamechanists.quaptics.panels.PanelContainer;
import org.metamechanists.quaptics.panels.Panel;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

public class PointPanel extends Panel {
    private static final float SIZE = 0.25F;
    private final ConnectionPointId pointId;

    public PointPanel(@NotNull final Location location, final ConnectionPointId pointId) {
        super(location);
        this.pointId = pointId;
    }

    public PointPanel(@NotNull final PanelId panelId, final ConnectionPointId pointId) {
        super(panelId);
        this.pointId = pointId;
    }

    private Optional<ConnectionPoint> getPoint() {
        return pointId.get();
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected Vector getOffset() {
        return new Vector(0.0, 0.2, 0.0);
    }

    @Override
    protected PanelContainer buildPanelContainer(@NotNull final Location location) {
        return new PanelBuilder(location.clone().add(getOffset()), SIZE)
                .addAttribute("frequency", true)
                .addAttribute("power", true)
                .addAttribute("name", false)
                .build();
    }

    @Override
    public void update() {
        if (isPanelHidden()) {
            return;
        }

        if (getPoint().isEmpty()) {
            remove();
            return;
        }

        final ConnectionPoint point = getPoint().get();

        panelContainer.setText("name", (point.getLink().isPresent() ? "&a" : "&c") + point.getName().toUpperCase());

        if (point.getLink().isEmpty()) {
            panelContainer.setAttributeHidden("power", true);
            panelContainer.setAttributeHidden("frequency", true);
            return;
        }

        final Link link = point.getLink().get();

        panelContainer.setAttributeHidden("power", link.getPower() == 0);
        panelContainer.setAttributeHidden("frequency", link.getFrequency() == 0);

        panelContainer.setText("power", Lore.powerNoArrow(roundTo2dp(link.getPower())));
        panelContainer.setText("frequency", Lore.frequencyNoArrow(roundTo2dp(link.getFrequency())));
    }
}
