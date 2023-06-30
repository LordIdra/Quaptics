package org.metamechanists.quaptics.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.joml.Vector3f;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LaserPointerManager extends BukkitRunnable implements Listener {
    private static final Map<UUID, LaserPointer.LaserPoint> points = new HashMap<>();
    private static final Vector3f pointerScale = new Vector3f(0.1F, 0.1F, 0.1F);
    public static final long INTERVAL_TICKS = 1;

    @Override
    public void run() {
        for (LaserPointer.LaserPoint point : points.values()) {
            final Player player = Bukkit.getPlayer(point.getPlayerId());
            final BlockDisplayID displayID = point.getDisplayID();
            if (player == null) {
                continue;
            }

            if (displayID == null || displayID.get() == null) {
                createBlockDisplay(point, player);
                continue;
            }

            updatePoint(point, player, displayID.get());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerScroll(PlayerItemHeldEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final Player player = event.getPlayer();
        final ItemStack itemStack = player.getInventory().getItem(event.getPreviousSlot());
        if (itemStack == null || itemStack.getType().isEmpty()) {
            return;
        }

        if (!(SlimefunItem.getByItem(itemStack) instanceof LaserPointer) || getPoint(player.getUniqueId()) == null) {
            return;
        }

        LaserPointer.togglePointer(player);
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

    public static void removePoints() {
        points.values().forEach(point -> {
            final BlockDisplayID displayID = point.getDisplayID();
            if (displayID == null) {
                return;
            }

            final BlockDisplay blockDisplay = displayID.get();
            if (blockDisplay != null) {
                blockDisplay.remove();
            }
        });
    }

    private static RayTraceResult rayTrace(Player player) {
        return player.getWorld()
                .rayTrace(
                        player.getEyeLocation(),
                        player.getEyeLocation().getDirection(),
                        32,
                        FluidCollisionMode.ALWAYS,
                        true,
                        0.1,
                        (entity -> false));
    }

    private static void createBlockDisplay(LaserPointer.LaserPoint point, Player player) {
        final RayTraceResult rayTraceResult = rayTrace(player);
        if (rayTraceResult == null) {
            return;
        }

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
        if (rayTraceResult == null) {
            return;
        }

        if (point.isUpdated()) {
            blockDisplay.setBlock(point.getColor().getMaterial().createBlockData());
            point.setUpdated(false);
        }

        blockDisplay.teleport(new Location(player.getWorld(), rayTraceResult.getHitPosition().getX(), rayTraceResult.getHitPosition().getY(), rayTraceResult.getHitPosition().getZ()));
    }
}
