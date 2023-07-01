package org.metamechanists.quaptics.panel;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.id.DisplayGroupId;
import org.metamechanists.quaptics.utils.id.PanelAttributeId;

import java.util.HashMap;
import java.util.Map;

public class PanelBuilder {
    private final DisplayGroupId displayGroupId;
    private final Vector3f displaySize;
    private final Vector attributeSpacing;
    private final Location nextAttributeLocation;
    private final Map<String, PanelAttributeId> attributes = new HashMap<>();

    public PanelBuilder(Location location, float size) {
        this.displayGroupId = new DisplayGroupId(new DisplayGroup(location, 0, 0).getParentUUID());
        this.displaySize = new Vector3f(size, size, size);
        this.attributeSpacing =  new Vector(0, size/3.5, 0);
        this.nextAttributeLocation = location;
    }

    public PanelBuilder addAttribute(String name) {
        attributes.put(name, new PanelAttribute(nextAttributeLocation, displaySize).getId());
        nextAttributeLocation.add(attributeSpacing);
        return this;
    }

    public Panel build() {
        return new Panel(displayGroupId, attributes);
    }
}
