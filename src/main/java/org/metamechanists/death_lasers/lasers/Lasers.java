package org.metamechanists.death_lasers.lasers;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;

public class Lasers {
    public static final SpawnTimer testTimer = new SpawnTimer(5);

    public static BlockDisplayBuilder testDisplay() {
        return new BlockDisplayBuilder()
                .setBrightness(new Display.Brightness(4, 0))
                .setBlockData(Material.RED_CONCRETE.createBlockData())
                .setGroupParentOffset(new Vector());
    }
}
