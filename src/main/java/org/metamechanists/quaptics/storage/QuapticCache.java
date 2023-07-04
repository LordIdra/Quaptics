package org.metamechanists.quaptics.storage;

import lombok.experimental.UtilityClass;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.panel.PanelAttribute;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.DirectBeamId;
import org.metamechanists.quaptics.utils.id.LinkId;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class QuapticCache {
    private final Map<DirectBeamId, DirectBeam> directBeams = new HashMap<>();
    private final Map<ConnectionGroupId, ConnectionGroup> connectionGroups = new HashMap<>();
    private final Map<ConnectionPointId, ConnectionPoint> connectionPoints = new HashMap<>();
    private final Map<LinkId, Link> links = new HashMap<>();
    private final Map<PanelAttributeId, PanelAttribute> panelAttributes = new HashMap<>();
    private final Map<PanelId, Panel> panels = new HashMap<>();

    public Optional<DirectBeam> getBeam(final DirectBeamId id) {
        if (!directBeams.containsKey(id)) {
            directBeams.put(id, new DirectBeam(id));
        }
        return Optional.ofNullable(directBeams.get(id));
    }

    public Optional<ConnectionGroup> getConnectionGroup(final ConnectionGroupId id) {
        if (!connectionGroups.containsKey(id)) {
            connectionGroups.put(id, new ConnectionGroup(id));
        }
        return Optional.ofNullable(connectionGroups.get(id));
    }

    public Optional<ConnectionPoint> getConnectionPoint(final ConnectionPointId id) {
        if (!connectionPoints.containsKey(id)) {
            connectionPoints.put(id, new ConnectionPoint(id));
        }
        return Optional.ofNullable(connectionPoints.get(id));
    }

    public Optional<Link> getLink(final LinkId id) {
        if (!links.containsKey(id)) {
            links.put(id, new Link(id));
        }
        return Optional.ofNullable(links.get(id));
    }

    public Optional<PanelAttribute> getPanelAttribute(final PanelAttributeId id) {
        if (!panelAttributes.containsKey(id)) {
            panelAttributes.put(id, new PanelAttribute(id));
        }
        return Optional.ofNullable(panelAttributes.get(id));
    }

    public Optional<Panel> getPanel(final PanelId id) {
        if (!panels.containsKey(id)) {
            panels.put(id, new Panel(id));
        }
        return Optional.ofNullable(panels.get(id));
    }
}
