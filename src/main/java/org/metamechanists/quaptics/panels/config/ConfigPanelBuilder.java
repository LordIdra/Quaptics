package org.metamechanists.quaptics.panels.config;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelAttributeId;

import java.util.HashMap;
import java.util.Map;

public class ConfigPanelBuilder {
    private final Vector3f displaySize;
    private final Vector3f displayRotation;
    private final Vector attributeSpacing;
    private final Location location;
    private final Vector offset;
    private final Map<String, ConfigPanelAttributeId> attributes = new HashMap<>();

    public ConfigPanelBuilder(final Location location, final float size, final float rotationY) {
        this.displaySize = new Vector3f(size, size, size);
        this.displayRotation = new Vector3f(0, rotationY, 0);
        this.attributeSpacing = new Vector(0, size/3.5, 0);
        this.location = location;
        this.offset = new Vector();
    }

    public ConfigPanelBuilder addAttribute(final String name, final String key) {
        final ConfigPanelAttribute attribute = new ConfigPanelAttribute(key, location.clone(), offset.clone(), displayRotation, displaySize);
        attributes.put(name, attribute.getId());
        offset.add(attributeSpacing);
        return this;
    }

    public ConfigPanelContainer build() {
        return new ConfigPanelContainer(location, attributes);
    }
}
