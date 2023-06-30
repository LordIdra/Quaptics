package org.metamechanists.quaptics.implementation.base;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import lombok.Builder;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticStorage;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.List;
import java.util.Objects;

public abstract class ConnectedBlock extends DisplayGroupTickerBlock {
    protected static final Display.Brightness BRIGHTNESS_ON = new Display.Brightness(13, 0);
    protected static final Display.Brightness BRIGHTNESS_OFF = new Display.Brightness(5, 0);
    protected static final int VIEW_RANGE_ON = 64;
    protected static final int VIEW_RANGE_OFF = 0;

    @Getter
    protected final Settings settings;

    protected ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(group, item, recipeType, recipe);
        this.settings = settings;
        addItemHandler(onUse());
    }

    public BlockUseHandler onUse() {
        return event -> {
            if (!event.getPlayer().isSneaking()) {
                return;
            }

            final Block block = event.getClickedBlock().orElse(null);
            if (block == null) {
                return;
            }

            final ConnectionGroup group = getGroup(block.getLocation());
            final boolean isAnyPanelHidden = group.getPoints().values().stream().anyMatch(
                    point -> point.get().getPointPanel().isPanelHidden());

            group.getPoints().values().forEach(pointID -> pointID.get().getPointPanel().setPanelHidden(!isAnyPanelHidden));

        };
    }

    protected ConnectionGroup getGroup(Location location) {
        return new ConnectionGroupID(getDisplayGroupID(location)).get();
    }

    protected abstract List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location);

    protected static double powerLoss(double inputPower, double powerLoss) {
        return inputPower*(1-powerLoss);
    }

    @Override
    protected void onPlace(@NotNull BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final ConnectionGroupID groupID = new ConnectionGroupID(getDisplayGroupID(location));
        final List<ConnectionPoint> points = generateConnectionPoints(groupID, event.getPlayer(), location);
        new ConnectionGroup(groupID, this, points);
        QuapticStorage.addGroup(groupID);
    }

    @SuppressWarnings("unused")
    protected void onBreak(Location location) {}

    @Override
    protected void onBreak(@NotNull BlockBreakEvent event) {
        getGroup(event.getBlock().getLocation()).remove();
        onBreak(event.getBlock().getLocation());
    }

    public void connect(@NotNull ConnectionPointID from, ConnectionPointID to) {
        from.get().getGroup().changePointLocation(from, calculatePointLocationSphere(from, to));
    }

    public void burnout(Location location) {
        // TODO send message to player to inform them what happened
        onBreak(location);
        getGroup(location).remove();
        getDisplayGroup(location).getDisplays().values().forEach(Entity::remove);
        getDisplayGroup(location).remove();

        BlockStorage.clearBlockInfo(location);
        location.getBlock().setBlockData(Material.AIR.createBlockData());
        new ParticleBuilder(Particle.FLASH)
                .location(location.toCenterLocation())
                .count(3)
                .spawn();
    }

    public boolean doBurnoutCheck(ConnectionGroup group, @NotNull ConnectionPoint point) {
        if (!point.hasLink() || point.getLink().getPower() <= settings.getTier().maxPower) {
            return false;
        }

        final Location location = group.getLocation();
        if (BlockStorage.hasBlockInfo(location) && BlockStorage.getLocationInfo(location, Keys.BURNOUT) == null) {
            BlockStorage.addBlockInfo(location, Keys.BURNOUT, "true");
            BurnoutManager.addBurnout(new BurnoutRunnable(location));
        }

        return true;
    }

    public List<ConnectionPoint> getLinkedPoints(Location location) {
        return getGroup(location).getPoints().values().stream()
                .map(ConnectionPointID::get)
                .filter(Objects::nonNull)
                .filter(ConnectionPoint::hasLink)
                .toList();
    }

    public List<ConnectionPointOutput> getLinkedOutputs(Location location) {
        return getLinkedPoints(location).stream()
                .filter(ConnectionPointOutput.class::isInstance)
                .map(ConnectionPointOutput.class::cast)
                .toList();
    }

    public List<ConnectionPointInput> getLinkedInputs(Location location) {
        return getLinkedPoints(location).stream()
                .filter(ConnectionPointInput.class::isInstance)
                .map(ConnectionPointInput.class::cast)
                .toList();
    }

    public List<ConnectionPointInput> getEnabledInputs(Location location) {
        return getLinkedInputs(location).stream().filter(ConnectionPoint::isLinkEnabled).toList();
    }

    public void doDisplayBrightnessCheck(Location location, String concreteDisplayName) {
        doDisplayBrightnessCheck(location, concreteDisplayName, true);
    }

    public void doDisplayBrightnessCheck(Location location, String concreteDisplayName, boolean setVisible) {
        final Display display = getDisplay(location, concreteDisplayName);
        if (display == null) {
            return;
        }

        final boolean noInputs = getEnabledInputs(location).isEmpty();
        if (setVisible) {
            display.setViewRange(noInputs ? VIEW_RANGE_OFF : VIEW_RANGE_ON);
        } else  {
            display.setBrightness(noInputs ? BRIGHTNESS_OFF : BRIGHTNESS_ON);
        }
    }

    public void onInputLinkUpdated(ConnectionGroup group) {}

    public Location calculatePointLocationSphere(@NotNull ConnectionPointID from, @NotNull ConnectionPointID to) {
        final Location fromGroupLocation =  from.get().getGroup().getLocation();
        final Location toGroupLocation =  to.get().getGroup().getLocation();
        final Vector radiusDirection = Vector.fromJOML(Transformations.getDirection(fromGroupLocation, toGroupLocation).mul(settings.getConnectionRadius()));
        return fromGroupLocation.clone().add(0.5, 0.5, 0.5).add(radiusDirection);
    }


    @Getter
    @Builder
    public static class Settings {
        Tier tier;

        private double minPower;
        private double emissionPower;
        private double powerLoss;

        private double minFrequency;
        private double maxFrequency;
        private double frequencyStep;
        private double frequencyMultiplier;

        private int minPhase;
        private int maxPhase;
        private int phaseStep;

        private float displayRadius;
        private float connectionRadius;

        private int connections;

        private double range;
        private double damage;
    }
}
