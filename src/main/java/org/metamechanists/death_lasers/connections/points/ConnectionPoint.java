package org.metamechanists.death_lasers.connections.points;

import dev.sefiraat.sefilib.misc.TransformationBuilder;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.utils.ConnectionPointLocation;

public class ConnectionPoint {
    @Getter
    private final ConnectionPointLocation location;
    private final BlockData blockData;
    @Getter
    protected final Display.Brightness connectedBrightness;
    @Getter
    protected final Display.Brightness disconnectedBrightness;
    private final float scale;
    protected final BlockDisplay blockDisplay;
    protected final Interaction interaction;

    ConnectionPoint(ConnectionPointLocation location, BlockData blockData, Display.Brightness connectedBrightness, Display.Brightness disconnectedBrightness, float scale) {
        this.location = location;
        this.blockData = blockData;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.scale = scale;
        this.blockDisplay = buildBlockDisplay(location.location);
        this.interaction = buildInteraction(location.location);
    }

    private BlockDisplay buildBlockDisplay(Location location) {
        final Location locationAdjustedForBukkitWeirdness = location.clone().add(new Vector(-scale/2, 0, -scale/2));
        final BlockDisplay display = location.getWorld().spawn(locationAdjustedForBukkitWeirdness, BlockDisplay.class);
        display.setBlock(blockData);
        display.setBrightness(disconnectedBrightness);
        display.setTransformation(new TransformationBuilder().scale(scale, scale, scale).build());
        return display;
    }

    private Interaction buildInteraction(Location location) {
        final Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(scale);
        interaction.setInteractionHeight(scale);
        return interaction;
    }

    public void remove() {
        blockDisplay.remove();
        interaction.remove();
    }

    public void select() {
        blockDisplay.setGlowing(true);
        blockDisplay.setGlowColorOverride(Color.fromRGB(0, 255, 0));
    }

    public void deselect() {
        blockDisplay.setGlowing(false);
    }
}
