package org.metamechanists.quaptics.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.TextDisplayBuilder;

public class InfoDisplayBuilder {
    private final DisplayGroup group;
    private final Location location;

    public InfoDisplayBuilder(Location location) {
        this.group = new DisplayGroup(location.clone(), 0, 0);
        this.location = location.clone().add(0, 0.2, 0);
    }

    public InfoDisplayBuilder add(String key) {
        group.addDisplay(key, new TextDisplayBuilder(location)
                .setTransformation(Transformations.scale(new Vector3f(0.25F, 0.25F, 0.25F)))
                .setBrightness(15)
                .setViewRange(0)
                .setBillboard(Display.Billboard.VERTICAL)
                .setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
                .build());
        location.add(0, 0.07, 0);
        return this;
    }

    public DisplayGroup build() {
        return group;
    }
}
