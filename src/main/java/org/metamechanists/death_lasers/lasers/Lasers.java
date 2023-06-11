package org.metamechanists.death_lasers.lasers;

import dev.sefiraat.sefilib.entity.display.builders.BlockDisplayBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.util.Vector;
import org.metamechanists.metalib.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Lasers {

    public static final List<Material> concreteTypes = new ArrayList<>();
    static {{
        concreteTypes.add(Material.RED_CONCRETE);
        concreteTypes.add(Material.LIME_CONCRETE);
        concreteTypes.add(Material.BLUE_CONCRETE);
        concreteTypes.add(Material.WHITE_CONCRETE);
    }}

    public static SpawnTimer testTimer() {
        return new SpawnTimer(20);
    }

    public static BlockDisplayBuilder testDisplay() {
        return new BlockDisplayBuilder()
                .setBrightness(new Display.Brightness(15, 15))
                .setBlockData(RandomUtils.randomChoice(concreteTypes).createBlockData())
                .setGroupParentOffset(new Vector());
    }
}
