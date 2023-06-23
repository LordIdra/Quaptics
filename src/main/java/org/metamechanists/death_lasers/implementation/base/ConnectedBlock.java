package org.metamechanists.death_lasers.implementation.base;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.utils.Transformations;
import org.metamechanists.death_lasers.utils.Keys;
import org.metamechanists.death_lasers.utils.id.ConnectionGroupID;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;

public abstract class ConnectedBlock extends EnergyDisplayGroupBlock {
    public final double maxPower;

    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
        this.maxPower = maxPower;
    }

    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower) {
        super(group, item, recipeType, recipe);
        this.maxPower = maxPower;
    }

    protected abstract List<ConnectionPoint> generateConnectionPoints(Player player, Location location);

    @Override
    protected void onPlace(BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();

        final List<ConnectionPoint> points = generateConnectionPoints(event.getPlayer(), location);
        final ConnectionGroup group = new ConnectionGroup(location, this, points);

        ConnectionPointStorage.addGroup(group);
        BlockStorage.addBlockInfo(location, Keys.CONNECTION_GROUP_ID,group.getId().toString());
    }

    protected void onBreak(Location location) {
        final ConnectionGroupID id = new ConnectionGroupID(BlockStorage.getLocationInfo(location, Keys.CONNECTION_GROUP_ID));
        ConnectionPointStorage.removeGroup(id);
    }

    @Override
    protected void onBreak(BlockBreakEvent event) {
        onBreak(event.getBlock().getLocation());
    }

    @OverridingMethodsMustInvokeSuper
    public void connect(ConnectionPoint from, ConnectionPoint to) {
        ConnectionPointStorage.updatePointLocation(from.getId(), calculateNewLocation(from, to));
    }

    public void burnout(Location location) {
        onBreak(location);
        getDisplayGroup(location).getDisplays().values().forEach(Entity::remove);
        getDisplayGroup(location).remove();

        BlockStorage.clearBlockInfo(location);
        location.getBlock().setBlockData(Material.AIR.createBlockData());

        location.createExplosion(1.5F, true, false);
        location.getWorld().spawnParticle(new ParticleBuilder(Particle.FLASH).location(location).count(1).particle(), location, 20);
    }

    public void doBurnoutCheck(ConnectionGroup group, ConnectionPoint point) {
        if (point.hasLink() && point.getLink().getPower() > maxPower) {
            burnout(group.getLocation());
        }
    }

    public void onInputLinkUpdated(ConnectionGroup group) {}

    public Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to) {
        final Location fromGroupLocation = from.getGroup().getLocation();
        final Location toGroupLocation = to.getGroup().getLocation();
        final Vector radiusDirection = Vector.fromJOML(Transformations.getDirection(fromGroupLocation, toGroupLocation).mul(getRadius()));
        return fromGroupLocation.clone().add(0.5, 0.5, 0.5).add(radiusDirection);
    }

    protected abstract float getRadius();
}
