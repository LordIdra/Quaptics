package org.metamechanists.quaptics.implementation.blocks.upgraders;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
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
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;
import org.metamechanists.quaptics.utils.transformations.TransformationUtils;

import java.util.List;
import java.util.Optional;


public class Interferometer extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings INTERFEROMETER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .powerLoss(0.07)
            .build();
    public static final SlimefunItemStack INTERFEROMETER_1 = new SlimefunItemStack(
            "QP_INTERFEROMETER_1",
            Material.YELLOW_TERRACOTTA,
            "&cPolariser &4I",
            Lore.create(INTERFEROMETER_1_SETTINGS,
                    "&7● Sets the Phase of the main ray to the phase of the auxiliary ray"));

    private static final Vector3f MAIN_SIZE = new Vector3f(0.30F, 0.30F, 0.90F);
    private static final Vector3f AUXILIARYE_SIZE = new Vector3f(0.40F, 0.15F, 0.15F);
    private static final Vector3f AUXILIARYE_OFFSET = new Vector3f(0.20F, 0.0F, 0.0F);
    private static final Vector3f PRISM_SIZE = new Vector3f(0.20F);

    private static final Vector MAIN_INPUT_LOCATION = new Vector(0.0F, 0.0F, -0.50F);
    private static final Vector AUXILIARY_INPUT_LOCATION = new Vector(0.50F, 0.0F, 0.0F);
    private static final Vector outputLocation = new Vector(0.0F, 0.0F, 0.50);

    public Interferometer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.0F;
    }
    @Override
    protected Optional<Location> calculatePointLocationSphere(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {
        final Optional<ConnectionPoint> point = from.get();
        return point.isPresent() ? point.get().getLocation() : Optional.empty();
    }
    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, @NotNull final Player player) {
        final BlockFace face = TransformationUtils.yawToFace(player.getEyeLocation().getYaw());
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.YELLOW_TERRACOTTA)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_SIZE)
                        .lookAlong(face)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("auxiliary", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GRAY_CONCRETE)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(AUXILIARYE_SIZE)
                        .translate(AUXILIARYE_OFFSET)
                        .lookAlong(face)
                        .rotate(0, (float) (Math.PI/2), 0)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("prism", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PRISM_SIZE)
                        .rotate(TransformationUtils.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "mainInput", formatPointLocation(player, location, MAIN_INPUT_LOCATION)),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "auxiliaryInput", formatPointLocation(player, location, AUXILIARY_INPUT_LOCATION)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "mainInput") || doBurnoutCheck(group, "auxiliaryInput")) {
            return;
        }

        final Optional<Link> mainLink = getLink(location, "mainInput");
        final Optional<Link> auxiliaryLink = getLink(location, "auxiliaryInput");
        final Optional<Link> outputLink = getLink(location, "output");
        onPoweredAnimation(location, settings.isOperational(auxiliaryLink));
        if (mainLink.isEmpty() || outputLink.isEmpty()) {
            return;
        }

        if (auxiliaryLink.isEmpty() || !settings.isOperational(auxiliaryLink.get())) {
            outputLink.get().disable();
            return;
        }

        outputLink.get().setPowerFrequencyPhase(
                PowerLossBlock.calculatePowerLoss(settings, mainLink.get()),
                mainLink.get().getFrequency(),
                auxiliaryLink.get().getPhase());
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "prism", powered);
    }
}
