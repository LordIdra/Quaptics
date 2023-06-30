package org.metamechanists.quaptics.items;

import org.bukkit.Material;
import org.metamechanists.quaptics.utils.Colors;

public enum Tier {
    PRIMITIVE(Colors.PRIMITIVE.getString() + "Primitive", Material.BROWN_CONCRETE, 10),
    BASIC( Colors.BASIC.getString() + "Basic", Material.GRAY_CONCRETE, 100),
    INTERMEDIATE( Colors.INTERMEDIATE.getString() + "Intermediate", Material.YELLOW_CONCRETE, 1000),
    ADVANCED( Colors.ADVANCED.getString() + "Advanced", Material.RED_CONCRETE, 10000);

    public final String name;
    public final Material material;
    public final double maxPower;

    Tier(String name, Material material, double maxPower) {
        this.name = name;
        this.material = material;
        this.maxPower = maxPower;
    }
}
