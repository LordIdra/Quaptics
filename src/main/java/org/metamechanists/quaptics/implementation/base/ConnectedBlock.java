package org.metamechanists.quaptics.implementation.base;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import lombok.Getter;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.QuapticStorage;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.DisplayGroupId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ConnectedBlock extends DisplayGroupTickerBlock {
    private static final Brightness BRIGHTNESS_ON = new Brightness(13, 0);
    private static final Brightness BRIGHTNESS_OFF = new Brightness(5, 0);
    private static final int VIEW_RANGE_ON = 1;
    protected static final int VIEW_RANGE_OFF = 0;
    private static final int BURNOUT_EXPLODE_VOLUME = 2;
    private static final float BURNOUT_EXPLODE_PITCH = 1.2F;

    @Getter
    protected final Settings settings;

    protected ConnectedBlock(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                             final Settings settings) {
        super(group, item, recipeType, recipe);
        this.settings = settings;
        addItemHandler(onUse());
    }

    private static void changePointLocation(final @NotNull ConnectionPointId pointId, @NotNull final Location newLocation) {
        pointId.get().ifPresent(point -> point.changeLocation(newLocation));
    }

    @NotNull
    private static BlockUseHandler onUse() {
        return event -> {
            if (!event.getPlayer().isSneaking()) {
                return;
            }

            final Block block = event.getClickedBlock().orElse(null);
            if (block == null) {
                return;
            }

            final Optional<ConnectionGroup> group = getGroup(block.getLocation());
            if (group.isEmpty()) {
                return;
            }

            final boolean isAnyPanelHidden = group.get().getPoints().values().stream().anyMatch(
                    pointId -> pointId.get().isPresent() && pointId.get().get().getPointPanel().isPanelHidden());

            group.get().getPoints().values().stream()
                    .map(ConnectionPointId::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(point -> point.getPointPanel().setPanelHidden(!isAnyPanelHidden));

        };
    }

    protected static Optional<ConnectionGroup> getGroup(final Location location) {
        final Optional<DisplayGroupId> displayGroupId = getDisplayGroupId(location);
        return displayGroupId.isEmpty()
            ? Optional.empty()
            : new ConnectionGroupId(displayGroupId.get()).get();
    }

    protected abstract List<ConnectionPoint> generateConnectionPoints(ConnectionGroupId groupId, Player player, Location location);

    @Override
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Optional<DisplayGroupId> displayGroupId = getDisplayGroupId(location);
        if (displayGroupId.isEmpty()) {
            return;
        }

        final ConnectionGroupId groupId = new ConnectionGroupId(displayGroupId.get());
        final List<ConnectionPoint> points = generateConnectionPoints(groupId, event.getPlayer(), location);
        new ConnectionGroup(groupId, this, points);
        QuapticStorage.addGroup(groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    @SuppressWarnings("unused")
    protected void onBreak(@NotNull final Location location) {
        getGroup(location).ifPresent(group -> onBreak(location));
    }

    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {
        calculatePointLocationSphere(from, to).ifPresent(location -> changePointLocation(from, location));
    }

    public void burnout(final Location location) {
        // TODO send message to player to inform them what happened
        onBreak(location);

        getGroup(location).ifPresent(ConnectionGroup::remove);
        getDisplayGroup(location).ifPresent(displayGroup -> {
            displayGroup.getDisplays().values().forEach(Entity::remove);
            displayGroup.remove();
        });

        // TODO make this naturally break, not the forced shit we have going on here
        BlockStorage.clearBlockInfo(location);
        location.getBlock().setBlockData(Material.AIR.createBlockData());
        location.getWorld().playSound(location.toCenterLocation(), Sound.ENTITY_GENERIC_EXPLODE, BURNOUT_EXPLODE_VOLUME, BURNOUT_EXPLODE_PITCH);
        new ParticleBuilder(Particle.FLASH).location(location.toCenterLocation()).count(3).spawn();
    }

    protected boolean doBurnoutCheck(@NotNull final ConnectionGroup group, @NotNull final ConnectionPoint point) {
        if (point.getLink().isEmpty() || point.getLink().get().getPower() <= settings.getTier().maxPower) {
            return false;
        }

        group.getLocation().ifPresent(location -> {
             if (BlockStorage.hasBlockInfo(location) && BlockStorage.getLocationInfo(location, Keys.BS_BURNOUT) == null) {
                 BlockStorage.addBlockInfo(location, Keys.BS_BURNOUT, "true");
                 BurnoutManager.addBurnout(new BurnoutRunnable(location));
             }
        });

        return true;
    }

    @NotNull
    private static List<ConnectionPoint> getLinkedPoints(final Location location) {
        final Optional<ConnectionGroup> group = getGroup(location);
        return group.map(connectionGroup -> connectionGroup.getPoints().values().stream()
                .map(ConnectionPointId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(point -> point.getLink().isPresent())
                .toList())
                .orElseGet(ArrayList::new);

    }

    @NotNull
    protected static List<ConnectionPointOutput> getLinkedOutputs(final Location location) {
        return getLinkedPoints(location).stream()
                .filter(ConnectionPointOutput.class::isInstance)
                .map(ConnectionPointOutput.class::cast)
                .toList();
    }

    @NotNull
    private static List<ConnectionPointInput> getLinkedInputs(final Location location) {
        return getLinkedPoints(location).stream()
                .filter(ConnectionPointInput.class::isInstance)
                .map(ConnectionPointInput.class::cast)
                .toList();
    }

    @NotNull
    protected static List<ConnectionPointInput> getEnabledInputs(final Location location) {
        return getLinkedInputs(location).stream().filter(ConnectionPoint::isLinkEnabled).toList();
    }

    protected static void doDisplayBrightnessCheck(final Location location, final String concreteDisplayName) {
        doDisplayBrightnessCheck(location, concreteDisplayName, true);
    }

    protected static void doDisplayBrightnessCheck(final Location location, final String concreteDisplayName, final boolean setVisible) {
        final Optional<Display> display = getDisplay(location, concreteDisplayName);
        if (display.isEmpty()) {
            return;
        }

        final boolean noInputs = getEnabledInputs(location).isEmpty();
        if (setVisible) {
            display.get().setViewRange(noInputs ? VIEW_RANGE_OFF : VIEW_RANGE_ON);
        } else  {
            display.get().setBrightness(noInputs ? BRIGHTNESS_OFF : BRIGHTNESS_ON);
        }
    }

    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {}

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