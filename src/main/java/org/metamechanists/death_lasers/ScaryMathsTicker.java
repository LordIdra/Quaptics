package org.metamechanists.death_lasers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;

import java.util.UUID;

public class ScaryMathsTicker extends BukkitRunnable {

    BlockDisplay display = null;

    public void run() {
        final Location location = new Location(Bukkit.getServer().getWorld("world"), 872, 100, -270);
        final Location target = Bukkit.getServer().getPlayer(UUID.fromString("bb685dd0-8a71-4506-bc20-24d374fb28b4")).getLocation();

        if (display == null || display.isDead()) {
            display = DisplayUtils.spawnBlockDisplay(location, Material.DISPENSER,
                    DisplayUtils.faceTargetTransformation(location, target, new Vector3f(1.0F, 4.0F, 1.0F)));
        } else {
            display.setTransformation(DisplayUtils.faceTargetTransformation(location, target, new Vector3f(1.0F, 4.0F, 1.0F)));
        }
    }
}
