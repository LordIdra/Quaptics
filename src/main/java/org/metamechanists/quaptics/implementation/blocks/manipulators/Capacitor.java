package org.metamechanists.quaptics.implementation.blocks.manipulators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
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
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PanelBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.panels.BlockPanel;
import org.metamechanists.quaptics.implementation.blocks.panels.CapacitorPanel;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Capacitor extends ConnectedBlock implements PanelBlock {
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
                .setTransformation(Transformations.adjustedRotateAndScale(mainGlassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
        displayGroup.addDisplay("tierGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().glassMaterial)
                .setTransformation(Transformations.adjustedRotateAndScale(tierGlassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
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
                new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public BlockPanel createPanel(final PanelId panelId, final ConnectionGroupId groupId) {
        return new CapacitorPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = getGroup(location);
        optionalGroup.ifPresent(group -> setPanelId(location, new CapacitorPanel(location, group.getId()).getId()));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        getPanelId(location)
                .flatMap(PanelId::get)
                .ifPresent(Panel::remove);
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        doCharge(location.get());
        doDischarge(location.get(), group);
        updateConcreteTransformation(location.get());
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
            setChargeRate(location.get(), 0);
            return;
        }

        setChargeRate(location.get(), settings.isOperational(inputLink) ? inputLink.get().getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND : 0);
    }

    private void doCharge(final Location location) {
        final double chargeRate = getChargeRate(location);
        final double charge = getCharge(location);
        setCharge(location, chargeRate == 0 ? charge : settings.stepCharge(charge, chargeRate));
    }

    private void doDischarge(final Location location, @NotNull final ConnectionGroup group) {
        final Optional<ConnectionPointOutput> output = group.getOutput("output");
        if (output.isEmpty()) {
            return;
        }

        final Optional<Link> outputLink = output.get().getLink();
        if (outputLink.isEmpty()) {
            return;
        }

        final double charge = settings.stepDischarge(getCharge(location));
        outputLink.get().setPowerAndFrequency(charge > 0 ? settings.getEmissionPower() : 0, 0);
        setCharge(location, charge);
    }

    private Matrix4f getConcreteTransformationMatrix(final double charge) {
        return Transformations.adjustedRotateAndScale(
                new Vector3f(maxConcreteDisplaySize).mul((float)(charge/settings.getCapacity())),
                Transformations.GENERIC_ROTATION_ANGLES);
    }

    private void updateConcreteTransformation(final Location location) {
        final double charge = getCharge(location);
        final Optional<Display> concreteDisplay = getDisplay(location, "concrete");
        concreteDisplay.ifPresent(display -> display.setTransformationMatrix(getConcreteTransformationMatrix(charge)));
    }

    public static double getCharge(final Location location) {
        final String chargeString = BlockStorage.getLocationInfo(location, Keys.BS_CHARGE);
        return chargeString == null ? 0 : Double.parseDouble(chargeString);
    }

    private static void setCharge(final Location location, final double charge) {
        BlockStorage.addBlockInfo(location, Keys.BS_CHARGE, Objects.toString(charge));
    }

    private static double getChargeRate(final Location location) {
        final String chargeRateString = BlockStorage.getLocationInfo(location, Keys.BS_CHARGE_RATE);
        return chargeRateString == null ? 0 : Double.parseDouble(chargeRateString);
    }

    private static void setChargeRate(final Location location, final double chargeRate) {
        BlockStorage.addBlockInfo(location, Keys.BS_CHARGE_RATE, String.valueOf(chargeRate));
    }
}
