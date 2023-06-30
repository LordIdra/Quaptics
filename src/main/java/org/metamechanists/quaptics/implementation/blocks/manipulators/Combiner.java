package org.metamechanists.quaptics.implementation.blocks.manipulators;

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
import java.util.Objects;
import java.util.stream.IntStream;

public class Combiner extends ConnectedBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final double connectionAngle = Math.PI / 2;
    private final Vector inputStartingLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector3f displayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);

    public Combiner(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
        displayGroup.addDisplay("glass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GRAY_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, displayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().material)
                .setBrightness(CONCRETE_BRIGHTNESS)
                .setViewRange(VIEW_RANGE_OFF)
                .setTransformation(Transformations.adjustedRotateAndScale(concreteDisplaySize, displayRotation))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();

        IntStream.range(0, settings.getConnections()).forEach(i -> {
            final String name = "input " + Objects.toString(i);
            final double angle = (-connectionAngle/2) + connectionAngle*((double)(i) / (settings.getConnections()-1));
            final Vector relativeLocation = inputStartingLocation.clone().rotateAroundY(angle);
            points.add(new ConnectionPointInput(groupID, name, formatPointLocation(player, location, relativeLocation)));
        });

        points.add(new ConnectionPointOutput(groupID, "output", formatPointLocation(player, location, outputLocation)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final List<ConnectionPointInput> enabledInputs = getEnabledInputs(group.getLocation());
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        if (enabledInputs.stream().anyMatch(input -> doBurnoutCheck(group, input))) {
            return;
        }

        doDisplayBrightnessCheck(group.getLocation(), "concrete");

        if (!output.hasLink()) {
            return;
        }

        if (enabledInputs.isEmpty()) {
            output.getLink().setEnabled(false);
            return;
        }

        final double inputPower = enabledInputs.stream().mapToDouble(input -> input.getLink().getPower()).sum();
        final double inputFrequency = enabledInputs.stream().mapToDouble(input -> input.getLink().getFrequency()).min().getAsDouble();
        final int inputPhase = (int)(enabledInputs.stream().mapToDouble(input -> input.getLink().getPhase()).sum() / enabledInputs.size());

        output.getLink().setAttributes(powerLoss(inputPower, settings.getPowerLoss()), inputFrequency, inputPhase, true);
    }
}
