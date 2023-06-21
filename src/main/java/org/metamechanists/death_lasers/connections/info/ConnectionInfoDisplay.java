package org.metamechanists.death_lasers.connections.info;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.common.ChatColors;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.items.Lore;

public class ConnectionInfoDisplay {
    private boolean hidden = true;
    private final ConnectionPoint point;
    private DisplayGroup displayGroup;

    public ConnectionInfoDisplay(ConnectionPoint point) {
        this.point = point;
    }

    private void removeDisplays() {
        displayGroup.getDisplays().values().forEach(Display::remove);
        displayGroup.remove();
        displayGroup = null;
    }

    private double roundTo2Dp(double value) {
        return ((double)Math.round(value*Math.pow(10, 2))) / Math.pow(10, 2);
    }

    private String formatName(ConnectionPoint point) {
        return ChatColors.color((point.hasLink() ? "&a" : "&c") + point.getName().toUpperCase());
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
            return;
        }

        if (displayGroup != null) {
            removeDisplays();
        }

        final Link link = point.getLink();
        final InfoDisplayBuilder builder = new InfoDisplayBuilder(point.getLocation());

        if (link.getPhase() != 0) { builder.add("phase", formatPhase(link), hidden); }
        if (link.getFrequency() != 0) { builder.add("frequency", formatFrequency(link), hidden); }
        if (link.getPower() != 0) { builder.add("power", formatPower(link), hidden); }
        builder.add("name", formatName(point), hidden);

        displayGroup = builder.build();
    }

    public void toggleVisibility() {
        hidden = !hidden;
        displayGroup.getDisplays().values().forEach(display -> display.setViewRange(hidden ? 0 : 15));
    }

    public void remove() {
        if (displayGroup != null) {
            removeDisplays();
        }
    }
}
