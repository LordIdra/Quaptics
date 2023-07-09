package org.metamechanists.quaptics.panels.info;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelAttributeId;

import java.util.HashMap;
import java.util.Map;

public class InfoPanelBuilder {
    private final Vector3f displaySize;
    private final Vector attributeSpacing;
    private final Location location;
    private final Map<String, InfoPanelAttributeId> attributes = new HashMap<>();

    public InfoPanelBuilder(final Location location, final float size) {
        this.displaySize = new Vector3f(size, size, size);
        this.attributeSpacing = new Vector(0, size/3.5, 0);
        this.location = location;
    }

    public InfoPanelBuilder addAttribute(final String name, final boolean hidden) {
        final InfoPanelAttribute attribute = new InfoPanelAttribute(location.clone(), displaySize);
        attribute.setHidden(hidden);
        attributes.put(name, attribute.getId());
        return this;
    }

    public InfoPanelContainer build() {
        return new InfoPanelContainer(location, attributeSpacing, attributes);
    }
}
