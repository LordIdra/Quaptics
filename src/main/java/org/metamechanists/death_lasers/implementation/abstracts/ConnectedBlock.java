package org.metamechanists.death_lasers.implementation.abstracts;

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
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.Keys;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.id.ConnectionGroupID;
import org.metamechanists.metalib.utils.RadiusUtils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ConnectedBlock extends EnergyDisplayGroupBlock {
    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }

    public ConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);
    }

    protected abstract List<ConnectionPoint> generateConnectionPoints(Player player, Location location);

    @Override
    protected void onPlace(BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();

        final AtomicBoolean valid = new AtomicBoolean(true);
        RadiusUtils.forEachSphereRadius(location.getBlock(), (int)(getRadius()+0.5F), block -> {
            if (block.getLocation().equals(location)) {
                return false;
            }

            if (BlockStorage.check(block) instanceof ConnectedBlock) {
                valid.set(false);
                return true;
            }

            return false;
        });

        if (!valid.get()) {
            event.getPlayer().sendMessage(Language.getLanguageEntry("too-close"));
            event.setCancelled(true);
            return;
        }

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

        location.createExplosion(3F, true, false);
        location.getWorld().spawnParticle(
                new ParticleBuilder(Particle.CAMPFIRE_COSY_SMOKE).location(location).particle(),
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

    public Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to) {
        final Location fromGroupLocation = from.getGroup().getLocation();
        final Location toGroupLocation = to.getGroup().getLocation();
        final Vector radiusDirection = DisplayUtils.getDirection(fromGroupLocation, toGroupLocation).multiply(getRadius());
        return fromGroupLocation.clone().add(0.5, 0.5, 0.5).add(radiusDirection);
    }

    protected abstract float getRadius();
}
