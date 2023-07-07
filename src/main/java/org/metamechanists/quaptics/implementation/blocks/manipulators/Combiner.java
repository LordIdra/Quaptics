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
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Combiner extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private static final double CONNECTION_ANGLE = Math.PI / 2;
    private final Vector inputStartingLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());

    public Combiner(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("glass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GRAY_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateScale(glassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(CONCRETE_BRIGHTNESS)
                .setViewRange(VIEW_RANGE_OFF)
                .setTransformation(Transformations.adjustedRotateScale(concreteDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        final List<ConnectionPoint> points = IntStream.range(0,
                settings.getConnections()).mapToObj(i -> new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input " + Objects.toString(i),
                formatPointLocation(player, location, getRelativeInputLocation(i)))).collect(Collectors.toList());
        points.add(new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputLocation)));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final List<ConnectionPoint> enabledInputs = getEnabledInputs(location.get());
        if (doBurnoutCheck(group, enabledInputs)) {
            return;
        }

        onPoweredAnimation(location.get(), !enabledInputs.isEmpty());

        final List<Link> incomingLinks = getIncomingLinks(location.get());
        final Optional<Link> outputLink = getLink(location.get(), "output");
        if (outputLink.isEmpty()) {
            return;
        }

        final double inputPower = incomingLinks.stream().mapToDouble(Link::getPower).sum();
        final double inputFrequency = incomingLinks.stream().mapToDouble(Link::getFrequency).min().orElse(0.0);
        outputLink.get().setPowerAndFrequency(PowerLossBlock.calculatePowerLoss(settings, inputPower), inputFrequency);
    }

    private @NotNull Vector getRelativeInputLocation(final int i) {
        final double angle = (-CONNECTION_ANGLE /2) + CONNECTION_ANGLE *((double)(i) / (settings.getConnections()-1));
        return inputStartingLocation.clone().rotateAroundY(angle);
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        getDisplay(location, "concrete").ifPresent(value -> value.setViewRange(powered ? VIEW_RANGE_ON : VIEW_RANGE_OFF));
    }
}
