package org.metamechanists.death_lasers.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
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
    private final int consumption;

    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
        this.consumption = consumption;
        this.capacity = capacity;
    }

    protected abstract Map<String, ConnectionPoint> generateConnectionPoints(Location location);

    @Override
    protected void onPlace(BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Map<String, ConnectionPoint> points = generateConnectionPoints(location);
        ConnectionPointStorage.addConnectionPointGroup(location, new ConnectionGroup(this, points));
    }

    @Override
    protected void onBreak(BlockBreakEvent event) {
        final Location location = event.getBlock().getLocation();
        ConnectionPointStorage.removeConnectionPointGroup(location);
    }

    @Override
    public void onTick(Block block, SlimefunItem item, Config data) {
        final int charge = getCharge(block.getLocation(), data);
        if (charge >= consumption) {
            removeCharge(block.getLocation(), consumption);
        }
    }

    @OverridingMethodsMustInvokeSuper
    public boolean connectionInvalid(Location blockLocation, ConnectionPoint from, ConnectionPoint to) {
        if (blockLocation == calculateNewLocation(from, to)) {
            return false;
        }
        return ConnectionPointStorage.hasConnectionPoint(calculateNewLocation(from, to));
    }

    protected abstract Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to);
    public abstract void connect(ConnectionPoint from, ConnectionPoint to);
}
