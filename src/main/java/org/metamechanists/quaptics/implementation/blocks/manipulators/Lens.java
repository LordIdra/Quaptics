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
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.transformations.TransformationMatrixBuilder;
import org.metamechanists.quaptics.utils.models.transformations.TransformationUtils;

import java.util.List;
import java.util.Optional;

public class Lens extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings LENS_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .powerLoss(0.1)
            .build();
    public static final Settings LENS_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .powerLoss(0.07)
            .build();
    public static final Settings LENS_3_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .powerLoss(0.04)
            .build();
    public static final Settings LENS_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .powerLoss(0.02)
            .build();
    public static final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Lore.create(LENS_1_SETTINGS,
                    "&7● Redirects a quaptic ray"));
    public static final SlimefunItemStack LENS_2 = new SlimefunItemStack(
            "QP_LENS_2",
            Material.GLASS,
            "&9Lens &bII",
            Lore.create(LENS_2_SETTINGS,
                    "&7● &bRedirects &7a quaptic ray"));
    public static final SlimefunItemStack LENS_3 = new SlimefunItemStack(
            "QP_LENS_3",
            Material.GLASS,
            "&9Lens &bIII",
            Lore.create(LENS_3_SETTINGS,
                    "&7● Redirects a quaptic ray"));
    public static final SlimefunItemStack LENS_4 = new SlimefunItemStack(
            "QP_LENS_4",
            Material.GLASS,
            "&9Lens &bIV",
            Lore.create(LENS_4_SETTINGS,
                    "&7● Redirects a quaptic ray"));

    private static final Vector3f GLASS_DISPLAY_SIZE = new Vector3f(0.20F);
    private static final Vector3f CONCRETE_DISPLAY_SIZE = new Vector3f(0.10F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public Lens(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.30F;
    }
    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(GLASS_DISPLAY_SIZE)
                        .rotate(TransformationUtils.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(Utils.BRIGHTNESS_ON)
                .setViewRange(Utils.VIEW_RANGE_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(CONCRETE_DISPLAY_SIZE)
                        .rotate(TransformationUtils.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<ConnectionPoint> input = group.getPoint("input");
        if (input.isEmpty()) {
            return;
        }

        onPoweredAnimation(location, input.get().isLinkEnabled());

        final Optional<Link> outputLink = getLink(location, "output");
        if (outputLink.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location, "input");
        if (inputLink.isEmpty()) {
            outputLink.get().disable();
            return;
        }

        outputLink.get().setPowerFrequencyPhase(
                PowerLossBlock.calculatePowerLoss(settings, inputLink.get()),
                inputLink.get().getFrequency(),
                inputLink.get().getPhase());
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        visibilityAnimation(location, "concrete", powered);
    }
}
