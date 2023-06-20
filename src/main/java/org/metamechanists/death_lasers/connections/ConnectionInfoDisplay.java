package org.metamechanists.death_lasers.connections;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import lombok.Setter;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class ConnectionInfoDisplay {
    @Setter
    private boolean hidden;
    private final ConnectionPoint point;
    private final DisplayGroup displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.point = point;
        displayGroup = new DisplayGroup(point.getLocation());

        // Prevent bounding box obscuring Interactions
        displayGroup.getParentDisplay().setInteractionWidth(0);
        displayGroup.getParentDisplay().setInteractionHeight(0);
    }

    public void update() {
        if (!point.hasLink()) {
            displayGroup.getDisplays().values().forEach(Display::remove);
            return;
        }

        if (!displayGroup.getDisplays().isEmpty()) {
            return;
        }

        final Link link = point.getLink();

        displayGroup.addDisplay("power", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.3, 0),
                ChatColors.color(Lore.powerWithoutAttributeSymbol(link.getPower())),
                0.25F,
                new Display.Brightness(15, 0)));
    }

    public void remove() {
        displayGroup.remove();
    }
}
