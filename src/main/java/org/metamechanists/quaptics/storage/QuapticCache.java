package org.metamechanists.quaptics.storage;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.panels.PanelContainer;
import org.metamechanists.quaptics.panels.PanelAttribute;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.DirectBeamId;
import org.metamechanists.quaptics.utils.id.complex.LinkId;
import org.metamechanists.quaptics.utils.id.complex.PanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class QuapticCache {
    private final Map<DirectBeamId, DirectBeam> directBeams = new ConcurrentHashMap<>();
    private final Map<ConnectionGroupId, ConnectionGroup> connectionGroups = new ConcurrentHashMap<>();
    private final Map<ConnectionPointId, ConnectionPoint> connectionPoints = new ConcurrentHashMap<>();
    private final Map<LinkId, Link> links = new ConcurrentHashMap<>();
    private final Map<PanelAttributeId, PanelAttribute> panelAttributes = new ConcurrentHashMap<>();
    private final Map<PanelId, PanelContainer> panels = new ConcurrentHashMap<>();

    private void garbageCollect(final @NotNull Map<? extends ComplexCustomId, ?> map) {
        map.keySet().stream().filter(x -> !x.isValid()).forEach(map::remove);
    }
    public void garbageCollect() {
        garbageCollect(directBeams);
        garbageCollect(connectionGroups);
        garbageCollect(connectionPoints);
        garbageCollect(links);
        garbageCollect(panelAttributes);
        garbageCollect(panels);
    }

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
    public Optional<PanelContainer> getPanel(final PanelId id) {
        if (!panels.containsKey(id)) {
            panels.put(id, new PanelContainer(id));
        }
        return Optional.ofNullable(panels.get(id));
    }
}
