package org.metamechanists.quaptics.utils.panel;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Vector;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;
import org.metamechanists.quaptics.utils.id.DisplayGroupID;

public class PanelBuilder {
    private final DisplayGroupID displayGroupID;
    private final Vector3f displaySize;
    private final Vector attributeSpacing;
    private final Location nextAttributeLocation;

    public PanelBuilder(Location location, float size) {
        this.displayGroupID = new DisplayGroupID(new DisplayGroup(location, 0, 0).getParentUUID());
        this.displaySize = new Vector3f(size, size, size);
        this.attributeSpacing =  new Vector(0, size/3.5, 0);
        this.nextAttributeLocation = location;
    }

    private DisplayGroup getDisplayGroup() {
        return DisplayGroup.fromUUID(displayGroupID.get());
    }

    public PanelBuilder addAttribute(String name) {
        final TextDisplay display = new TextDisplayBuilder(nextAttributeLocation)
                .setTransformation(Transformations.scale(displaySize))
                .setBrightness(15)
                .setViewRange(0)
                .setBillboard(Display.Billboard.VERTICAL)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build();
        PersistentDataAPI.setBoolean(display, Keys.ATTRIBUTE_HIDDEN, true);
        getDisplayGroup().addDisplay(name, display);
        nextAttributeLocation.add(attributeSpacing);
        return this;
    }

    public Panel build() {
        return new Panel(displayGroupID);
    }
}
