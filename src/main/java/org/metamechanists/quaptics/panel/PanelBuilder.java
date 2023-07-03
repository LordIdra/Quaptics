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
    private final Location location;
    private final Vector offset;
    private final Map<String, PanelAttributeId> attributes = new HashMap<>();

    public PanelBuilder(final Location location, final float size) {
        this.displayGroupId = new DisplayGroupId(new DisplayGroup(location, 0, 0).getParentUUID());
        this.displaySize = new Vector3f(size, size, size);
        this.attributeSpacing = new Vector(0, size/3.5, 0);
        this.location = location;
        this.offset = new Vector();
    }

    public PanelBuilder addAttribute(final String name, final boolean hidden) {
        final PanelAttribute attribute = new PanelAttribute(location, offset, displaySize);
        attribute.setHidden(hidden);
        attributes.put(name, attribute.getId());
        offset.add(attributeSpacing);
        return this;
    }

    public Panel build() {
        return new Panel(displayGroupId, attributes);
    }
}
