package org.metamechanists.quaptics.implementation.blocks.consumers;

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
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.beams.DeprecatedTickerStorage;
import org.metamechanists.quaptics.beams.ticker.IntervalVelocityTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Turret extends ConnectedBlock {
    private final Vector3f mainDisplaySize = new Vector3f(0.6F, 0.6F, 0.6F);
    private final Vector3f barrelSize = new Vector3f(0.18F, 0.18F, settings.getDisplayRadius());
    private final Vector3f barrelTranslation = new Vector3f(0, 0, settings.getDisplayRadius() *0.8F);
    private final Vector barrelLocation = new Vector(0.5, 0.7, 0.5);
    private final Vector3f projectileSize = new Vector3f(0.095F, 0.095F, 0.20F);
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public Turret(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                  Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    private Matrix4f getBarrelMatrix(@NotNull Location from, Location to) {
        return Transformations.lookAlong(barrelSize, Transformations.getDirection(from.clone().add(barrelLocation), to)).translate(barrelTranslation);
    }

    private BlockDisplay generateBarrel(@NotNull Location from, Location to) {
        return new BlockDisplayBuilder(from.clone().add(barrelLocation))
                .setMaterial(Material.GRAY_CONCRETE)
                .setTransformation(getBarrelMatrix(from, to))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                        .setMaterial(Material.POLISHED_ANDESITE)
                        .setTransformation(Transformations.adjustedScale(mainDisplaySize))
                        .build());
        displayGroup.addDisplay("barrel", generateBarrel(location, location.clone().add(barrelLocation).add(new Vector(0, 1, 0))));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        return List.of(new ConnectionPointInput(groupID, "input", formatPointLocation(player, location, inputLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");

        if (doBurnoutCheck(group, input)) {
            return;
        }

        BlockStorage.addBlockInfo(group.getLocation(), Keys.POWERED, "false");
        if (!input.hasLink()) {
            return;
        }
        if (input.getLink().getPower() >= settings.getMinPower()) {
            BlockStorage.addBlockInfo(group.getLocation(), Keys.POWERED, "true");
        }
    }

    private void setTarget(Location location, @NotNull Damageable entity) {
        BlockStorage.addBlockInfo(location, Keys.TARGET, entity.getUniqueId().toString());
    }

    private void clearTarget(Location location) {
        BlockStorage.addBlockInfo(location, Keys.TARGET, null);
    }

    private @Nullable LivingEntity getTarget(Location location) {
        final String targetString =  BlockStorage.getLocationInfo(location, Keys.TARGET);
        if (targetString == null) {
            return null;
        }

        return (LivingEntity) Bukkit.getEntity(UUID.fromString(targetString));
    }

    private void retarget(Location location) {
        if (BlockStorage.getLocationInfo(location, Keys.TARGET) != null) {
            return;
        }

        final Collection<Entity> entities = location.getWorld().getNearbyEntities(location, settings.getRange(), settings.getRange(), settings.getRange());
        final Collection<Entity> targetableEntities = entities.stream().filter(entity ->
                entity.getSpawnCategory().equals(SpawnCategory.MONSTER)
                        && entity instanceof Damageable
                        && entity.getLocation().distance(location) < settings.getRange()).toList();

        if (targetableEntities.isEmpty()) {
            return;
        }

        LivingEntity target = null;
        double targetDistance = 9999999;
        for (Entity entity : targetableEntities) {
            final double distance = entity.getLocation().distance(location.toCenterLocation());
            if (distance < targetDistance) {
                target = (LivingEntity) entity;
                targetDistance = distance;
            }
        }

        setTarget(location, target);
    }

    private void shoot(Location location) {
        LivingEntity target = getTarget(location);

        if (target == null
                || target.isDead()
                || location.toCenterLocation().distance(target.getLocation()) > settings.getRange()) {
            clearTarget(location);
            return;
        }

        getDisplayGroup(location).getDisplays().get("barrel").setTransformationMatrix(getBarrelMatrix(location, target.getEyeLocation()));

        DeprecatedTickerStorage.deprecate(new IntervalVelocityTicker(
                Material.LIGHT_BLUE_CONCRETE,
                location.clone().add(barrelLocation),
                target.getEyeLocation(),
                projectileSize,
                1));

        target.damage(settings.getDamage());
    }

    @Override
    protected void onSlimefunTick(@NotNull Block block, SlimefunItem item, Config data) {
        if (Objects.equal(BlockStorage.getLocationInfo(block.getLocation(), Keys.POWERED), "true")) {
            retarget(block.getLocation());
            shoot(block.getLocation());
        }
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.SMOOTH_STONE_SLAB;
    }
}
