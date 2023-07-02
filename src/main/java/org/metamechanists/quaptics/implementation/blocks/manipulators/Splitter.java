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
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.Settings;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class Splitter extends ConnectedBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private static final double CONNECTION_ANGLE = Math.PI / 2;
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputStartingLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());

    public Splitter(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_GRAY_STAINED_GLASS)
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

        points.add(new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputLocation)));
        IntStream.range(0, settings.getConnections()).forEach(i -> {
            final String name = "output " + Objects.toString(i);
            final double angle = (-CONNECTION_ANGLE /2) + CONNECTION_ANGLE *((double)(i) / (settings.getConnections()-1));
            final Vector relativeLocation = outputStartingLocation.clone().rotateAroundY(angle);
            points.add(new ConnectionPointOutput(groupId, name, formatPointLocation(player, location, relativeLocation)));
        });

        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final List<ConnectionPointOutput> linkedOutputs = getLinkedOutputs(location.get());
        final Optional<ConnectionPointInput> input = group.getInput("input");
        if (input.isEmpty()) {
            return;
        }

        if (doBurnoutCheck(group, input.get())) {
            return;
        }

        doDisplayBrightnessCheck(location.get(), "concrete");

        if (linkedOutputs.isEmpty()) {
            return;
        }

        if (!input.get().isLinkEnabled()) {
            linkedOutputs.forEach(output -> output.getLink().get().setEnabled(false));
            return;
        }

        final Link inputLink = input.get().getLink().get();
        final double outputPower = settings.powerLoss(inputLink.getPower()) / linkedOutputs.size();
        final double outputFrequency = inputLink.getFrequency();
        final int outputPhase = inputLink.getPhase();

        linkedOutputs.forEach(output -> output.getLink().get().setAttributes(outputPower, outputFrequency, outputPhase, true));
    }
}
