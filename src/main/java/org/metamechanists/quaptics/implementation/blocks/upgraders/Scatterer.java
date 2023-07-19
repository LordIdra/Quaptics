package org.metamechanists.quaptics.implementation.blocks.upgraders;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
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
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Scatterer extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings SCATTERER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .maxPowerHidden(true)
            .minPower(40)
            .powerLoss(0.06)
            .minFrequency(5)
            .maxFrequency(10)
            .frequencyMultiplier(1.4)
            .comparatorVisual("compare")
            .build();
    public static final Settings SCATTERER_2_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .maxPowerHidden(true)
            .minPower(360)
            .powerLoss(0.03)
            .minFrequency(40)
            .maxFrequency(200)
            .frequencyMultiplier(1.8)
            .comparatorVisual("compare")
            .build();
    public static final Settings SCATTERER_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .maxPowerHidden(true)
            .minPower(1800)
            .powerLoss(0.03)
            .minFrequency(40000)
            .maxFrequency(160000)
            .frequencyMultiplier(1.5)
            .comparatorVisual("compare")
            .build();

    public static final SlimefunItemStack SCATTERER_1 = new SlimefunItemStack(
            "QP_SCATTERER_1",
            Material.CYAN_STAINED_GLASS,
            "&cScatterer &4I",
            Lore.create(SCATTERER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));
    public static final SlimefunItemStack SCATTERER_2 = new SlimefunItemStack(
            "QP_SCATTERER_2",
            Material.CYAN_STAINED_GLASS,
            "&cScatterer &4II",
            Lore.create(SCATTERER_2_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));
    public static final SlimefunItemStack SCATTERER_3 = new SlimefunItemStack(
            "QP_SCATTERER_3",
            Material.CYAN_STAINED_GLASS,
            "&cScatterer &4III",
            Lore.create(SCATTERER_3_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));


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
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.CYAN_STAINED_GLASS)
                        .facing(player.getFacing())
                        .size(0.4F)
                        .rotation(Math.PI / 4))
                .add("prism", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .facing(player.getFacing())
                        .rotation(Math.PI/4)
                        .size(0.3F, 0.1F, 0.3F))
                .add("comparator", new ModelCuboid()
                        .block(createComparatorBlockData(false))
                        .facing(player.getFacing())
                        .location(0, 0.1F, 0)
                        .size(0.2F, 0.1F, 0.2F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
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
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        final Optional<BlockDisplay> blockDisplay = getBlockDisplay(location, "comparator");
        blockDisplay.ifPresent(display -> display.setBlock(createComparatorBlockData(powered)));
    }

    private static double calculateFrequency(@NotNull final Settings settings, final double frequency) {
        return frequency * settings.getFrequencyMultiplier();
    }

    private @NotNull BlockData createComparatorBlockData(final boolean powered) {
        return Material.COMPARATOR.createBlockData("[mode=" + settings.getComparatorVisual() + ",powered=" + Objects.toString(powered) + "]");
    }
}
