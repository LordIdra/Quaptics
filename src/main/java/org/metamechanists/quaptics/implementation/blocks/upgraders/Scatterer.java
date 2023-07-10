package org.metamechanists.quaptics.implementation.blocks.upgraders;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
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
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.components.ModelDiamond;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;
import org.metamechanists.quaptics.utils.transformations.TransformationUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Scatterer extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings SCATTERER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .minPower(40)
            .powerLoss(0.05)
            .minFrequency(0.2)
            .maxFrequency(1.0)
            .frequencyMultiplier(2.0)
            .comparatorVisual("compare")
            .build();
    public static final SlimefunItemStack SCATTERER_1 = new SlimefunItemStack(
            "QP_SCATTERER_1",
            Material.ORANGE_STAINED_GLASS,
            "&cScatterer &4I",
            Lore.create(SCATTERER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    private static final Vector3f GLASS_DISPLAY_SIZE = new Vector3f(0.50F);
    private static final Vector3f COMPARATOR_DISPLAY_SIZE = new Vector3f(0.25F);
    private static final Vector3f COMPARATOR_OFFSET = new Vector3f(0.0F, 0.10F, 0.0F);
    private static final Vector3f CONCRETE_DISPLAY_SIZE = new Vector3f(0.26F, 0.075F, 0.26F);
    private static final Vector3f CONCRETE_OFFSET = new Vector3f(0.0F, -0.05F, 0.0F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public Scatterer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.50F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        final DisplayGroup displayGroup = new DisplayGroup(location);
        final BlockFace face = TransformationUtils.yawToFace(player.getEyeLocation().getYaw());
        displayGroup.addDisplay("main", new BlockDisplayBuilder()
                .material(Material.ORANGE_STAINED_GLASS)
                .transformation(new TransformationMatrixBuilder()
                        .scale(GLASS_DISPLAY_SIZE)
                        .rotate(ModelDiamond.ROTATION)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder()
                .material(settings.getTier().concreteMaterial)
                .brightness(Utils.BRIGHTNESS_ON)
                .transformation(new TransformationMatrixBuilder()
                        .scale(CONCRETE_DISPLAY_SIZE)
                        .translate(CONCRETE_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        final BlockDisplay comparator = new BlockDisplayBuilder()
                .material(Material.COMPARATOR)
                .blockData(createComparatorBlockData(face.name().toLowerCase(), false))
                .transformation(new TransformationMatrixBuilder()
                        .scale(COMPARATOR_DISPLAY_SIZE)
                        .translate(COMPARATOR_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation());
        PersistentDataAPI.setString(comparator, Keys.FACING, face.name().toLowerCase());
        displayGroup.addDisplay("comparator", comparator);
        return displayGroup;
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

        final Optional<Link> inputLink = getLink(location, "input");
        final Optional<Link> outputLink = getLink(location, "output");
        onPoweredAnimation(location, settings.isOperational(inputLink));
        if (outputLink.isEmpty()) {
            return;
        }

        if (inputLink.isEmpty() || !settings.isOperational(inputLink.get())) {
            outputLink.get().disable();
            return;
        }

        outputLink.get().setPowerFrequencyPhase(
                PowerLossBlock.calculatePowerLoss(settings, inputLink.get()),
                calculateFrequency(settings, inputLink.get().getFrequency()),
                inputLink.get().getPhase());
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        final Optional<BlockDisplay> blockDisplay = getBlockDisplay(location, "comparator");
        blockDisplay.ifPresent(display -> display.setBlock(createComparatorBlockData(PersistentDataAPI.getString(display, Keys.FACING), powered)));
    }

    private static double calculateFrequency(@NotNull final Settings settings, final double frequency) {
        return frequency * settings.getFrequencyMultiplier();
    }

    private @NotNull BlockData createComparatorBlockData(@NotNull final String facing, final boolean powered) {
        return Material.COMPARATOR.createBlockData("[mode=" + settings.getComparatorVisual() + ",facing=" + facing + ",powered=" + Objects.toString(powered) + "]");
    }
}
