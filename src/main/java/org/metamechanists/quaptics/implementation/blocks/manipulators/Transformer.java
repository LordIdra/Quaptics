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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;

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
            Material.BLACK_TERRACOTTA,
            "&9Transformer &bI",
            Lore.create(TRANSFORMER_1_SETTINGS,
                    "&7● Drops the power of a quaptic ray",
                    "&7● Excess input power is wasted"));

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
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.LIGHT_GRAY_CONCRETE)
                        .facing(player.getFacing())
                        .size(0.30F, 0.30F, 0.80F))
                .add("coil1", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .facing(player.getFacing())
                        .location(0.0F, 0.0F, 0.25F)
                        .size(0.20F, 0.60F, 0.20F))
                .add("coil2", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .facing(player.getFacing())
                        .location(0.0F, 0.0F, -0.25F)
                        .size(0.20F, 0.60F, 0.20F))
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
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "coil1", powered);
        brightnessAnimation(location, "coil2", powered);
    }
}
