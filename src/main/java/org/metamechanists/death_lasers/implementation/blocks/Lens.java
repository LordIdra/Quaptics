package org.metamechanists.death_lasers.implementation.blocks;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.links.LinkAttributes;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.base.ConnectedBlock;
import org.metamechanists.death_lasers.utils.Transformations;
import org.metamechanists.death_lasers.utils.builders.BlockDisplayBuilder;

import java.util.ArrayList;
import java.util.List;

public class Lens extends ConnectedBlock {
    private final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.2F, 0.2F, 0.2F);
    private final Vector3f MAIN_DISPLAY_ROTATION = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final Vector INPUT_POINT_LOCATION = new Vector(0.0F, 0.0F, -getRadius());
    private final Vector OUTPUT_POINT_LOCATION = new Vector(0.0F, 0.0F, getRadius());
    private final double powerLoss;

    public Lens(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, maxPower);
        this.powerLoss = powerLoss;
    }

    @Override
    protected void addDisplays(DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.GLASS)
                .setTransformation(Transformations.rotateAndScale(MAIN_DISPLAY_SIZE, MAIN_DISPLAY_ROTATION))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointInput("input", formatPointLocation(player, location, INPUT_POINT_LOCATION)));
        points.add(new ConnectionPointOutput("output", formatPointLocation(player, location, OUTPUT_POINT_LOCATION)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        final ConnectionPointInput input = group.getInput("input");
        final ConnectionPointOutput output = group.getOutput("output");

        doBurnoutCheck(group, input);

        if (!output.hasLink()) {
            return;
        }

        if (!input.hasLink() || !input.getLink().isEnabled()) {
            output.getLink().setEnabled(false);
            return;
        }

        output.getLink().setPower(LinkAttributes.powerLoss(input.getLink().getPower(), maxPower, powerLoss));
        output.getLink().setEnabled(true);
    }

    @Override
    protected float getRadius() {
        return 0.35F;
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
