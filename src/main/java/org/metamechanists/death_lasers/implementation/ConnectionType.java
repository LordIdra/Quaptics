package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
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
            new Display.Brightness(5, 5),
            0.1F, 0.1F),
    OUTPUT(Material.RED_STAINED_GLASS.createBlockData(),
            new Display.Brightness(15, 15),
            new Display.Brightness(5, 5),
            0.1F, 0.1F);

    private final BlockDisplayBuilder builder;
    @Getter
    private final Display.Brightness connectedBrightness;
    @Getter
    private final Display.Brightness disconnectedBrightness;
    private final float width;
    private final float height;

    ConnectionType(BlockData blockData, Display.Brightness connectedBrightness, Display.Brightness disconnectedBrightness, float width, float height) {
        this.builder = new BlockDisplayBuilder()
                .setBlockData(blockData)
                .setBrightness(disconnectedBrightness)
                .setDisplayWidth(width)
                .setDisplayHeight(height);
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.width = width;
        this.height = height;
    }

    public BlockDisplay buildBlockDisplay(Location location) {
        return builder.setLocation(location).build();
    }

    public Interaction buildInteraction(Location location) {
        final Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(width);
        interaction.setInteractionHeight(height);
        return interaction;
    }
}
