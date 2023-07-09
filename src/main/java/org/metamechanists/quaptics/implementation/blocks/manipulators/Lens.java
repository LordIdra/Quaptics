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

import java.util.List;
import java.util.Optional;

public class Lens extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings LENS_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.24F)
            .connectionRadius(0.48F)
            .powerLoss(0.1)
            .build();
    public static final Settings LENS_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .displayRadius(0.21F)
            .connectionRadius(0.42F)
            .powerLoss(0.07)
            .build();
    public static final Settings LENS_3_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .displayRadius(0.18F)
            .connectionRadius(0.36F)
            .powerLoss(0.04)
            .build();
    public static final Settings LENS_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .displayRadius(0.15F)
            .connectionRadius(0.30F)
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

    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());

    public Lens(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(glassDisplaySize)
                        .rotate(Transformations.GENERIC_ROTATION_ANGLES)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(concreteDisplaySize)
                        .rotate(Transformations.GENERIC_ROTATION_ANGLES)
                        .buildForBlockDisplay())
                .setBrightness(Utils.BRIGHTNESS_ON)
                .setViewRange(Utils.VIEW_RANGE_OFF)
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

        outputLink.get().setPowerAndFrequency(PowerLossBlock.calculatePowerLoss(settings, inputLink.get()), inputLink.get().getFrequency());
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        visibilityAnimation(location, "concrete", powered);
    }
}
