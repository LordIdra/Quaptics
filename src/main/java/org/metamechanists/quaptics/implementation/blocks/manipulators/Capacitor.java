package org.metamechanists.quaptics.implementation.blocks.manipulators;

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
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.ChargeHolder;
import org.metamechanists.quaptics.implementation.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.implementation.CapacitorInfoPanel;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelDiamond;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Capacitor extends ConnectedBlock implements InfoPanelBlock, PowerLossBlock, ChargeHolder {
    public static final Settings CAPACITOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .chargeCapacity(800)
            .outputPower(3)
            .powerLoss(0.25)
            .build();
    public static final Settings CAPACITOR_2_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .chargeCapacity(1200)
            .outputPower(35)
            .powerLoss(0.16)
            .build();
    public static final Settings CAPACITOR_3_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .chargeCapacity(16000)
            .outputPower(400)
            .powerLoss(0.08)
            .build();
    public static final Settings CAPACITOR_4_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .chargeCapacity(200000)
            .outputPower(4500)
            .powerLoss(0.04)
            .build();

    public static final SlimefunItemStack CAPACITOR_1 = new SlimefunItemStack(
            "QP_CAPACITOR_1",
            Material.LIGHT_BLUE_CONCRETE,
            "&9Capacitor &8I",
            Lore.create(CAPACITOR_1_SETTINGS,
                    "&7● Stores charge"));
    public static final SlimefunItemStack CAPACITOR_2 = new SlimefunItemStack(
            "QP_CAPACITOR_2",
            Material.LIGHT_BLUE_CONCRETE,
            "&9Capacitor &8II",
            Lore.create(CAPACITOR_2_SETTINGS,
                    "&7● Stores charge"));
    public static final SlimefunItemStack CAPACITOR_3 = new SlimefunItemStack(
            "QP_CAPACITOR_3",
            Material.LIGHT_BLUE_CONCRETE,
            "&9Capacitor &8III",
            Lore.create(CAPACITOR_3_SETTINGS,
                    "&7● Stores charge"));
    public static final SlimefunItemStack CAPACITOR_4 = new SlimefunItemStack(
            "QP_CAPACITOR_4",
            Material.LIGHT_BLUE_CONCRETE,
            "&9Capacitor &8IV",
            Lore.create(CAPACITOR_4_SETTINGS,
                    "&7● Stores charge"));

    private static final float MAX_CONCRETE_DISPLAY_SIZE = 0.78F;

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
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("concrete", new ModelDiamond()
                        .material(Material.LIGHT_BLUE_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_ON))
                .add("mainGlass", new ModelDiamond()
                        .material(Material.GLASS)
                        .size(0.85F))
                .add("tierGlass", new ModelDiamond()
                        .material(settings.getTier().glassMaterial)
                        .size(0.80F))
                .buildAtBlockCenter(location);
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
    protected boolean isTicker() {
        return true;
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
    @SuppressWarnings("unused")
    @Override
    public void onTick10(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        double charge = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE);
        final double initialCharge = charge;
        charge = doCharge(location, charge);
        charge = doDischarge(location, charge);

        if (Utils.equal(initialCharge, charge)) {
            return;
        }

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
                ? PowerLossBlock.calculatePowerLoss(settings, inputLink.get().getPower() * ((double) QuapticTicker.INTERVAL_TICKS_10 / QuapticTicker.TICKS_PER_SECOND))
                : 0;
        BlockStorageAPI.set(location, Keys.BS_CHARGE_RATE, chargeRate);
    }

    private double doCharge(final Location location, final double charge) {
        final double chargeRate = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE_RATE);
        return stepCharge(settings, charge, chargeRate);
    }
    private double doDischarge(final Location location, final double charge) {
        final Optional<Link> outputLink = getLink(location, "output");
        if (outputLink.isEmpty()) {
            return charge;
        }

        final double newCharge = stepDischarge(settings, charge);
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
            if (outputPower < getSettings().getOutputPower()) {
                outputLink.get().setPower(outputPower);
                return;
            }
        }

        outputLink.get().setPowerFrequency(
                (charge > settings.getOutputPower() * ((double) QuapticTicker.INTERVAL_TICKS_10 / QuapticTicker.TICKS_PER_SECOND))
                        ? settings.getOutputPower()
                        : 0,
                0);
    }
    private @NotNull Matrix4f getConcreteTransformationMatrix(final double charge) {
        return new ModelDiamond()
                .size(MAX_CONCRETE_DISPLAY_SIZE * (float)(charge/settings.getChargeCapacity()))
                .getMatrix();
    }
    private void updateConcreteTransformation(final Location location) {
        final double charge = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE);
        final Optional<Display> concreteDisplay = getDisplay(location, "concrete");
        concreteDisplay.ifPresent(display -> display.setTransformationMatrix(getConcreteTransformationMatrix(charge)));
    }
}
