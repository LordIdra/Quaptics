package org.metamechanists.death_lasers.implementation.blocks;

import com.google.common.base.Objects;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.beams.DeprecatedTickerStorage;
import org.metamechanists.death_lasers.beams.ticker.ticker.IntervalLinearTimeTicker;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.implementation.base.ConnectedBlock;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.Keys;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Turret extends ConnectedBlock {
    private final double powerConsumption;
    private final double range;
    private final double damagePerSlimefunTick;

    public Turret(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                  double maxPower, double powerConsumption, double range, double damagePerSlimefunTick) {
        super(group, item, recipeType, recipe, maxPower);
        this.powerConsumption = powerConsumption;
        this.range = range;
        this.damagePerSlimefunTick = damagePerSlimefunTick;
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);

        displayGroup.addDisplay(
                "main",
                DisplayUtils.spawnBlockDisplay(
                        location.clone().add(0.5, 0.5, 0.5),
                        Material.ORANGE_STAINED_GLASS,
                        DisplayUtils.rotationTransformation(
                                new Vector3f(0.2F, 0.2F, 0.2F),
                                new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0))));

        return displayGroup;
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointInput("input", formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, -getRadius()))));
        return points;
    }

    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");

        doBurnoutCheck(group, input);

        BlockStorage.addBlockInfo(group.getLocation(), Keys.POWERED, "false");
        if (!input.hasLink()) {
            return;
        }
        if (input.getLink().getPower() >= powerConsumption) {
            BlockStorage.addBlockInfo(group.getLocation(), Keys.POWERED, "true");
        }
    }

    private void setTarget(Location location, Damageable entity) {
        BlockStorage.addBlockInfo(location, Keys.TARGET, entity.getUniqueId().toString());
    }

    private void clearTarget(Location location) {
        BlockStorage.addBlockInfo(location, Keys.TARGET, null);
    }

    private Damageable getTarget(Location location) {
        final String targetString =  BlockStorage.getLocationInfo(location, Keys.TARGET);
        if (targetString == null) {
            return null;
        }

        return (Damageable) Bukkit.getEntity(UUID.fromString(targetString));
    }

    private void retarget(Location location) {
        if (BlockStorage.getLocationInfo(location, Keys.TARGET) != null) {
            return;
        }

        final Collection<Entity> entities = location.getWorld().getNearbyEntities(location, range, range, range);
        final Collection<Entity> targetableEntities = entities.stream().filter(entity -> {
            final boolean hostile = entity.getSpawnCategory().equals(SpawnCategory.MONSTER);
            final boolean damageable = entity instanceof Damageable;
            final boolean inRange = entity.getLocation().distance(location) < range;
            return hostile && inRange && damageable;
        }).toList();

        if (targetableEntities.isEmpty()) {
            return;
        }

        Damageable target = null;
        double targetDistance = 9999999;
        for (Entity entity : targetableEntities) {
            final double distance = entity.getLocation().distance(location);
            if (distance < targetDistance) {
                target = (Damageable) entity;
                targetDistance = distance;
            }
        }

        setTarget(location, target);
    }

    private void shoot(Location location) {
        Damageable target = getTarget(location);

        if (target == null
                || target.isDead()
                || location.distance(target.getLocation()) > range) {
            clearTarget(location);
            return;
        }

        DeprecatedTickerStorage.deprecate(new IntervalLinearTimeTicker(
                Material.LIGHT_BLUE_CONCRETE,
                location.clone().add(0.5, 0.5, 0.5),
                target.getLocation(),
                10));

        target.damage(damagePerSlimefunTick);
    }

    @Override
    protected void onSlimefunTick(Block block, SlimefunItem item, Config data) {
        if (Objects.equal(BlockStorage.getLocationInfo(block.getLocation(), Keys.POWERED), "true")) {
            retarget(block.getLocation());
            shoot(block.getLocation());
        }
    }

    @Override
    protected float getRadius() {
        return 0.35F;
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
