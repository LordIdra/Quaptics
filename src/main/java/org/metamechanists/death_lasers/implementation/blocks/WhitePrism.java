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
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.links.LinkProperties;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;
import org.metamechanists.death_lasers.utils.DisplayUtils;

import java.util.HashMap;
import java.util.Map;

public class WhitePrism extends ConnectedBlock {
    private final double maxPower;
    private final double powerLoss;

    public WhitePrism(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe);
        this.maxPower = maxPower;
        this.powerLoss = powerLoss;
        // TODO burnout when max power exceeded
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);

        displayGroup.addDisplay(
                "main",
                DisplayUtils.spawnBlockDisplay(
                        location.clone().add(0.5, 0.5, 0.5),
                        Material.WHITE_STAINED_GLASS,
                        DisplayUtils.rotationTransformation(
                                new Vector3f(0.4F, 0.4F, 0.4F),
                                new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0))));

        return displayGroup;
    }

    @Override
    protected Map<String, ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final Map<String, ConnectionPoint> points = new HashMap<>();

        points.put("input 1", new ConnectionPointInput("input 1",
                formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, -0.54F).rotateAroundY(-Math.PI/8))));

        points.put("input 2", new ConnectionPointInput("input 2",
                formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, -0.54F).rotateAroundY(Math.PI/8))));

        points.put("output", new ConnectionPointOutput("output",
                formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, 0.54F))));

        return points;
    }

    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        super.onInputLinkUpdated(group);

        final ConnectionPointInput input1 = (ConnectionPointInput) group.getPoint("input 1");
        final ConnectionPointInput input2 = (ConnectionPointInput) group.getPoint("input 2");
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        if (!output.hasLink()) {
            return;
        }

        final boolean input1On = input1.hasLink() && input1.getLink().isEnabled();
        final boolean input2On = input2.hasLink() && input2.getLink().isEnabled();
        if (!input1On && !input2On) {
            output.getLink().setEnabled(false);
            return;
        }

        double inputPower = 0;
        if (input1On) { inputPower += input1.getLink().getPower(); }
        if (input2On) { inputPower += input2.getLink().getPower(); }

        final Link outputLink = output.getLink();
        outputLink.setPower(LinkProperties.calculatePower(inputPower, maxPower, powerLoss));
        outputLink.setEnabled(true);
    }

    @Override
    protected Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to) {
        final Location fromGroupLocation = ConnectionPointStorage.getGroup(from.getLocation());
        final Location toGroupLocation = ConnectionPointStorage.getGroup(to.getLocation());
        final Vector radiusDirection = DisplayUtils.getDirection(fromGroupLocation, toGroupLocation).multiply(0.55F);
        return fromGroupLocation.clone().add(0.5, 0.5, 0.5).add(radiusDirection);
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
