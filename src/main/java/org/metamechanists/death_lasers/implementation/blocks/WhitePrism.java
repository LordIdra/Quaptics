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

public class WhitePrism extends ConnectedBlock {
    private final double powerLoss;

    public WhitePrism(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, maxPower);
        this.powerLoss = powerLoss;
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.clone().add(0.5, 0.5, 0.5))
                        .setMaterial(Material.WHITE_STAINED_GLASS)
                        .setTransformation(Transformations.rotateAndScale(
                                new Vector3f(0.4F, 0.4F, 0.4F),
                                new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0)))
                        .build());
        return displayGroup;
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();

        points.add(new ConnectionPointInput("input",
                formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, -getRadius()))));

        points.add(new ConnectionPointOutput("output 1",
                formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, getRadius()).rotateAroundY(-Math.PI/8))));

        points.add(new ConnectionPointOutput("output 2",
                formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, getRadius()).rotateAroundY(Math.PI/8))));

        return points;
    }

    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");
        final ConnectionPointOutput output1 = (ConnectionPointOutput) group.getPoint("output 1");
        final ConnectionPointOutput output2 = (ConnectionPointOutput) group.getPoint("output 2");

        doBurnoutCheck(group, input);

        if (!output1.hasLink() && !output2.hasLink()) {
            return;
        }

        final boolean inputOn = input.hasLink() && input.getLink().isEnabled();
        if (!inputOn) {
            if (output1.hasLink()) {
                final Link link = output1.getLink();
                link.setEnabled(false);
            }
            if (output2.hasLink()) {
                final Link link = output2.getLink();
                link.setEnabled(false);
            }
            return;
        }

        double outputPower = LinkProperties.calculatePower(input.getLink().getPower(), maxPower, powerLoss);
        if (output1.hasLink() && output2.hasLink()) {
            outputPower /= 2.0;
        }

        if (output1.hasLink()) {
            final Link link = output1.getLink();
            link.setPower(outputPower);
            link.setEnabled(true);
        }

        if (output2.hasLink()) {
            final Link link = output2.getLink();
            link.setPower(outputPower);
            link.setEnabled(true);
        }
    }

    @Override
    protected float getRadius() {
        return 0.55F;
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
