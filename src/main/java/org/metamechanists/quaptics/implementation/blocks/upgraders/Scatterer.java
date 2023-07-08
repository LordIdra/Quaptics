package org.metamechanists.quaptics.implementation.blocks.upgraders;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
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
import org.metamechanists.quaptics.implementation.blocks.attachments.UpgraderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.List;
import java.util.Optional;

public class Scatterer extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock, UpgraderBlock {
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f comparatorDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector3f comparatorOffset = new Vector3f(0.0F, 0.1F, 0.0F);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius()+0.01F, 0.075F, settings.getDisplayRadius()+0.01F);
    private final Vector3f concreteOffset = new Vector3f(0.0F, -0.05F, 0.0F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0.0F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final String modeVisual;

    public Scatterer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                     final Settings settings, final String modeVisual) {
        super(itemGroup, item, recipeType, recipe, settings);
        this.modeVisual = modeVisual;
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, @NotNull final Player player) {
        final BlockFace face = Transformations.yawToFace(player.getEyeLocation().getYaw());
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.ORANGE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateScale(glassDisplaySize, mainDisplayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(Transformations.adjustedScaleOffset(concreteDisplaySize, concreteOffset))
                .setBrightness(Utils.BRIGHTNESS_ON)
                .build());
        final BlockDisplay comparator = new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.COMPARATOR)
                .setBlockData(Material.COMPARATOR.createBlockData(
                        "[facing=" + face.name().toLowerCase()
                        + ",powered=false]"))
                .setTransformation(Transformations.adjustedScaleOffset(comparatorDisplaySize, comparatorOffset))
                .build();
        PersistentDataAPI.setString(comparator, Keys.FACING, face.name().toLowerCase());
        displayGroup.addDisplay("comparator", comparator);
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
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

        outputLink.get().setPowerAndFrequency(
                PowerLossBlock.calculatePowerLoss(settings, inputLink.get()),
                calculateUpgrade(settings, inputLink.get().getFrequency()));
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        final Optional<BlockDisplay> blockDisplay = getBlockDisplay(location, "comparator");
        blockDisplay.ifPresent(display -> display.setBlock(Material.COMPARATOR.createBlockData(
                "[mode=" + modeVisual
                        + ",facing=" + PersistentDataAPI.getString(display, Keys.FACING)
                        + ",powered=" + powered + "]")));
    }

    @Override
    public double calculateUpgrade(@NotNull final Settings settings, final double frequency) {
        return frequency * settings.getFrequencyMultiplier();
    }
}
