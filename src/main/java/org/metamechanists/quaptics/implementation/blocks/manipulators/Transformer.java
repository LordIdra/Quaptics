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

import java.util.List;
import java.util.Optional;


public class Transformer extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings TRANSFORMER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .emissionPower(10)
            .minPower(10)
            .build();
    public static final SlimefunItemStack TRANSFORMER_1 = new SlimefunItemStack(
            "QP_TRANSFORMER_1",
            Material.LIGHT_GRAY_TERRACOTTA,
            "&9Transformer &bI",
            Lore.create(TRANSFORMER_1_SETTINGS,
                    "&7● Drops the power of a quaptic ray",
                    "&7● Excess input power is wasted"));

    private static final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.30F, 0.30F, 0.80F);
    private static final Vector3f COIL_DISPLAY_SIZE = new Vector3f(0.20F, 0.60F, 0.20F);
    private static final Vector3f FIRST_COIL_DISPLAY_OFFSET = new Vector3f(0.0F, 0.0F, 0.25F);
    private static final Vector3f SECOND_COIL_DISPLAY_OFFSET = new Vector3f(0.0F, 0.0F, -0.25F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public Transformer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.55F;
    }
    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_GRAY_TERRACOTTA)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_DISPLAY_SIZE)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("coil1", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setViewRange(Utils.VIEW_RANGE_ON)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(COIL_DISPLAY_SIZE)
                        .rotate(FIRST_COIL_DISPLAY_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("coil2", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setViewRange(Utils.VIEW_RANGE_ON)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(COIL_DISPLAY_SIZE)
                        .rotate(SECOND_COIL_DISPLAY_OFFSET)
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

        final Optional<Link> outputLink = getLink(location, "output");
        final Optional<Link> inputLink = getLink(location, "input");

        onPoweredAnimation(location, settings.isOperational(inputLink));

        if (outputLink.isEmpty()) {
            return;
        }

        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            outputLink.get().disable();
            return;
        }

        outputLink.get().setPowerFrequencyPhase(
                settings.getEmissionPower(),
                inputLink.get().getFrequency(),
                inputLink.get().getPhase());
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "coil1", powered);
        brightnessAnimation(location, "coil2", powered);
    }
}
