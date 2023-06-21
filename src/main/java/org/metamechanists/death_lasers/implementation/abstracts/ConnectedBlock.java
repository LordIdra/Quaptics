package org.metamechanists.death_lasers.implementation.abstracts;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Map;

public abstract class ConnectedBlock extends EnergyDisplayGroupBlock {
    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }

    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);
    }

    protected abstract Map<String, ConnectionPoint> generateConnectionPoints(Player player, Location location);

    @Override
    protected void onPlace(BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Map<String, ConnectionPoint> points = generateConnectionPoints(event.getPlayer(), location);
        ConnectionPointStorage.addConnectionPointGroup(location, new ConnectionGroup(location, this, points));
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
        return ConnectionPointStorage.hasPoint(newFrom);
    }

    @OverridingMethodsMustInvokeSuper
    public void connect(ConnectionPoint from, ConnectionPoint to) {
        ConnectionPointStorage.updatePointLocation(from.getLocation(), calculateNewLocation(from, to));
    }

    @OverridingMethodsMustInvokeSuper
    public void onInputLinkUpdated(ConnectionGroup group) {
        group.update();
    }

    protected abstract Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to);
}
