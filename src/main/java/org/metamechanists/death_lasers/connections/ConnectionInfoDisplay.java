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

    private double roundTo2Dp(double value) {
        return ((double)Math.round(value*Math.pow(10, 2))) / Math.pow(10, 2);
    }

    private String formatName(ConnectionPoint point) {
        return ChatColors.color(point.getLink().isEnabled() ? "&a" : "&c" + point.getName().toUpperCase());
    }
    private String formatPower(Link link) {
        return ChatColors.color(Lore.powerWithoutAttributeSymbol(roundTo2Dp(link.getPower())));
    }
    private String formatFrequency(Link link) {
        return ChatColors.color(Lore.frequencyWithoutAttributeSymbol(roundTo2Dp(link.getFrequency())));
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

        displayGroup.addDisplay("name", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.41, 0),
                formatName(point),
                0.25F,
                new Display.Brightness(15, 0),
                hidden));

        displayGroup.addDisplay("power", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.34, 0),
                formatPower(link),
                0.25F,
                new Display.Brightness(15, 0),
                hidden));

        displayGroup.addDisplay("frequency", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.27, 0),
                formatFrequency(link),
                0.25F,
                new Display.Brightness(15, 0),
                hidden));

        displayGroup.addDisplay("phase", DisplayUtils.spawnTextDisplay(
                point.getLocation().clone().add(0, 0.2, 0),
                formatPhase(link),
                0.25F,
                new Display.Brightness(15, 0),
                hidden));
    }

    public void toggleVisibility() {
        hidden = !hidden;
        displayGroup.getDisplays().values().forEach(display -> display.setViewRange(hidden ? 0 : 15));
    }

    public void remove() {
        displayGroup.remove();
    }
}
