package org.metamechanists.death_lasers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;

public class ScaryMathsTicker extends BukkitRunnable {

    public void run() {
        final Location location = new Location(Bukkit.getServer().getWorld("world"), 872, 100, -270);
        if (Bukkit.getServer().getPlayer("bb685dd0-8a71-4506-bc20-24d374fb28b4") == null) {
            return;
        }

        final Location target = Bukkit.getServer().getPlayer("bb685dd0-8a71-4506-bc20-24d374fb28b4").getLocation();

        DisplayUtils.spawnBlockDisplay(location, Material.DISPENSER,
                DisplayUtils.faceTargetTransformation(location, target, new Vector3f(1.0F, 1.0F, 1.0F)));
    }
}
