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
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
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


public class DiffractionGrating extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings DIFFRACTION_GRATING_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .minPower(40)
            .powerLoss(0.05)
            .minFrequency(1.0)
            .maxFrequency(3.0)
            .frequencyMultiplier(2.0)
            .targetPhase(48)
            .targetPhaseSpread(10)
            .build();
    public static final SlimefunItemStack DIFFRACTION_GRATING_1 = new SlimefunItemStack(
            "QP_DIFFRACTION_GRATING_1",
            Material.ORANGE_STAINED_GLASS,
            "&cDiffraction Grating &4I",
            Lore.create(DIFFRACTION_GRATING_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray",
                    "&7● The size of the increase depends on how close the",
                    "&7  auxiliary input is to the target phase"));

    private static final Vector3f MAIN_ROTATION = new Vector3f(0.0F, (float) (Math.PI * 2/3), 0.0F);
    private static final Vector3f MAIN_SIZE = new Vector3f(0.20F, 0.20F, 0.40F);
    private static final Vector3f MAIN_OFFSET = new Vector3f(0.0F, 0.0F, 0.20F).rotateY(MAIN_ROTATION.y);
    private static final Vector3f OUTPUT_ROTATION = new Vector3f(0.0F, (float) (-Math.PI * 2/3), 0.0F);
    private static final Vector3f OUTPUT_SIZE = new Vector3f(0.20F, 0.20F, 0.40F);
    private static final Vector3f OUTPUT_OFFSET = new Vector3f(0.0F, 0.0F, 0.20F).rotateY(OUTPUT_ROTATION.y);
    private static final Vector3f AUXILIARY_ROTATION = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f AUXILIARY_SIZE = new Vector3f(0.15F, 0.15F, 0.40F);
    private static final Vector3f AUXILIARY_OFFSET = new Vector3f(0.0F, 0.0F, 0.20F).rotateY(AUXILIARY_ROTATION.y);
    private static final Vector3f PRISM_ROTATION = new Vector3f(0.0F, (float) (Math.PI/4), 0.0F);
    private static final Vector3f PRISM_SIZE = new Vector3f(0.25F);

    private final Vector mainPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius()).rotateAroundY(MAIN_ROTATION.y);
    private final Vector auxiliaryPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius()).rotateAroundY(AUXILIARY_ROTATION.y);
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius()).rotateAroundY(OUTPUT_ROTATION.y);

    public DiffractionGrating(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.40F;
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
                .setMaterial(Material.TERRACOTTA)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_SIZE)
                        .rotate(MAIN_ROTATION)
                        .translate(MAIN_OFFSET)
                        .lookAlong(face)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("output", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.TERRACOTTA)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(OUTPUT_SIZE)
                        .rotate(OUTPUT_ROTATION)
                        .translate(OUTPUT_OFFSET)
                        .lookAlong(face)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("auxiliary", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GRAY_CONCRETE)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(AUXILIARY_SIZE)
                        .rotate(AUXILIARY_ROTATION)
                        .translate(AUXILIARY_OFFSET)
                        .lookAlong(face)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("prism", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PRISM_SIZE)
                        .lookAlong(face)
                        .rotate(PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "main", formatPointLocation(player, location, mainPointLocation)),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "auxiliary", formatPointLocation(player, location, auxiliaryPointLocation)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "main") || doBurnoutCheck(group, "auxiliary")) {
            return;
        }

        final Optional<Link> mainLink = getLink(location, "main");
        final Optional<Link> auxiliaryLink = getLink(location, "auxiliary");
        final Optional<Link> outputLink = getLink(location, "output");
        final boolean powered = auxiliaryLink.isPresent() && mainLink.isPresent() && settings.isOperational(mainLink);
        onPoweredAnimation(location, powered);

        if (outputLink.isEmpty()) {
            return;
        }

        if (!powered) {
            outputLink.get().disable();
            return;
        }

        final double newFrequency = calculateFrequency(settings, mainLink.get().getFrequency(), auxiliaryLink.get().getPhase());
        if (Utils.equal(newFrequency, mainLink.get().getFrequency())) {
            outputLink.get().disable();
            onPoweredAnimation(location, false);
            return;
        }

        outputLink.get().setPowerFrequencyPhase(
                PowerLossBlock.calculatePowerLoss(settings, mainLink.get()),
                calculateFrequency(settings, mainLink.get().getFrequency(), auxiliaryLink.get().getPhase()),
                mainLink.get().getPhase());
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "prism", powered);
    }

    private static double calculateFrequency(@NotNull final Settings settings, final double frequency, final int phase) {
        final int phaseDifference = Math.abs(phase - settings.getTargetPhase());
        final double targetPhaseDifference = Math.max(settings.getTargetPhaseSpread() - phaseDifference, 0);
        final double targetPhaseProportion = targetPhaseDifference / settings.getTargetPhaseSpread();
        return frequency * (1 + (targetPhaseProportion * (settings.getFrequencyMultiplier() - 1)));
    }
}
