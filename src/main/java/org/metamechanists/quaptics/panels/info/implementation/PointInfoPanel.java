package org.metamechanists.quaptics.panels.info.implementation;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.info.InfoPanelBuilder;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.panels.info.InfoPanel;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Optional;

public class PointInfoPanel extends InfoPanel {
    private static final float SIZE = 0.25F;
    private final ConnectionPointId pointId;

    public PointInfoPanel(@NotNull final Location location, final ConnectionPointId pointId) {
        super(location);
        this.pointId = pointId;
    }

    public PointInfoPanel(@NotNull final InfoPanelId panelId, final ConnectionPointId pointId) {
        super(panelId);
        this.pointId = pointId;
    }

    private Optional<ConnectionPoint> getPoint() {
        return pointId.get();
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected Vector getOffset() {
        return new Vector(0.0, 0.7, 0.0);
    }

    @Override
    protected InfoPanelContainer buildPanelContainer(@NotNull final Location location) {
        return new InfoPanelBuilder(location.clone().add(getOffset()), SIZE)
                .addAttribute("name", false)
                .addAttribute("power", true)
                .addAttribute("frequency", true)
                .addAttribute("phase", true)
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

        container.setText("name", (point.getLink().isPresent() ? "&a" : "&c") + point.getName().toUpperCase());

        if (point.getLink().isEmpty()) {
            container.setAttributeHidden("power", true);
            container.setAttributeHidden("frequency", true);
            container.setAttributeHidden("phase", true);
            return;
        }

        final Link link = point.getLink().get();

        container.setAttributeHidden("power", link.getPower() == 0);
        container.setAttributeHidden("frequency", link.getFrequency() == 0);
        container.setAttributeHidden("phase", link.getPhase() == 0);

        container.setText("power", Lore.powerNoArrow(Utils.roundTo2dp(link.getPower())));
        container.setText("frequency", Lore.frequencyNoArrow(Utils.roundTo2dp(link.getFrequency())));
        container.setText("phase", Lore.phaseNoArrow(link.getPhase()));
    }
}
