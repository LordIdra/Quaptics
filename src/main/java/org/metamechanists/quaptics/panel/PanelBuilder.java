package org.metamechanists.quaptics.panel;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.id.DisplayGroupID;
import org.metamechanists.quaptics.utils.id.PanelAttributeID;

import java.util.HashMap;
import java.util.Map;

public class PanelBuilder {
    private final DisplayGroupID displayGroupID;
    private final Vector3f displaySize;
    private final Vector attributeSpacing;
    private final Location nextAttributeLocation;
    private final Map<String, PanelAttributeID> attributes = new HashMap<>();

    public PanelBuilder(Location location, float size) {
        this.displayGroupID = new DisplayGroupID(new DisplayGroup(location, 0, 0).getParentUUID());
        this.displaySize = new Vector3f(size, size, size);
        this.attributeSpacing =  new Vector(0, size/3.5, 0);
        this.nextAttributeLocation = location;
    }

    public PanelBuilder addAttribute(String name) {
        attributes.put(name, new PanelAttribute(nextAttributeLocation, displaySize).getID());
        nextAttributeLocation.add(attributeSpacing);
        return this;
    }

    public Panel build() {
        return new Panel(displayGroupID, attributes);
    }
}
