package org.metamechanists.quaptics.implementation.blocks.consumers.turrets;

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
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class Turret extends ConnectedBlock {
    private static final int ARBITRARILY_LARGE_NUMBER = 9999999;
    private final Vector3f mainDisplaySize = new Vector3f(0.6F, 0.6F, 0.6F);
    private final Vector3f barrelSize = new Vector3f(0.18F, 0.18F, settings.getDisplayRadius()*1.3F);
    private final Vector3f barrelTranslation = new Vector3f(0, 0, settings.getDisplayRadius()*0.8F);
    private final Vector barrelLocation = new Vector(0.5, 0.7, 0.5);
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    protected Turret(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(group, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                        .setMaterial(settings.getMainMaterial())
                        .setTransformation(Transformations.adjustedScale(mainDisplaySize))
                        .build());
        displayGroup.addDisplay("barrel", generateBarrel(location, location.clone().add(barrelLocation).add(new Vector(0, -1, 0))));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputLocation)));
    }

    @Override
    protected void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        if (isPowered(block.getLocation())) {
            retarget(block.getLocation());
            shoot(block.getLocation());
        }
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        setPowered(location.get(), false);

        final Optional<Link> inputLink = getLink(location.get(), "input");
        if (inputLink.isEmpty()) {
            return;
        }

        if (settings.isOperational(inputLink)) {
            setPowered(location.get(), true);
        }
    }

    private Matrix4f getBarrelMatrix(@NotNull final Location from, final Location to) {
        return Transformations.lookAlong(barrelSize, Transformations.getDirection(from.clone().add(barrelLocation), to)).translate(barrelTranslation);
    }

    private BlockDisplay generateBarrel(@NotNull final Location from, final Location to) {
        return new BlockDisplayBuilder(from.clone().add(barrelLocation))
                .setMaterial(Material.GRAY_CONCRETE)
                .setTransformation(getBarrelMatrix(from, to))
                .build();
    }

    private static void setPowered(final Location location, final boolean powered) {
        BlockStorage.addBlockInfo(location, Keys.BS_POWERED, java.util.Objects.toString(powered));
    }

    private static boolean isPowered(final Location location) {
        return "true".equals(BlockStorage.getLocationInfo(location, Keys.BS_POWERED));
    }

    private static void setTarget(@NotNull final Location location, @NotNull final Entity entity) {
        BlockStorage.addBlockInfo(location, Keys.BS_TARGET, entity.getUniqueId().toString());
    }

    private static void clearTarget(final Location location) {
        BlockStorage.addBlockInfo(location, Keys.BS_TARGET, null);
    }

    private static Optional<LivingEntity> getTarget(final Location location) {
        final String targetString = BlockStorage.getLocationInfo(location, Keys.BS_TARGET);
        if (targetString == null) {
            return Optional.empty();
        }

        return Optional.ofNullable((LivingEntity) Bukkit.getEntity(UUID.fromString(targetString)));
    }

    private static Optional<LivingEntity> getClosestEntity(@NotNull final Collection<? extends Entity> entities, final Location location) {
        LivingEntity target = null;
        double targetDistance = ARBITRARILY_LARGE_NUMBER;
        for (final Entity entity : entities) {
            final double distance = entity.getLocation().distance(location.toCenterLocation());
            if (distance < targetDistance) {
                target = (LivingEntity) entity;
                targetDistance = distance;
            }
        }
        return Optional.ofNullable(target);
    }

    private void retarget(@NotNull final Location location) {
        if (BlockStorage.getLocationInfo(location, Keys.BS_TARGET) != null) {
            return;
        }

        final Collection<Entity> entities = location.getWorld()
                .getNearbyEntities(location, settings.getRange(), settings.getRange(), settings.getRange(),
                        (entity -> settings.getTargets().contains(entity.getSpawnCategory())
                                && entity instanceof LivingEntity
                                && entity.getLocation().distance(location) < settings.getRange()));

        if (entities.isEmpty()) {
            return;
        }

        final Optional<LivingEntity> closestEntity = getClosestEntity(entities, location);
        closestEntity.ifPresent(livingEntity -> setTarget(location, livingEntity));
    }

    private void updateBarrelTransformation(final Location location, final LivingEntity target) {
        getDisplay(location, "barrel").ifPresent(value -> value.setTransformationMatrix(getBarrelMatrix(location, target.getEyeLocation())));
    }

    private void shoot(final Location location) {
        final Optional<LivingEntity> target = getTarget(location);
        if (target.isEmpty() || target.get().isDead() || location.toCenterLocation().distance(target.get().getLocation()) > settings.getRange()) {
            clearTarget(location);
            return;
        }

        updateBarrelTransformation(location, target.get());
        createProjectile(location.clone().add(barrelLocation), target.get().getEyeLocation());
        target.get().damage(settings.getDamage());
    }

    protected abstract void createProjectile(final Location source, final Location target);
}
