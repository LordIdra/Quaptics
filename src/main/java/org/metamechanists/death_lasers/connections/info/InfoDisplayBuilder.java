package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class InfoDisplayBuilder {
    private final DisplayGroup group;
    private final Location location;

    public InfoDisplayBuilder(Location location) {
        this.group = new DisplayGroup(location.clone(), 0, 0);
        this.location = location.clone().add(0, 0.2, 0);
    }

    public InfoDisplayBuilder add(String key) {
        group.addDisplay(key, DisplayUtils.spawnTextDisplay(location, "", 0.25F, new Display.Brightness(15, 0), true));
        location.add(0, 0.07, 0);
        return this;
    }

    public DisplayGroup build() {
        return group;
    }
}
