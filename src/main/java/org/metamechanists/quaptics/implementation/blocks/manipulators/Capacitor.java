package org.metamechanists.quaptics.implementation.blocks.manipulators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.implementation.CapacitorInfoPanel;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.models.transformations.TransformationMatrixBuilder;
import org.metamechanists.quaptics.utils.models.transformations.TransformationUtils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Capacitor extends ConnectedBlock implements InfoPanelBlock, PowerLossBlock {
    public static final Settings CAPACITOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.6F)
            .chargeCapacity(200)
            .emissionPower(3)
            .powerLoss(0.25)
            .build();
    public static final SlimefunItemStack CAPACITOR_1 = new SlimefunItemStack(
            "QP_CAPACITOR_1",
            Material.LIGHT_BLUE_CONCRETE,
            "&3Capacitor &bI",
            Lore.create(CAPACITOR_1_SETTINGS,
                    "&7● Stores charge",
                    "&7● Outputs at a constant power"));

    private static final Vector3f MAIN_GLASS_DISPLAY_SIZE = new Vector3f(0.60F);
    private static final Vector3f TIER_GLASS_DISPLAY_SIZE = new Vector3f(0.51F);
    private static final Vector3f MAX_CONCRETE_DISPLAY_SIZE = new Vector3f(0.49F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public Capacitor(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.60F;
    }
    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_GLASS_DISPLAY_SIZE)
                        .rotate(TransformationUtils.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("tierGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().glassMaterial)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(TIER_GLASS_DISPLAY_SIZE)
                        .rotate(TransformationUtils.PRISM_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_CONCRETE)
                .setBrightness(Utils.BRIGHTNESS_ON)
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public BlockInfoPanel createPanel(final Location location, final @NotNull ConnectionGroup group) {
        return new CapacitorInfoPanel(location, group.getId());
    }
    @Override
    public BlockInfoPanel getPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new CapacitorInfoPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        onPlaceInfoPanelBlock(event);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakInfoPanelBlock(location);
    }
    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        double charge = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE);
        charge = doCharge(location, charge);
        charge = doDischarge(location, charge);

        BlockStorageAPI.set(location, Keys.BS_CHARGE, charge);
        doEmission(location, charge);
        updateConcreteTransformation(location);
        setPanelHidden(group, charge == 0);
        updatePanel(group);
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Link> inputLink = getLink(location, "input");
        if (inputLink.isEmpty()) {
            BlockStorageAPI.set(location, Keys.BS_CHARGE_RATE, 0);
            return;
        }

        final double chargeRate = settings.isOperational(inputLink)
                ? PowerLossBlock.calculatePowerLoss(settings, inputLink.get().getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND)
                : 0;
        BlockStorageAPI.set(location, Keys.BS_CHARGE_RATE, chargeRate);
    }

    private double doCharge(final Location location, final double charge) {
        final double chargeRate = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE_RATE);
        return settings.stepCharge(charge, chargeRate);
    }
    private double doDischarge(final Location location, final double charge) {
        final Optional<Link> outputLink = getLink(location, "output");
        if (outputLink.isEmpty()) {
            return charge;
        }

        final double newCharge = settings.stepDischarge(charge);
        BlockStorageAPI.set(location, Keys.BS_CHARGE, charge);
        return newCharge;
    }
    private void doEmission(final Location location, final double charge) {
        final Optional<Link> outputLink = getLink(location, "output");
        if (outputLink.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location, "input");
        if (inputLink.isPresent() && charge == 0) {
            final double outputPower = PowerLossBlock.calculatePowerLoss(settings, inputLink.get().getPower());
            if (outputPower < getSettings().getEmissionPower()) {
                outputLink.get().setPower(outputPower);
                return;
            }
        }

        outputLink.get().setPowerFrequency(
                (charge > settings.getEmissionPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND)
                        ? settings.getEmissionPower()
                        : 0,
                0);
    }
    private @NotNull Matrix4f getConcreteTransformationMatrix(final double charge) {
        return new TransformationMatrixBuilder()
                .scale(new Vector3f(MAX_CONCRETE_DISPLAY_SIZE).mul((float)(charge/settings.getChargeCapacity())))
                .rotate(TransformationUtils.PRISM_ROTATION)
                .buildForBlockDisplay();
    }
    private void updateConcreteTransformation(final Location location) {
        final double charge = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE);
        final Optional<Display> concreteDisplay = getDisplay(location, "concrete");
        concreteDisplay.ifPresent(display -> display.setTransformationMatrix(getConcreteTransformationMatrix(charge)));
    }
}
