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
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class Splitter extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings SPLITTER_1_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();
    public static final Settings SPLITTER_2_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.14)
            .connections(2)
            .build();
    public static final Settings SPLITTER_2_3_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.14)
            .connections(3)
            .build();
    public static final Settings SPLITTER_3_2_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.08)
            .connections(2)
            .build();
    public static final Settings SPLITTER_3_3_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.08)
            .connections(3)
            .build();
    public static final Settings SPLITTER_4_2_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.25F)
            .connectionRadius(0.5F)
            .powerLoss(0.05)
            .connections(2)
            .build();
    public static final Settings SPLITTER_4_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .powerLoss(0.05)
            .connections(3)
            .build();
    public static final Settings SPLITTER_4_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.35F)
            .connectionRadius(0.7F)
            .powerLoss(0.05)
            .connections(4)
            .build();
    public static final SlimefunItemStack SPLITTER_1_2 = new SlimefunItemStack(
            "QP_SPLITTER_1_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI &8(2 connections)",
            Lore.create(SPLITTER_1_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_2_2 = new SlimefunItemStack(
            "QP_SPLITTER_2_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(2 connections)",
            Lore.create(SPLITTER_2_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_2_3 = new SlimefunItemStack(
            "QP_SPLITTER_2_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eII &8(3 connections)",
            Lore.create(SPLITTER_2_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_3_2 = new SlimefunItemStack(
            "QP_SPLITTER_3_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(2 connections)",
            Lore.create(SPLITTER_3_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_3_3 = new SlimefunItemStack(
            "QP_SPLITTER_3_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIII &8(3 connections)",
            Lore.create(SPLITTER_3_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_4_2 = new SlimefunItemStack(
            "QP_SPLITTER_4_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(2 connections)",
            Lore.create(SPLITTER_4_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_4_3 = new SlimefunItemStack(
            "QP_SPLITTER_4_3",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(3 connections)",
            Lore.create(SPLITTER_4_3_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));
    public static final SlimefunItemStack SPLITTER_4_4 = new SlimefunItemStack(
            "QP_SPLITTER_4_4",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eIV &8(4 connections)",
            Lore.create(SPLITTER_4_4_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));


    private static final double CONNECTION_ANGLE = Math.PI / 2;
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputStartingLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());

    public Splitter(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_GRAY_STAINED_GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(glassDisplaySize)
                        .rotate(Transformations.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(Utils.BRIGHTNESS_ON)
                .setViewRange(Utils.VIEW_RANGE_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(concreteDisplaySize)
                        .rotate(Transformations.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
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
        outgoingLinks.forEach(output -> output.setPowerAndFrequency(outputPower, outputFrequency));
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        visibilityAnimation(location, "concrete", powered);
    }

    private @NotNull Vector getRelativeOutputLocation(final int i) {
        final double angle = (-CONNECTION_ANGLE /2) + CONNECTION_ANGLE *((double)(i) / (settings.getConnections()-1));
        return outputStartingLocation.clone().rotateAroundY(angle);
    }
}
