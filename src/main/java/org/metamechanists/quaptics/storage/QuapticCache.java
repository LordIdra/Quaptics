package org.metamechanists.quaptics.storage;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.beam.Beam;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.panel.PanelAttribute;
import org.metamechanists.quaptics.utils.id.BeamId;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.LinkId;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class QuapticCache {
    private final Map<BeamId, Beam> beams = new HashMap<>();
    private final Map<ConnectionGroupId, ConnectionGroup> connectionGroups = new HashMap<>();
    private final Map<ConnectionPointId, ConnectionPoint> connectionPoints = new HashMap<>();
    private final Map<LinkId, Link> links = new HashMap<>();
    private final Map<PanelAttributeId, PanelAttribute> panelAttributes = new HashMap<>();
    private final Map<PanelId, Panel> panels = new HashMap<>();

    public void add(@NotNull final Beam value) {
        beams.put(value.getId(), value);
    }

    public void add(@NotNull final ConnectionGroup value) {
        connectionGroups.put(value.getId(), value);
    }

    public void add(@NotNull final ConnectionPoint value) {
        connectionPoints.put(value.getId(), value);
    }

    public void add(@NotNull final Link value) {
        links.put(value.getId(), value);
    }

    public void add(@NotNull final PanelAttribute value) {
        panelAttributes.put(value.getId(), value);
    }

    public void add(@NotNull final Panel value) {
        panels.put(value.getId(), value);
    }

    public Optional<Beam> getBeam(final BeamId id) {
        if (!beams.containsKey(id) && id.get().isPresent()) {
            id.get().ifPresent(value -> beams.put(id, value));
        }
        return Optional.ofNullable(beams.get(id));
    }

    public Optional<ConnectionGroup> getConnectionGroup(final ConnectionGroupId id) {
        if (!connectionGroups.containsKey(id) && id.get().isPresent()) {
            id.get().ifPresent(value -> connectionGroups.put(id, value));
        }
        return Optional.ofNullable(connectionGroups.get(id));
    }

    public Optional<ConnectionPoint> getConnectionPoint(final ConnectionPointId id) {
        if (!connectionPoints.containsKey(id) && id.get().isPresent()) {
            id.get().ifPresent(value -> connectionPoints.put(id, value));
        }
        return Optional.ofNullable(connectionPoints.get(id));
    }

    public Optional<Link> getLink(final LinkId id) {
        if (!links.containsKey(id) && id.get().isPresent()) {
            id.get().ifPresent(value -> links.put(id, value));
        }
        return Optional.ofNullable(links.get(id));
    }

    public Optional<PanelAttribute> getPanelAttribute(final PanelAttributeId id) {
        if (!panelAttributes.containsKey(id) && id.get().isPresent()) {
            id.get().ifPresent(value -> panelAttributes.put(id, value));
        }
        return Optional.ofNullable(panelAttributes.get(id));
    }

    public Optional<Panel> getPanel(final PanelId id) {
        if (!panels.containsKey(id) && id.get().isPresent()) {
            id.get().ifPresent(value -> panels.put(id, value));
        }
        return Optional.ofNullable(panels.get(id));
    }
}
