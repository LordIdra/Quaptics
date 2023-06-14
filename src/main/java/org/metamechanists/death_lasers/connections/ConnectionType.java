package org.metamechanists.death_lasers.connections;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
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
            new Display.Brightness(0, 0),
            0.2F),
    OUTPUT(Material.RED_STAINED_GLASS.createBlockData(),
            new Display.Brightness(15, 15),
            new Display.Brightness(0, 0),
            0.2F);

    private final BlockDisplayBuilder builder;
    @Getter
    private final Display.Brightness connectedBrightness;
    @Getter
    private final Display.Brightness disconnectedBrightness;
    private final float scale;

    ConnectionType(BlockData blockData, Display.Brightness connectedBrightness, Display.Brightness disconnectedBrightness, float scale) {
        this.builder = new BlockDisplayBuilder()
                .setBlockData(blockData)
                .setBrightness(disconnectedBrightness)
                .setTransformation(new TransformationBuilder().scale(scale, scale, scale).build())
                .setDisplayWidth(scale)
                .setDisplayHeight(scale);
        this.connectedBrightness = connectedBrightness;
        this.disconnectedBrightness = disconnectedBrightness;
        this.scale = scale;
    }

    public BlockDisplay buildBlockDisplay(Location location) {
        final BlockDisplay display = builder.build();
        display.teleport(location);
        return display;
    }

    public Interaction buildInteraction(Location location) {
        final Interaction interaction = location.getWorld().spawn(location, Interaction.class);
        interaction.setInteractionWidth(scale);
        interaction.setInteractionHeight(scale);
        interaction.teleport(location);
        return interaction;
    }
}
