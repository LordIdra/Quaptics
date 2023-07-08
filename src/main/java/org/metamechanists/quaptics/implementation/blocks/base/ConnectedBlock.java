package org.metamechanists.quaptics.implementation.blocks.base;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.burnout.BurnoutManager;
import org.metamechanists.quaptics.implementation.blocks.burnout.BurnoutRunnable;
import org.metamechanists.quaptics.panels.info.implementation.PointInfoPanel;
import org.metamechanists.quaptics.storage.QuapticStorage;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.simple.DisplayGroupId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ConnectedBlock extends QuapticBlock {
    private static final int BURNOUT_EXPLODE_VOLUME = 2;
    private static final float BURNOUT_EXPLODE_PITCH = 1.2F;

    protected ConnectedBlock(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    protected abstract List<ConnectionPoint> initConnectionPoints(ConnectionGroupId groupId, Player player, Location location);

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Optional<DisplayGroupId> displayGroupId = getDisplayGroupId(location);
        if (displayGroupId.isEmpty()) {
            return;
        }

        final ConnectionGroupId groupId = new ConnectionGroupId(displayGroupId.get());
        final List<ConnectionPoint> points = initConnectionPoints(groupId, event.getPlayer(), location);
        new ConnectionGroup(groupId, this, points);
        QuapticStorage.addGroup(groupId);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        getGroup(location).ifPresent(ConnectionGroup::remove);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onShiftRightClick(@NotNull final Location location, @NotNull final Player player) {
        final Optional<ConnectionGroup> group = getGroup(location);
        if (group.isEmpty()) {
            return;
        }

        final boolean isAnyPanelHidden = group.get().getPointPanels().stream().anyMatch(PointInfoPanel::isPanelHidden);
        group.get().getPointPanels().forEach(panel -> panel.setPanelHidden(!isAnyPanelHidden));
    }
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {}

    public void burnout(final Location location) {
        // TODO send message to player to inform them what happened
        onBreak(location);

        getGroup(location).ifPresent(ConnectionGroup::remove);
        getDisplayGroup(location).ifPresent(displayGroup -> {
            displayGroup.getDisplays().values().forEach(Entity::remove);
            displayGroup.remove();
        });

        // TODO make this naturally break, not the forced shit we have going on here
        BlockStorageAPI.removeData(location);
        location.getBlock().setBlockData(Material.AIR.createBlockData());
        location.getWorld().playSound(location.toCenterLocation(), Sound.ENTITY_GENERIC_EXPLODE, BURNOUT_EXPLODE_VOLUME, BURNOUT_EXPLODE_PITCH);
        new ParticleBuilder(Particle.FLASH).location(location.toCenterLocation()).count(3).spawn();
    }
    private boolean doBurnoutCheck(@NotNull final ConnectionGroup group, @NotNull final ConnectionPoint point) {
        if (point.getLink().isEmpty() || point.getLink().get().getPower() <= settings.getTier().maxPower) {
            return false;
        }

        group.getLocation().ifPresent(location -> {
             if (!BlockStorageAPI.hasData(location, Keys.BS_BURNOUT)) {
                 BlockStorageAPI.set(location, Keys.BS_BURNOUT, true);
                 BurnoutManager.addBurnout(new BurnoutRunnable(location));
             }
        });

        return true;
    }
    protected boolean doBurnoutCheck(@NotNull final ConnectionGroup group, @NotNull final String pointName) {
        final Optional<ConnectionPoint> point = group.getPoint(pointName);
        return point.isPresent() && doBurnoutCheck(group, point.get());
    }
    protected boolean doBurnoutCheck(@NotNull final ConnectionGroup group, final @NotNull List<? extends ConnectionPoint> points) {
        return points.stream().anyMatch(input -> doBurnoutCheck(group, input));
    }

    @SuppressWarnings("OptionalIsPresent")
    @NotNull
    private static List<ConnectionPoint> getLinkedPoints(final Location location) {
        final Optional<ConnectionGroup> group = getGroup(location);
        if (group.isEmpty()) {
            return new ArrayList<>();
        }
        return group.get().getPointList().stream().filter(point -> point.getLink().isPresent()).toList();

    }
    @NotNull
    private static List<ConnectionPoint> getLinkedOutputs(final Location location) {
        return getLinkedPoints(location).stream().filter(ConnectionPoint::isOutput).toList();
    }
    @NotNull
    private static List<ConnectionPoint> getLinkedInputs(final Location location) {
        return getLinkedPoints(location).stream().filter(ConnectionPoint::isInput).toList();
    }
    @NotNull
    protected static List<ConnectionPoint> getEnabledInputs(final Location location) {
        return getLinkedInputs(location).stream().filter(ConnectionPoint::isLinkEnabled).toList();
    }

    @NotNull
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected static @Unmodifiable List<Link> getOutgoingLinks(final Location location) {
        return getLinkedOutputs(location).stream().map(output -> output.getLink().get()).toList();
    }
    @NotNull
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected static @Unmodifiable List<Link> getIncomingLinks(final Location location) {
        return getLinkedInputs(location).stream().map(output -> output.getLink().get()).toList();
    }
    protected static Optional<Link> getLink(final Location location, final String name) {
        final Optional<ConnectionGroup> group = getGroup(location);
        if (group.isEmpty()) {
            return Optional.empty();
        }

        final Optional<ConnectionPoint> point = group.get().getPoint(name);
        if (point.isEmpty()) {
            return Optional.empty();
        }

        return point.get().getLink();
    }
    protected static Optional<Link> getLink(final @NotNull ConnectionGroup group, final String name) {
        final Optional<Location> location = group.getLocation();
        return location.isEmpty() ? Optional.empty() : getLink(location.get(), name);
    }

    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {
        calculatePointLocationSphere(from, to).ifPresent(location -> changePointLocation(from, location));
    }

    private static void changePointLocation(final @NotNull ConnectionPointId pointId, @NotNull final Location newLocation) {
        pointId.get().ifPresent(point -> point.changeLocation(newLocation));
    }
    public static Optional<ConnectionGroup> getGroup(final Location location) {
        final Optional<DisplayGroupId> displayGroupId = getDisplayGroupId(location);
        return displayGroupId.isEmpty()
                ? Optional.empty()
                : new ConnectionGroupId(displayGroupId.get()).get();
    }
    protected static Optional<Location> getGroupLocation(final @NotNull ConnectionPointId pointId) {
        final Optional<ConnectionPoint> point = pointId.get();
        if (point.isEmpty()) {
            return Optional.empty();
        }

        final Optional<ConnectionGroup> group = point.get().getGroup();
        if (group.isEmpty()) {
            return Optional.empty();
        }

        return group.get().getLocation();
    }

    private Optional<Location> calculatePointLocationSphere(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {
        final Optional<ConnectionPoint> fromPoint = from.get();
        final Optional<ConnectionPoint> toPoint = to.get();
        if (fromPoint.isEmpty() || toPoint.isEmpty()) {
            return Optional.empty();
        }

        final Optional<ConnectionGroup> fromGroup = fromPoint.get().getGroup();
        final Optional<ConnectionGroup> toGroup = toPoint.get().getGroup();
        if (fromGroup.isEmpty() || toGroup.isEmpty()) {
            return Optional.empty();
        }

        final Optional<Location> fromLocation =  fromGroup.get().getLocation();
        final Optional<Location> toLocation =  toGroup.get().getLocation();
        if (fromLocation.isEmpty() || toLocation.isEmpty()) {
            return Optional.empty();
        }

        final Vector radiusDirection = Vector.fromJOML(Transformations.getDirection(fromLocation.get(), toLocation.get()).mul(settings.getConnectionRadius()));
        return Optional.of(fromLocation.get().clone().toCenterLocation().add(radiusDirection));
    }
}