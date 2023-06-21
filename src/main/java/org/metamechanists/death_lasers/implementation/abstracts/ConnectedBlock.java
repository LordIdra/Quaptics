package org.metamechanists.death_lasers.implementation.abstracts;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
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

    protected void onBreak(Location location) {
        ConnectionPointStorage.removeConnectionPointGroup(location);
    }

    @Override
    protected void onBreak(BlockBreakEvent event) {
        onBreak(event.getBlock().getLocation());
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

    public void burnout(Location location) {
        onBreak(location);
        getDisplayGroup(location).getDisplays().values().forEach(Entity::remove);
        getDisplayGroup(location).remove();

        location.createExplosion(1.5F, true, false);
        location.getWorld().spawnParticle(
                new ParticleBuilder(Particle.CAMPFIRE_COSY_SMOKE).location(location).count(0).particle(),
                0.05, 0.05, 0.05,
                20);
        location.getWorld().spawnParticle(
                new ParticleBuilder(Particle.FLASH).location(location).count(1).particle(),
                location,
                20);
        // TODO breaking sound?
    }

    public void doBurnoutCheck(ConnectionGroup group, ConnectionPoint point, double maxPower) {
        if (point.hasLink() && point.getLink().getPower() > maxPower) {
            burnout(group.getLocation());
        }
    }

    public void onInputLinkUpdated(ConnectionGroup group) {}

    protected abstract Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to);
}
