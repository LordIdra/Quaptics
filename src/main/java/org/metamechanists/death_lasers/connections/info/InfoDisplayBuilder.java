package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class InfoDisplayBuilder {
    private final DisplayGroup group;
    private final Location location;

    public InfoDisplayBuilder(Location location) {
        this.location = location.clone().add(0, 0.2, 0);
        this.group = new DisplayGroup(location.clone(), 0, 0);
    }

    public void add(String key, String text, boolean hidden) {
        group.addDisplay(key, DisplayUtils.spawnTextDisplay(location, text, 0.25F, new Display.Brightness(15, 0), hidden));
        location.add(0, 0.07, 0);
    }

    public DisplayGroup build() {
        return group;
    }
}
