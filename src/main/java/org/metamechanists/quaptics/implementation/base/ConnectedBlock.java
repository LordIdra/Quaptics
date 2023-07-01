package org.metamechanists.quaptics.implementation.base;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ConnectedBlock extends DisplayGroupTickerBlock {
    protected static final Display.Brightness BRIGHTNESS_ON = new Display.Brightness(13, 0);
    protected static final Display.Brightness BRIGHTNESS_OFF = new Display.Brightness(5, 0);
    protected static final int VIEW_RANGE_ON = 1;
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
            if (group == null) {
                return;
            }

            final boolean isAnyPanelHidden = group.getPoints().values().stream().anyMatch(pointId -> {
                final ConnectionPoint point = pointId.get();
                if (point != null) {
                    return point.getPointPanel().isPanelHidden();
                }
                return false;
            });

            group.getPoints().values().forEach(pointId -> {
                final ConnectionPoint point = pointId.get();
                if (point != null) {
                    point.getPointPanel().setPanelHidden(!isAnyPanelHidden);
                }
            });

        };
    }

    protected @Nullable ConnectionGroup getGroup(Location location) {
        final DisplayGroupId displayGroupId = getDisplayGroupId(location);
        if (displayGroupId == null) {
            return null;
        }

        return new ConnectionGroupId(displayGroupId).get();
    }

    protected abstract List<ConnectionPoint> generateConnectionPoints(ConnectionGroupId groupId, Player player, Location location);

    @Override
    protected void onPlace(@NotNull BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final ConnectionGroupId groupId = new ConnectionGroupId(getDisplayGroupId(location));
        final List<ConnectionPoint> points = generateConnectionPoints(groupId, event.getPlayer(), location);
        new ConnectionGroup(groupId, this, points);
        QuapticStorage.addGroup(groupId);
    }

    @SuppressWarnings("unused")
    protected void onBreak(Location location) {}

    @Override
    protected void onBreak(@NotNull BlockBreakEvent event) {
        final ConnectionGroup group = getGroup(event.getBlock().getLocation());
        if (group != null) {
            group.remove();
        }
        onBreak(event.getBlock().getLocation());
    }

    public void connect(@NotNull ConnectionPointId from, @NotNull ConnectionPointId to) {
        final ConnectionPoint fromPoint = from.get();
        final ConnectionGroup fromGroup = fromPoint != null ? fromPoint.getGroup() : null;

        if (fromGroup != null) {
            fromGroup.changePointLocation(from, calculatePointLocationSphere(from, to));
        }
    }

    public void burnout(Location location) {
        // TODO send message to player to inform them what happened
        onBreak(location);

        final ConnectionGroup group = getGroup(location);
        if (group != null) {
            group.remove();
        }

        final DisplayGroup displayGroup = getDisplayGroup(location);
        if (displayGroup != null) {
            displayGroup.getDisplays().values().forEach(Entity::remove);
            displayGroup.remove();
        }

        BlockStorage.clearBlockInfo(location);
        location.getBlock().setBlockData(Material.AIR.createBlockData());
        location.getWorld().playSound(location.toCenterLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2, 1.2F);
        new ParticleBuilder(Particle.FLASH).location(location.toCenterLocation()).count(3).spawn();
    }

    public boolean doBurnoutCheck(@NotNull ConnectionGroup group, @NotNull ConnectionPoint point) {
        if (!point.hasLink() || point.getLink().getPower() <= settings.getTier().maxPower) {
            return false;
        }

        final Location location = group.getLocation();
        if (location != null && BlockStorage.hasBlockInfo(location) && BlockStorage.getLocationInfo(location, Keys.BS_BURNOUT) == null) {
            BlockStorage.addBlockInfo(location, Keys.BS_BURNOUT, "true");
            BurnoutManager.addBurnout(new BurnoutRunnable(location));
        }

        return true;
    }

    public @NotNull List<ConnectionPoint> getLinkedPoints(Location location) {
        final ConnectionGroup group = getGroup(location);
        if (group != null) {
            return group.getPoints().values().stream()
                    .map(ConnectionPointId::get)
                    .filter(Objects::nonNull)
                    .filter(ConnectionPoint::hasLink)
                    .toList();
        }
        return new ArrayList<>();
    }

    public @NotNull List<ConnectionPointOutput> getLinkedOutputs(Location location) {
        return getLinkedPoints(location).stream()
                .filter(ConnectionPointOutput.class::isInstance)
                .map(ConnectionPointOutput.class::cast)
                .toList();
    }

    public @NotNull List<ConnectionPointInput> getLinkedInputs(Location location) {
        return getLinkedPoints(location).stream()
                .filter(ConnectionPointInput.class::isInstance)
                .map(ConnectionPointInput.class::cast)
                .toList();
    }

    public @NotNull List<ConnectionPointInput> getEnabledInputs(Location location) {
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

    public Location calculatePointLocationSphere(@NotNull ConnectionPointId from, @NotNull ConnectionPointId to) {
        final ConnectionPoint fromPoint = from.get();
        final ConnectionPoint toPoint = to.get();
        if (fromPoint == null || toPoint == null) {
            return null;
        }

        final ConnectionGroup fromGroup = fromPoint.getGroup();
        final ConnectionGroup toGroup = toPoint.getGroup();
        if (fromGroup == null || toGroup == null) {
            return null;
        }

        final Location fromLocation =  fromGroup.getLocation();
        final Location toLocation =  toGroup.getLocation();
        if (fromLocation == null || toLocation == null) {
            return null;
        }

        final Vector radiusDirection = Vector.fromJOML(Transformations.getDirection(fromLocation, toLocation).mul(settings.getConnectionRadius()));
        return fromLocation.clone().add(0.5, 0.5, 0.5).add(radiusDirection);
    }
}
