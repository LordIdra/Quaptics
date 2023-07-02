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
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.UpgraderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.List;
import java.util.Optional;

public class Repeater extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock, UpgraderBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f repeaterDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector3f repeaterOffset = new Vector3f(0.0F, 0.1F, 0.0F);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius()+0.01F, 0.075F, settings.getDisplayRadius()+0.01F);
    private final Vector3f concreteOffset = new Vector3f(0.0F, -0.05F, 0.0F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0.0F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final int delayVisual;

    public Repeater(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                    final Settings settings, final int delayVisual) {
        super(itemGroup, item, recipeType, recipe, settings);
        this.delayVisual = delayVisual;
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, @NotNull final Player player) {
        final BlockFace face = Transformations.yawToFace(player.getEyeLocation().getYaw());
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.RED_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, mainDisplayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(Transformations.adjustedScaleAndOffset(concreteDisplaySize, concreteOffset))
                .setBrightness(CONCRETE_BRIGHTNESS)
                .build());
        final BlockDisplay repeater = new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.REPEATER)
                .setBlockData(Material.REPEATER.createBlockData(
                        "[delay=" + delayVisual
                        + ",facing=" + face.name().toLowerCase()
                        + ",powered=false]"))
                .setTransformation(Transformations.adjustedScaleAndOffset(repeaterDisplaySize, repeaterOffset))
                .build();
        PersistentDataAPI.setString(repeater, Keys.FACING, face.name().toLowerCase());
        displayGroup.addDisplay("repeater", repeater);
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location.get(), "input");
        onPoweredAnimation(location.get(), settings.isOperational(inputLink));
        if (inputLink.isEmpty()) {
            return;
        }

        final Optional<Link> outputLink = getLink(location.get(), "output");
        if (outputLink.isEmpty()) {
            return;
        }

        if (!settings.isOperational(inputLink.get())) {
            outputLink.get().setPower(0);
            return;
        }

        outputLink.get().setPowerAndFrequency(
                calculatePowerLoss(settings, inputLink.get()),
                calculateUpgrade(settings, inputLink.get().getFrequency()));
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        final Optional<BlockDisplay> blockDisplay = getBlockDisplay(location, "repeater");
        blockDisplay.ifPresent(display -> display.setBlock(Material.REPEATER.createBlockData(
                "[delay=" + delayVisual
                + ",facing=" + PersistentDataAPI.getString(display, Keys.FACING)
                + ",powered=" + powered + "]")));
    }

    @Override
    public double calculateUpgrade(@NotNull final Settings settings, final double frequency) {
        return frequency + settings.getFrequencyStep();
    }
}
