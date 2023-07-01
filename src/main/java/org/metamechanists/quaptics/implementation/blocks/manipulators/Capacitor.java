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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.implementation.panels.CapacitorPanel;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Objects;

public class Capacitor extends ConnectedBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Vector3f mainGlassDisplaySize = new Vector3f(settings.getDisplayRadius()*2.0F);
    private final Vector3f tierGlassDisplaySize = new Vector3f(settings.getDisplayRadius()*1.7F);
    private final Vector3f maxConcreteDisplaySize = new Vector3f(settings.getDisplayRadius()*1.65F);
    private final Vector3f displayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());

    public Capacitor(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(mainGlassDisplaySize, displayRotation))
                .build());
        displayGroup.addDisplay("tierGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().glassMaterial)
                .setTransformation(Transformations.adjustedRotateAndScale(tierGlassDisplaySize, displayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_CONCRETE)
                .setTransformation(Transformations.adjustedRotateAndScale(new Vector3f(), displayRotation))
                .setBrightness(CONCRETE_BRIGHTNESS)
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputPointLocation)));
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

    public static @Nullable PanelId getPanelId(final Location location) {
        final String stringID = BlockStorage.getLocationInfo(location, Keys.BS_PANEL_ID);
        return stringID == null ? null : new PanelId(stringID);
    }

    private static void setPanelID(final Location location, @NotNull final PanelId id) {
        BlockStorage.addBlockInfo(location, Keys.BS_PANEL_ID, id.toString());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final ConnectionGroup group = getGroup(location);
        if (group == null) {
            return;
        }
        setPanelID(location, new CapacitorPanel(location, group.getId()).getId());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final BlockBreakEvent event) {
        super.onBreak(event);
        final Location location = event.getBlock().getLocation();
        final PanelId panelId = getPanelId(location);
        if (panelId == null) {
            return;
        }

        final Panel panel = panelId.get();
        if (panel == null) {
            return;
        }

        panel.remove();
    }

    private static void updatePanel(@NotNull final ConnectionGroup group) {
        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final PanelId id = getPanelId(location);
        if (id != null) {
            final CapacitorPanel panel = new CapacitorPanel(id, group.getId());
            panel.update();
        }
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final ConnectionPointOutput output = group.getOutput("output");
        if (output == null) {
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final double chargeRate = getChargeRate(location);
        double charge = getCharge(group.getLocation());

        if (chargeRate != 0) {
            charge = settings.stepCharge(charge, chargeRate);
        }

        if (output.hasLink()) {
            final double dischargeRate = settings.getEmissionPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND;
            charge = settings.stepCharge(charge, -dischargeRate);
            if (charge > dischargeRate) {
                output.getLink().setAttributes(settings.getEmissionPower(), 0, 0, true);
            } else {
                output.disableLinkIfExists();
            }
        }

        final Display concreteDisplay = getDisplay(group.getLocation(), "concrete");
        if (concreteDisplay != null) {
            concreteDisplay.setTransformationMatrix(
                    Transformations.adjustedRotateAndScale(new Vector3f(maxConcreteDisplaySize)
                            .mul((float)(charge/settings.getCapacity())), displayRotation));
        }

        updatePanel(group);
        setCharge(group.getLocation(), charge);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        final ConnectionPointInput input = group.getInput("input");

        if (input == null) {
            return;
        }

        if (doBurnoutCheck(group, input)) {
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        setChargeRate(location, input.isLinkEnabled() ? input.getLink().getPower() / QuapticTicker.QUAPTIC_TICKS_PER_SECOND : 0);
    }
}
