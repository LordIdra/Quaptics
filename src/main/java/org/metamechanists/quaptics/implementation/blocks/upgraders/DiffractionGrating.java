package org.metamechanists.quaptics.implementation.blocks.upgraders;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;

import java.util.List;
import java.util.Optional;


public class DiffractionGrating extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings DIFFRACTION_GRATING_1_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .maxPowerHidden(true)
            .minPower(360)
            .powerLoss(0.05)
            .minFrequency(200)
            .maxFrequency(1000)
            .frequencyMultiplier(1.8)
            .targetPhase(168)
            .targetPhaseSpread(60)
            .build();
    public static final Settings DIFFRACTION_GRATING_2_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .maxPowerHidden(true)
            .minPower(1600)
            .powerLoss(0.01)
            .minFrequency(6000)
            .maxFrequency(40000)
            .frequencyMultiplier(2.0)
            .targetPhase(267)
            .targetPhaseSpread(60)
            .build();

    public static final SlimefunItemStack DIFFRACTION_GRATING_1 = new SlimefunItemStack(
            "QP_DIFFRACTION_GRATING_1",
            Material.YELLOW_TERRACOTTA,
            "&cDiffraction Grating &4I",
            Lore.create(DIFFRACTION_GRATING_1_SETTINGS,
                    "&7● Increases the frequency of quaptic rays",
                    "&7● The closer the auxiliary input to the target phase, the",
                    "&7  greater the increase in frequency",
                    "&7● The operating power & frequency only apply to the main beam",
                    "&7● The target phase only applies to the auxiliary beam"));

    public static final SlimefunItemStack DIFFRACTION_GRATING_2 = new SlimefunItemStack(
            "QP_DIFFRACTION_GRATING_2",
            Material.YELLOW_TERRACOTTA,
            "&cDiffraction Grating &4II",
            Lore.create(DIFFRACTION_GRATING_2_SETTINGS,
                    "&7● Increases the frequency of quaptic rays",
                    "&7● The closer the auxiliary input to the target phase, the",
                    "&7  greater the increase in frequency",
                    "&7● The operating power & frequency only apply to the main beam",
                    "&7● The target phase only applies to the auxiliary beam"));

    private final Vector mainPointLocation = new Vector(0, 0, -0.5);
    private final Vector auxiliaryPointLocation = new Vector(0, 0.4, 0);
    private final Vector outputPointLocation = new Vector(0, 0, 0.5);

    public DiffractionGrating(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0;
    }
    @Override
    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {}
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.YELLOW_TERRACOTTA)
                        .facing(player.getFacing())
                        .size(0.2F, 0.2F, 1.0F))
                .add("auxiliary", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .facing(player.getFacing())
                        .size(0.15F, 0.4F, 0.15F)
                        .location(0, 0.2F, 0))
                .add("prism", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .facing(player.getFacing())
                        .size(0.4F)
                        .rotation(Math.PI/4))
                .buildAtBlockCenter(location);
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
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "prism", powered);
    }

    private static double calculateFrequency(@NotNull final Settings settings, final double frequency, final int phase) {
        final int phaseDifference = Math.abs(phase - settings.getTargetPhase());
        final double targetPhaseDifference = Math.max(settings.getTargetPhaseSpread() - phaseDifference, 0);
        final double targetPhaseProportion = targetPhaseDifference / settings.getTargetPhaseSpread();
        return frequency * (1 + (targetPhaseProportion * (settings.getFrequencyMultiplier() - 1)));
    }
}
