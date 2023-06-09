package org.metamechanists.death_lasers.lasers;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.util.Vector;

public class Lasers {
    public static final SpawnTimer testTimer = new SpawnTimer(5);

    public static final BlockDisplay testDisplay = new BlockDisplayBuilder()
            .setBlockData(Material.RED_CONCRETE.createBlockData())
            .setGroupParentOffset(new Vector())
            .build();
}
