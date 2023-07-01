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
import org.bukkit.entity.Display;
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
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.List;
import java.util.Optional;

public class Scatterer extends ConnectedBlock implements FrequencyUpgrader {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f comparatorDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector3f comparatorOffset = new Vector3f(0.0F, 0.1F, 0.0F);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius()+0.01F, 0.075F, settings.getDisplayRadius()+0.01F);
    private final Vector3f concreteOffset = new Vector3f(0.0F, -0.05F, 0.0F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0.0F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final String modeVisual;

    public Scatterer(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                     final Settings settings, final String modeVisual) {
        super(group, item, recipeType, recipe, settings);
        this.modeVisual = modeVisual;
    }

    private void setComparatorPowered(final Location location, final boolean powered) {
        final Optional<Display> display = getDisplay(location, "comparator");
        if (display.isEmpty() || !(display.get() instanceof final BlockDisplay blockDisplay)) {
            return;
        }

        blockDisplay.setBlock(Material.COMPARATOR.createBlockData(
                "[mode=" + modeVisual
                + ",facing=" + PersistentDataAPI.getString(blockDisplay, Keys.FACING)
                + ",powered=" + powered + "]"));
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, @NotNull final Player player) {
        final BlockFace face = Transformations.yawToFace(player.getEyeLocation().getYaw());
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.ORANGE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, mainDisplayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(Transformations.adjustedScaleAndOffset(concreteDisplaySize, concreteOffset))
                .setBrightness(CONCRETE_BRIGHTNESS)
                .build());
        final BlockDisplay comparator = new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.COMPARATOR)
                .setBlockData(Material.COMPARATOR.createBlockData(
                        "[facing=" + face.name().toLowerCase()
                        + ",powered=false]"))
                .setTransformation(Transformations.adjustedScaleAndOffset(comparatorDisplaySize, comparatorOffset))
                .build();
        PersistentDataAPI.setString(comparator, Keys.FACING, face.name().toLowerCase());
        displayGroup.addDisplay("comparator", comparator);
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        final Optional<ConnectionPointInput> input = group.getInput("input");
        final Optional<ConnectionPointOutput> output = group.getOutput("output");
        final Optional<Location> location = group.getLocation();
        if (input.isEmpty() || output.isEmpty() || location.isEmpty()) {
            return;
        }

        if (doBurnoutCheck(group, input.get())) {
            return;
        }

        doDisplayBrightnessCheck(location.get(), "concrete", false);
        setComparatorPowered(location.get(), input.get().isLinkEnabled() && upgradeFrequency(settings, input.get().getLink().get()));

        if (output.get().getLink().isEmpty()) {
            return;
        }

        if (!input.get().isLinkEnabled()) {
            output.get().disableLinkIfExists();
            return;
        }

        final Link link = output.get().getLink().get();
        link.setAttributes(
                settings.powerLoss(link.getPower()),
                settings.multiplyFrequency(link.getFrequency()),
                link.getPhase(),
                true);
    }
}
