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

import java.util.ArrayList;
import java.util.List;

public class Combiner extends ConnectedBlock {
    private final Vector INPUT_1_LOCATION = new Vector(0.0F, 0.0F, -getRadius()).rotateAroundY(-Math.PI/8);
    private final Vector INPUT_2_LOCATION = new Vector(0.0F, 0.0F, -getRadius()).rotateAroundY(Math.PI/8);
    private final Vector OUTPUT_LOCATION = new Vector(0.0F, 0.0F, getRadius());
    private final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.4F, 0.4F, 0.4F);
    private final Vector3f MAIN_DISPLAY_ROTATION = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final double powerLoss;

    public Combiner(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, maxPower);
        this.powerLoss = powerLoss;
    }

    @Override
    protected void addDisplays(DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.clone().add(RELATIVE_CENTER))
                        .setMaterial(Material.GRAY_STAINED_GLASS)
                        .setTransformation(Transformations.rotateAndScale(MAIN_DISPLAY_SIZE, MAIN_DISPLAY_ROTATION))
                        .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointInput("input 1", formatPointLocation(player, location, INPUT_1_LOCATION)));
        points.add(new ConnectionPointInput("input 2", formatPointLocation(player, location, INPUT_2_LOCATION)));
        points.add(new ConnectionPointOutput("output", formatPointLocation(player, location, OUTPUT_LOCATION)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        final ConnectionPointInput input1 = (ConnectionPointInput) group.getPoint("input 1");
        final ConnectionPointInput input2 = (ConnectionPointInput) group.getPoint("input 2");
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        doBurnoutCheck(group, input1);
        doBurnoutCheck(group, input2);

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

        output.getLink().setPower(powerLoss(inputPower, maxPower, powerLoss));
        output.getLink().setEnabled(true);
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
