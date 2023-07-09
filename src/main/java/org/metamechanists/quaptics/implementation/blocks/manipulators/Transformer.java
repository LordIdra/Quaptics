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
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.List;
import java.util.Optional;


public class Transformer extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock {
    public static final Settings TRANSFORMER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .connectionRadius(0.48F)
            .emissionPower(10)
            .build();
    public static final SlimefunItemStack TRANSFORMER_1 = new SlimefunItemStack(
            "QP_TRANSFORMER_1",
            Material.CYAN_TERRACOTTA,
            "&9Transformer &bI",
            Lore.create(TRANSFORMER_1_SETTINGS,
                    "&7● Drops the power of a quaptic ray"));

    private final Vector3f mainDisplaySize = new Vector3f(0.3F, 0.3F, 0.8F);
    private final Vector3f coilDisplaySize = new Vector3f(0.2F, 0.6F, 0.2F);
    private final Vector3f firstCoilDisplayOffset = new Vector3f(0.0F, 0.0F, 0.3F);
    private final Vector3f secondCoilDisplayOffset = new Vector3f(0.0F, 0.0F, -0.3F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());

    public Transformer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.CYAN_TERRACOTTA)
                .setTransformation(Transformations.adjustedScale(mainDisplaySize))
                .build());
        displayGroup.addDisplay("coil1", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(Transformations.adjustedScaleOffset(coilDisplaySize, firstCoilDisplayOffset))
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setViewRange(Utils.VIEW_RANGE_ON)
                .build());
        displayGroup.addDisplay("coil2", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(Transformations.adjustedScaleOffset(coilDisplaySize, secondCoilDisplayOffset))
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setViewRange(Utils.VIEW_RANGE_ON)
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

        if (inputLink.isEmpty()) {
            outputLink.get().disable();
            return;
        }

        outputLink.get().setPowerAndFrequency(settings.getEmissionPower(), inputLink.get().getFrequency());
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "coil1", powered);
        brightnessAnimation(location, "coil2", powered);
    }
}
