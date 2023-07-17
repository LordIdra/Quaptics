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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelDiamond;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class Splitter extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings SPLITTER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .powerLoss(0.2)
            .connections(2)
            .build();
    public static final Settings SPLITTER_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .powerLoss(0.14)
            .connections(3)
            .build();
    public static final Settings SPLITTER_3_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .powerLoss(0.08)
            .connections(4)
            .build();
    public static final Settings SPLITTER_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .powerLoss(0.05)
            .connections(5)
            .build();

    public static final SlimefunItemStack SPLITTER_1 = new SlimefunItemStack(
            "QP_SPLITTER_1",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &8I",
            Lore.create(SPLITTER_1_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_2 = new SlimefunItemStack(
            "QP_SPLITTER_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &8II",
            Lore.create(SPLITTER_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_3 = new SlimefunItemStack(
            "QP_SPLITTER_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &8III",
            Lore.create(SPLITTER_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_4 = new SlimefunItemStack(
            "QP_SPLITTER_4",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &8IV",
            Lore.create(SPLITTER_4_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    private static final double CONNECTION_ANGLE = Math.PI * 2/3;

    private final Vector inputLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());
    private final Vector outputStartingLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public Splitter(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.5F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("concrete", new ModelDiamond()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.4F))
                .add("glass", new ModelDiamond()
                        .material(Material.LIGHT_GRAY_STAINED_GLASS)
                        .size(0.8F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputLocation)));
        IntStream.range(0, settings.getConnections()).forEach(i ->
            points.add(new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output " + Objects.toString(i),
                    formatPointLocation(player, location, getRelativeOutputLocation(i)))));
        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final List<Link> outgoingLinks = getOutgoingLinks(location);
        if (outgoingLinks.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location, "input");
        if (inputLink.isEmpty()) {
            outgoingLinks.forEach(Link::disable);
            return;
        }

        onPoweredAnimation(location, inputLink.get().isEnabled());

        final double outputPower = PowerLossBlock.calculatePowerLoss(settings, inputLink.get().getPower()) / outgoingLinks.size();
        final double outputFrequency = inputLink.get().getFrequency();
        final int outputPhase = inputLink.get().getPhase();
        outgoingLinks.forEach(output -> output.setPowerFrequencyPhase(outputPower, outputFrequency, outputPhase));
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "concrete", powered);
    }

    private @NotNull Vector getRelativeOutputLocation(final int i) {
        final double angle = (-CONNECTION_ANGLE /2) + CONNECTION_ANGLE *((double)(i) / (settings.getConnections()-1));
        return outputStartingLocation.clone().rotateAroundY(angle);
    }
}
