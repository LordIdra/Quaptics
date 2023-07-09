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
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.UpgraderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class Polariser extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock, UpgraderBlock {
    public static final Settings POLARISER_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .minPower(15)
            .powerLoss(0.05)
            .minFrequency(0.0)
            .maxFrequency(0.3)
            .frequencyStep(0.1)
            .repeaterDelay(1)
            .build();
    public static final SlimefunItemStack POLARISER_1 = new SlimefunItemStack(
            "QP_AUGMENTER_1",
            Material.YELLOW_TERRACOTTA,
            "&cPolariser &4I",
            Lore.create(POLARISER_1_SETTINGS,
                    "&7● Increases the Phase of a quaptic ray",
                    "&7● Requires a Phase Crystal to operate",
                    "&7● &eRight Click &7with a phase crystal to insert it"));

    private static final Vector3f PRISM_SIZE = new Vector3f(0.30F);
    private static final Vector3f MAIN_TUBE_SIZE = new Vector3f(0.30F, 0.30F, 0.90F);
    private static final Vector3f AUXILIARY_TUBE_SIZE = new Vector3f(0.40F, 0.15F, 0.15F);
    private static final Vector3f AUXILIARY_TUBE_OFFSET = new Vector3f(0.20F, 0.0F, 0.0F);
    private static final Vector mainInputLocation = new Vector(0.0F, 0.0F, -0.50F);
    private static final Vector auxiliaryInputLocation = new Vector(0.50F, 0.0F, 0.0F);
    private static final Vector outputLocation = new Vector(0.0F, 0.0F, 0.50F);

    public Polariser(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, @NotNull final Player player) {
        final BlockFace face = Transformations.yawToFace(player.getEyeLocation().getYaw());
        //displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
        //        .setMaterial(Material.RED_STAINED_GLASS)
        //        .setTransformation(Transformations.adjustedRotateScale(glassDisplaySize, mainDisplayRotation))
        //        .build());
        //displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
        //        .setMaterial(settings.getTier().concreteMaterial)
        //        .setTransformation(Transformations.adjustedScaleOffset(concreteDisplaySize, concreteOffset))
        //        .setBrightness(Utils.BRIGHTNESS_ON)
        //        .build());
        //final BlockDisplay repeater = new BlockDisplayBuilder(location.toCenterLocation())
        //        .setMaterial(Material.REPEATER)
        //        .setBlockData(createRepeaterBlockData(face.name().toLowerCase(), false))
        //        .setTransformation(Transformations.adjustedScaleOffset(repeaterDisplaySize, REPEATER_OFFSET))
        //        .build();
        //PersistentDataAPI.setString(repeater, Keys.FACING, face.name().toLowerCase());
        //displayGroup.addDisplay("repeater", repeater);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "mainInput", formatPointLocation(player, location, mainInputLocation)),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "auxiliaryInput", formatPointLocation(player, location, auxiliaryInputLocation)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputLocation)));
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
        final Optional<BlockDisplay> blockDisplay = getBlockDisplay(location, "repeater");
        blockDisplay.ifPresent(display -> display.setBlock(createRepeaterBlockData(PersistentDataAPI.getString(display, Keys.FACING), powered)));
    }

    @Override
    public double calculateUpgrade(@NotNull final Settings settings, final double frequency) {
        return frequency + settings.getFrequencyStep();
    }

    private @NotNull BlockData createRepeaterBlockData(@NotNull final String facing, final boolean powered) {
        return Material.REPEATER.createBlockData("[delay=" + settings.getRepeaterDelay() + ",facing=" + facing + ",powered=" + Objects.toString(powered) + "]");
    }
}
