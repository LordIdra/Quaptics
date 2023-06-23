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
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.links.LinkProperties;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.base.ConnectedBlock;
import org.metamechanists.death_lasers.utils.Transformations;
import org.metamechanists.death_lasers.utils.builders.BlockDisplayBuilder;

import java.util.ArrayList;
import java.util.List;

public class Lens extends ConnectedBlock {
    private final double powerLoss;

    public Lens(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, maxPower);
        this.powerLoss = powerLoss;
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.clone().add(0.5, 0.5, 0.5))
                        .setMaterial(Material.GLASS)
                        .setTransformation(Transformations.rotateAndScale(
                                new Vector3f(0.2F, 0.2F, 0.2F),
                                new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0)))
                        .build());
        return displayGroup;
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointInput("input", formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, -getRadius()))));
        points.add(new ConnectionPointOutput("output", formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, getRadius()))));
        return points;
    }

    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        doBurnoutCheck(group, input);

        if (!output.hasLink()) {
            return;
        }

        if (!input.hasLink() || !input.getLink().isEnabled()) {
            output.getLink().setEnabled(false);
            return;
        }

        final Link inputLink = input.getLink();
        final Link outputLink = output.getLink();

        outputLink.setPower(LinkProperties.calculatePower(inputLink.getPower(), maxPower, powerLoss));
        outputLink.setEnabled(true);
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
