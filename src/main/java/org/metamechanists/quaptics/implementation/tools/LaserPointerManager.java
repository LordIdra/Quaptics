package org.metamechanists.quaptics.implementation.tools;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LaserPointerManager extends BukkitRunnable {
    private static final Map<UUID, LaserPointer.LaserPoint> points = new HashMap<>();
    private static final Vector3f pointerScale = new Vector3f(0.5F, 0.1F, 0.5F);
    public static final long INTERVAL_TICKS = 1;

    @Override
    public void run() {
        for (LaserPointer.LaserPoint point : points.values()) {
            final Player player = Bukkit.getPlayer(point.getPlayerId());
            final BlockDisplay blockDisplay = point.getDisplayID().get();
            if (player == null) {
                continue;
            }

            if (blockDisplay == null) {
                createBlockDisplay(point, player);
                continue;
            }

            updatePoint(point, player, blockDisplay);
        }
    }

    public static LaserPointer.LaserPoint getPoint(UUID playerId) {
        return points.get(playerId);
    }

    public static void addPoint(LaserPointer.LaserPoint point) {
        points.remove(point.getPlayerId());
        points.put(point.getPlayerId(), point);
    }

    public static void removePoint(UUID playerId) {
        points.remove(playerId);
    }

    private static RayTraceResult rayTrace(Player player) {
        return player.getWorld()
                .rayTrace(
                        player.getLocation(),
                        player.getEyeLocation().toVector(),
                        10,
                        FluidCollisionMode.ALWAYS,
                        false,
                        0.5,
                        (entity -> false));
    }

    private static void createBlockDisplay(LaserPointer.LaserPoint point, Player player) {
        final RayTraceResult rayTraceResult = rayTrace(player);
        final BlockDisplay display = new BlockDisplayBuilder(
                new Location(player.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ()))
                .setTransformation(Transformations.adjustedScale(pointerScale))
                .setMaterial(point.getColor().getMaterial())
                .setBrightness(15)
                .build();

        point.setDisplayID(new BlockDisplayID(display.getUniqueId()));
    }

    private static void updatePoint(LaserPointer.LaserPoint point, Player player, BlockDisplay blockDisplay) {
        final RayTraceResult rayTraceResult = rayTrace(player);
        blockDisplay.teleport(new Location(player.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ()));

        if (point.isUpdated()) {
            blockDisplay.setBlock(point.getColor().getMaterial().createBlockData());
            point.setUpdated(false);
        }
    }
}
