package org.metamechanists.death_lasers.connections;

import dev.sefiraat.sefilib.misc.TransformationBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Interaction;

public enum ConnectionType {
    INPUT(Material.LIME_STAINED_GLASS.createBlockData(),
            new Display.Brightness(15, 15),
            new Display.Brightness(3, 3),
            0.2F),
    OUTPUT(Material.RED_STAINED_GLASS.createBlockData(),
            new Display.Brightness(15, 15),
            new Display.Brightness(3, 3),
            0.2F);

    private final BlockData blockData;
    @Getter
    private final Display.Brightness connectedBrightness;
    @Getter
    private final Display.Brightness disconnectedBrightness;
    private final float scale;

    ConnectionType(BlockData blockData, Display.Brightness connectedBrightness, Display.Brightness disconnectedBrightness, float scale) {
        this.blockData = blockData;
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.scale = scale;
    }

    public BlockDisplay buildBlockDisplay(Location location) {
        //final Location locationAdjustedForBukkitWeirdness = location.clone().add(new Vector(scale/2, 0, scale/2));
        final BlockDisplay display = location.getWorld().spawn(location, BlockDisplay.class);
        display.setBlock(blockData);
        display.setBrightness(disconnectedBrightness);
        display.setTransformation(new TransformationBuilder().scale(scale, scale, scale).build());
        return display;
    }

    public Interaction buildInteraction(Location location) {
        final Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(scale);
        interaction.setInteractionHeight(scale);
        return interaction;
    }
}
