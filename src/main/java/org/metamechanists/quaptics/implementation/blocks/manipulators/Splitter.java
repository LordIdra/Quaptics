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
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class Splitter extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private static final double CONNECTION_ANGLE = Math.PI / 2;
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputStartingLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());

    public Splitter(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
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
        IntStream.range(0, settings.getConnections()).forEach(i ->
            points.add(new ConnectionPointOutput(groupId,
                    "output " + Objects.toString(i),
                    formatPointLocation(player, location, getRelativeOutputLocation(i)))));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final List<Link> outgoingLinks = getOutgoingLinks(location.get());
        if (outgoingLinks.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location.get(), "input");
        if (inputLink.isEmpty()) {
            outgoingLinks.forEach(output -> output.setPower(0));
            return;
        }

        onPoweredAnimation(location.get(), inputLink.get().isEnabled());

        final double outputPower = doPowerLoss(settings, inputLink.get().getPower()) / outgoingLinks.size();
        final double outputFrequency = inputLink.get().getFrequency();
        outgoingLinks.forEach(output -> output.setPowerAndFrequency(outputPower, outputFrequency));
    }

    private @NotNull Vector getRelativeOutputLocation(final int i) {
        final double angle = (-CONNECTION_ANGLE /2) + CONNECTION_ANGLE *((double)(i) / (settings.getConnections()-1));
        return outputStartingLocation.clone().rotateAroundY(angle);
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        getDisplay(location, "concrete").ifPresent(value -> value.setViewRange(powered ? VIEW_RANGE_ON : VIEW_RANGE_OFF));
    }
}
