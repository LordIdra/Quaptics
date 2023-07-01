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
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class Combiner extends ConnectedBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private static final double CONNECTION_ANGLE = Math.PI / 2;
    private final Vector inputStartingLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());

    public Combiner(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("glass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GRAY_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(CONCRETE_BRIGHTNESS)
                .setViewRange(VIEW_RANGE_OFF)
                .setTransformation(Transformations.adjustedRotateAndScale(concreteDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();

        IntStream.range(0, settings.getConnections()).forEach(i -> {
            final String name = "input " + Objects.toString(i);
            final double angle = (-CONNECTION_ANGLE /2) + CONNECTION_ANGLE *((double)(i) / (settings.getConnections()-1));
            final Vector relativeLocation = inputStartingLocation.clone().rotateAroundY(angle);
            points.add(new ConnectionPointInput(groupId, name, formatPointLocation(player, location, relativeLocation)));
        });

        points.add(new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputLocation)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final List<ConnectionPointInput> enabledInputs = getEnabledInputs(location.get());
        final Optional<ConnectionPointOutput> output = group.getOutput("output");
        if (output.isEmpty()) {
            return;
        }

        if (enabledInputs.stream().anyMatch(input -> doBurnoutCheck(group, input))) {
            return;
        }

        doDisplayBrightnessCheck(location.get(), "concrete");

        if (output.get().getLink().isEmpty()) {
            return;
        }

        if (enabledInputs.isEmpty()) {
            output.get().getLink().get().setEnabled(false);
            return;
        }

        final double inputPower = enabledInputs.stream().mapToDouble(input -> input.getLink().get().getPower()).sum();
        final double inputFrequency = enabledInputs.stream().mapToDouble(input -> input.getLink().get().getFrequency()).min().orElse(0.0);
        final int inputPhase = (int) (enabledInputs.stream().mapToDouble(input -> input.getLink().get().getPhase()).sum() / enabledInputs.size());
        output.get().getLink().get().setAttributes(settings.powerLoss(inputPower), inputFrequency, inputPhase, true);
    }
}
