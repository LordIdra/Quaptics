package org.metamechanists.quaptics.implementation.blocks.manipulators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
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
import java.util.Objects;
import java.util.stream.IntStream;

public class Combiner extends ConnectedBlock {
    private static final Display.Brightness BRIGHTNESS_OFF = new Display.Brightness(5, 0);
    private static final Display.Brightness BRIGHTNESS_ON = new Display.Brightness(13, 0);
    private final Material concreteMaterial;
    private final double connectionAngle = Math.PI / 2;
    private final int connections;
    private final Vector inputStartingLocation = new Vector(0.0F, 0.0F, -connectionRadius);
    private final Vector outputLocation = new Vector(0.0F, 0.0F, connectionRadius);
    private final Vector3f glassDisplaySize = new Vector3f(displayRadius*2);
    private final Vector3f concreteDisplaySize = new Vector3f(displayRadius);
    private final Vector3f displayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final double powerLoss;

    public Combiner(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                    Material concreteMaterial, float radius, int connections, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, radius, 2*radius, maxPower);
        this.concreteMaterial = concreteMaterial;
        this.connections = connections;
        this.powerLoss = powerLoss;
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
        displayGroup.addDisplay("glass", new BlockDisplayBuilder(location.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.GRAY_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, displayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.clone().add(RELATIVE_CENTER))
                .setMaterial(concreteMaterial)
                .setTransformation(Transformations.adjustedRotateAndScale(concreteDisplaySize, displayRotation))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();

        IntStream.range(0, connections).forEach(i -> {
            final String name = "input " + Objects.toString(i);
            final double angle = (-connectionAngle/2) + connectionAngle*((double)(i) / (connections-1));
            final Vector relativeLocation = inputStartingLocation.clone().rotateAroundY(angle);
            points.add(new ConnectionPointInput(groupID, name, formatPointLocation(player, location, relativeLocation)));
        });

        points.add(new ConnectionPointOutput(groupID, "output", formatPointLocation(player, location, outputLocation)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final List<ConnectionPointInput> inputs = IntStream.range(0, connections)
                .mapToObj(i -> (ConnectionPointInput) group.getPoint("input " + Objects.toString(i)))
                .toList();
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        inputs.forEach(input -> doBurnoutCheck(group, input));

        if (!output.hasLink()) {
            getDisplayGroup(group.getLocation()).getDisplays().get("concrete").setBrightness(BRIGHTNESS_OFF);
            return;
        }

        final List<ConnectionPointInput> poweredInputs = inputs.stream()
                .filter(input -> input.hasLink() && input.getLink().isEnabled())
                .toList();
        if (poweredInputs.isEmpty()) {
            output.getLink().setEnabled(false);
            getDisplayGroup(group.getLocation()).getDisplays().get("concrete").setBrightness(BRIGHTNESS_OFF);
            return;
        }

        final double inputPower = poweredInputs.stream()
                .mapToDouble(input -> input.getLink().getPower())
                .sum();
        final double inputFrequency = poweredInputs.stream()
                .mapToDouble(input -> input.getLink().getFrequency())
                .sum();
        final int inputPhase = (int)(poweredInputs.stream()
                .mapToDouble(input -> input.getLink().getPhase())
                .sum() / poweredInputs.size());

        output.getLink().setPower(powerLoss(inputPower, powerLoss));
        output.getLink().setFrequency(inputFrequency);
        output.getLink().setPhase(inputPhase);
        output.getLink().setEnabled(true);
        getDisplayGroup(group.getLocation()).getDisplays().get("concrete").setBrightness(BRIGHTNESS_ON);
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
