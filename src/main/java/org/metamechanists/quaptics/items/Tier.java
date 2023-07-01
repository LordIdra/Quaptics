package org.metamechanists.quaptics.items;

import org.bukkit.Material;
import org.metamechanists.quaptics.utils.Colors;

public enum Tier {
    PRIMITIVE(Colors.PRIMITIVE.getString() + "Primitive", Material.BROWN_CONCRETE, Material.BROWN_STAINED_GLASS, 10),
    BASIC( Colors.BASIC.getString() + "Basic", Material.GRAY_CONCRETE, Material.GRAY_STAINED_GLASS, 100),
    INTERMEDIATE( Colors.INTERMEDIATE.getString() + "Intermediate", Material.YELLOW_CONCRETE, Material.YELLOW_STAINED_GLASS, 1000),
    ADVANCED( Colors.ADVANCED.getString() + "Advanced", Material.RED_CONCRETE, Material.RED_STAINED_GLASS, 10000);

    public final String name;
    public final Material concreteMaterial;
    public final Material glassMaterial;
    public final double maxPower;

    Tier(final String name, final Material concreteMaterial, final Material glassMaterial, final double maxPower) {
        this.name = name;
        this.concreteMaterial = concreteMaterial;
        this.glassMaterial = glassMaterial;
        this.maxPower = maxPower;
    }
}
