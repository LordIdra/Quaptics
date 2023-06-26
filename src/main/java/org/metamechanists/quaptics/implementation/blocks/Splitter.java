package org.metamechanists.quaptics.implementation.blocks;

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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.ArrayList;
import java.util.List;

public class Splitter extends ConnectedBlock {
    private final Vector INPUT_LOCATION = new Vector(0.0F, 0.0F, -getRadius());
    private final Vector OUTPUT_1_LOCATION = new Vector(0.0F, 0.0F, getRadius()).rotateAroundY(-Math.PI/8);
    private final Vector OUTPUT_2_LOCATION = new Vector(0.0F, 0.0F, getRadius()).rotateAroundY(Math.PI/8);
    private final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.4F, 0.4F, 0.4F);
    private final Vector3f MAIN_DISPLAY_ROTATION = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final double powerLoss;

    public Splitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, maxPower);
        this.powerLoss = powerLoss;
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.clone().add(RELATIVE_CENTER))
                        .setMaterial(Material.LIGHT_GRAY_STAINED_GLASS)
                        .setTransformation(Transformations.rotateAndScale(MAIN_DISPLAY_SIZE, MAIN_DISPLAY_ROTATION))
                        .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointInput(groupID, "input", formatPointLocation(player, location, INPUT_LOCATION)));
        points.add(new ConnectionPointOutput(groupID, "output 1", formatPointLocation(player, location, OUTPUT_1_LOCATION)));
        points.add(new ConnectionPointOutput(groupID, "output 2", formatPointLocation(player, location, OUTPUT_2_LOCATION)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");
        final ConnectionPointOutput output1 = (ConnectionPointOutput) group.getPoint("output 1");
        final ConnectionPointOutput output2 = (ConnectionPointOutput) group.getPoint("output 2");

        doBurnoutCheck(group, input);

        if (!output1.hasLink() && !output2.hasLink()) {
            return;
        }

        if (!input.hasLink() || !input.getLink().isEnabled()) {
            if (output1.hasLink()) { output1.getLink().setEnabled(false); }
            if (output2.hasLink()) { output2.getLink().setEnabled(false); }
            return;
        }

        double outputPower = powerLoss(input.getLink().getPower(), maxPower, powerLoss);
        if (output1.hasLink() && output2.hasLink()) {
            outputPower /= 2.0;
        }

        if (output1.hasLink()) {
            output1.getLink().setPower(outputPower);
            output1.getLink().setEnabled(true);
        }

        if (output2.hasLink()) {
            output2.getLink().setPower(outputPower);
            output2.getLink().setEnabled(true);
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
