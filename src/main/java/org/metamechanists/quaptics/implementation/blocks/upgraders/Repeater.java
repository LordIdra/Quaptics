package org.metamechanists.quaptics.implementation.blocks.upgraders;

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
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
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
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Repeater extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings REPEATER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .maxPowerHidden(true)
            .minPower(20)
            .powerLoss(0.08)
            .minFrequency(0)
            .maxFrequency(5)
            .frequencyStep(2)
            .repeaterDelay(1)
            .build();

    public static final Settings REPEATER_2_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .maxPowerHidden(true)
            .minPower(190)
            .powerLoss(0.06)
            .minFrequency(10)
            .maxFrequency(40)
            .frequencyStep(12)
            .repeaterDelay(1)
            .build();

    public static final Settings REPEATER_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .maxPowerHidden(true)
            .minPower(1100)
            .powerLoss(0.04)
            .minFrequency(1000)
            .maxFrequency(6000)
            .frequencyStep(2400)
            .repeaterDelay(1)
            .build();

    public static final SlimefunItemStack REPEATER_1 = new SlimefunItemStack(
            "QP_REPEATER_1",
            Material.RED_STAINED_GLASS,
            "&cRepeater &4I",
            Lore.create(REPEATER_1_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));
    public static final SlimefunItemStack REPEATER_2 = new SlimefunItemStack(
            "QP_REPEATER_2",
            Material.RED_STAINED_GLASS,
            "&cRepeater &4II",
            Lore.create(REPEATER_2_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));
    public static final SlimefunItemStack REPEATER_3 = new SlimefunItemStack(
            "QP_REPEATER_3",
            Material.RED_STAINED_GLASS,
            "&cRepeater &4III",
            Lore.create(REPEATER_3_SETTINGS,
                    "&7● Increases the frequency of a quaptic ray"));

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public Repeater(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.40F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.RED_STAINED_GLASS)
                        .facing(player.getFacing())
                        .size(0.4F)
                        .rotation(Math.PI / 4))
                .add("prism", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .facing(player.getFacing())
                        .rotation(Math.PI/4)
                        .size(0.3F, 0.1F, 0.3F))
                .add("repeater", new ModelCuboid()
                        .block(createRepeaterBlockData(false))
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
        final Optional<BlockDisplay> blockDisplay = getBlockDisplay(location, "repeater");
        blockDisplay.ifPresent(display -> display.setBlock(createRepeaterBlockData(powered)));
    }

    private static double calculateFrequency(@NotNull final Settings settings, final double frequency) {
        return frequency + settings.getFrequencyStep();
    }

    private @NotNull BlockData createRepeaterBlockData(final boolean powered) {
        return Material.REPEATER.createBlockData("[delay=" + settings.getRepeaterDelay() + ",powered=" + Objects.toString(powered) + "]");
    }
}
