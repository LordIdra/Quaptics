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
import org.metamechanists.quaptics.implementation.blocks.attachments.PanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.panels.info.implementation.CapacitorInfoPanel;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Capacitor extends ConnectedBlock implements PanelBlock, PowerLossBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Vector3f mainGlassDisplaySize = new Vector3f(settings.getDisplayRadius()*2.0F);
    private final Vector3f tierGlassDisplaySize = new Vector3f(settings.getDisplayRadius()*1.7F);
    private final Vector3f maxConcreteDisplaySize = new Vector3f(settings.getDisplayRadius()*1.65F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());

    public Capacitor(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GLASS)
                .setTransformation(Transformations.adjustedRotateScale(mainGlassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
        displayGroup.addDisplay("tierGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().glassMaterial)
                .setTransformation(Transformations.adjustedRotateScale(tierGlassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_CONCRETE)
                .setTransformation(Transformations.none())
                .setBrightness(CONCRETE_BRIGHTNESS)
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public BlockInfoPanel createPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new CapacitorInfoPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = getGroup(location);
        optionalGroup.ifPresent(group -> PanelBlock.setPanelId(location, new CapacitorInfoPanel(location, group.getId()).getId()));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        PanelBlock.getPanelId(location)
                .flatMap(InfoPanelId::get)
                .ifPresent(InfoPanelContainer::remove);
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        double charge = BlockStorageAPI.getDouble(location.get(), Keys.BS_CHARGE);
        charge = doCharge(location.get(), charge);
        charge = doDischarge(location.get(), charge);

        BlockStorageAPI.set(location.get(), Keys.BS_CHARGE, charge);
        doEmission(location.get(), charge);
        updateConcreteTransformation(location.get());
        setPanelHidden(group, charge == 0);
        updatePanel(group);
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
        if (inputLink.isEmpty()) {
            BlockStorageAPI.set(location.get(), Keys.BS_CHARGE_RATE, 0);
            return;
        }

        final double chargeRate = settings.isOperational(inputLink)
                ? PowerLossBlock.calculatePowerLoss(settings, inputLink.get().getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND)
                : 0;
        BlockStorageAPI.set(location.get(), Keys.BS_CHARGE_RATE, chargeRate);
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

        outputLink.get().setPowerAndFrequency(
                (charge > settings.getEmissionPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND)
                        ? settings.getEmissionPower()
                        : 0,
                0);
    }

    private Matrix4f getConcreteTransformationMatrix(final double charge) {
        return Transformations.adjustedRotateScale(
                new Vector3f(maxConcreteDisplaySize).mul((float)(charge/settings.getCapacity())),
                Transformations.GENERIC_ROTATION_ANGLES);
    }

    private void updateConcreteTransformation(final Location location) {
        final double charge = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE);
        final Optional<Display> concreteDisplay = getDisplay(location, "concrete");
        concreteDisplay.ifPresent(display -> display.setTransformationMatrix(getConcreteTransformationMatrix(charge)));
    }
}
