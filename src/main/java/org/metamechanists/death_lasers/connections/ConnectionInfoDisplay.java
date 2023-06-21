package org.metamechanists.death_lasers.connections;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class ConnectionInfoDisplay {
    private boolean hidden = true;
    private final ConnectionPoint point;
    private DisplayGroup displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.point = point;
        displayGroup = new DisplayGroup(point.getLocation(), 0, 0);
    }

    private double roundToDp(double value, int decimalPlaces) {
        return ((double)Math.round(value*Math.pow(10, decimalPlaces))) / Math.pow(10, decimalPlaces);
    }

    private String formatPower(Link link) {
        return ChatColors.color(Lore.powerWithoutAttributeSymbol(roundToDp(link.getPower(), 2)));
    }

    private String formatFrequency(Link link) {
        return ChatColors.color(Lore.frequencyWithoutAttributeSymbol(roundToDp(link.getFrequency(), 2)));
    }

    private String formatPhase(Link link) {
        return ChatColors.color(Lore.phaseWithoutAttributeSymbol(link.getPhase()));
    }

    public void update() {
        if (!point.hasLink()) {
            displayGroup.getDisplays().values().forEach(Display::remove);
            return;
        }

        final Link link = point.getLink();

        displayGroup.remove();
        displayGroup = new DisplayGroup(point.getLocation(), 0, 0);

        displayGroup.addDisplay("power", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.34, 0),
                formatPower(link),
                0.25F,
                new Display.Brightness(15, 0),
                true));

        displayGroup.addDisplay("frequency", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.27, 0),
                formatFrequency(link),
                0.25F,
                new Display.Brightness(15, 0),
                true));

        displayGroup.addDisplay("phase", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.2, 0),
                formatPhase(link),
                0.25F,
                new Display.Brightness(15, 0),
                true));
    }

    public void toggleVisibility() {
        hidden = !hidden;
        displayGroup.getDisplays().values().forEach(display -> display.setViewRange(hidden ? 0 : 15));
    }

    public void remove() {
        displayGroup.remove();
    }
}
