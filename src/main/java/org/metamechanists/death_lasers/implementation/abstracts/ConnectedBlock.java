package org.metamechanists.death_lasers.implementation.abstracts;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;

public abstract class ConnectedBlock extends EnergyDisplayGroupBlock {
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;
    @Getter
    private final int capacity;
    protected final int consumption;

    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
        this.consumption = consumption;
        this.capacity = capacity;
    }

    private double yawToCardinalDirection(float yaw) {
        return Math.round(yaw / 90F) * (Math.PI/2);
    }

    protected Location formatRelativeLocation(Player player, Location location, Vector vector) {
        final double rotationAngle = yawToCardinalDirection(player.getEyeLocation().getYaw());
        vector.rotateAroundY(rotationAngle);
        vector.add(new Vector(0.5, 0.5, 0.5));
        return location.add(vector);
    }

    protected abstract Map<String, ConnectionPoint> generateConnectionPoints(Player player, Location location);

    @Override
    protected void onPlace(BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Map<String, ConnectionPoint> points = generateConnectionPoints(event.getPlayer(), location);
        ConnectionPointStorage.addConnectionPointGroup(location, new ConnectionGroup(this, points));
    }

    @Override
    protected void onBreak(BlockBreakEvent event) {
        final Location location = event.getBlock().getLocation();
        ConnectionPointStorage.removeConnectionPointGroup(location);
    }

    @OverridingMethodsMustInvokeSuper
    public boolean connectionInvalid(ConnectionPoint from, ConnectionPoint to) {
        Location newFrom = calculateNewLocation(from, to);
        if (from.getLocation().distance(newFrom) < 0.000001F) {
            return false;
        }
        return ConnectionPointStorage.hasConnectionPoint(newFrom);
    }

    @OverridingMethodsMustInvokeSuper
    public void connect(ConnectionPoint from, ConnectionPoint to) {
        ConnectionPointStorage.updateLocation(from.getLocation(), calculateNewLocation(from, to));
    }

    @OverridingMethodsMustInvokeSuper
    public void onLinkUpdated(ConnectionGroup group) {
        group.update();
    }

    protected abstract Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to);
}
