package org.metamechanists.quaptics.storage;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.panels.config.ConfigPanelAttribute;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.panels.info.InfoPanelAttribute;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.DirectBeamId;
import org.metamechanists.quaptics.utils.id.complex.LinkId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelAttributeId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("OverlyCoupledClass")
@UtilityClass
public class QuapticCache {
    private final Map<DirectBeamId, DirectBeam> directBeams = new ConcurrentHashMap<>();
    private final Map<ConnectionGroupId, ConnectionGroup> connectionGroups = new ConcurrentHashMap<>();
    private final Map<ConnectionPointId, ConnectionPoint> connectionPoints = new ConcurrentHashMap<>();
    private final Map<LinkId, Link> links = new ConcurrentHashMap<>();
    private final Map<InfoPanelAttributeId, InfoPanelAttribute> infoPanelAttributes = new ConcurrentHashMap<>();
    private final Map<InfoPanelId, InfoPanelContainer> infoPanels = new ConcurrentHashMap<>();
    private final Map<ConfigPanelAttributeId, ConfigPanelAttribute> configPanelAttributes = new ConcurrentHashMap<>();
    private final Map<ConfigPanelId, ConfigPanelContainer> configPanels = new ConcurrentHashMap<>();

    private void garbageCollect(final @NotNull Map<? extends ComplexCustomId, ?> map) {
        map.keySet().stream().filter(x -> !x.isValid()).forEach(map::remove);
    }
    public void garbageCollect() {
        garbageCollect(directBeams);
        garbageCollect(connectionGroups);
        garbageCollect(connectionPoints);
        garbageCollect(links);
        garbageCollect(infoPanelAttributes);
        garbageCollect(infoPanels);
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
    public Optional<InfoPanelAttribute> getInfoPanelAttribute(final InfoPanelAttributeId id) {
        if (!infoPanelAttributes.containsKey(id)) {
            infoPanelAttributes.put(id, new InfoPanelAttribute(id));
        }
        return Optional.ofNullable(infoPanelAttributes.get(id));
    }
    public Optional<InfoPanelContainer> getInfoPanel(final InfoPanelId id) {
        if (!infoPanels.containsKey(id)) {
            infoPanels.put(id, new InfoPanelContainer(id));
        }
        return Optional.ofNullable(infoPanels.get(id));
    }
    public Optional<ConfigPanelContainer> getConfigPanel(final ConfigPanelId id) {
        if (!configPanels.containsKey(id)) {
            configPanels.put(id, new ConfigPanelContainer(id));
        }
        return Optional.ofNullable(configPanels.get(id));
    }
    public Optional<ConfigPanelAttribute> getConfigPanelAttribute(final ConfigPanelAttributeId id) {
        if (!configPanelAttributes.containsKey(id)) {
            configPanelAttributes.put(id, new ConfigPanelAttribute(id));
        }
        return Optional.ofNullable(configPanelAttributes.get(id));
    }
}
