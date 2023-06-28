package org.metamechanists.quaptics.items;

import org.metamechanists.quaptics.utils.Colors;

public enum Tiers {
    PRIMITIVE("Primitive", Colors.PRIMITIVE, 10),
    BASIC("Basic", Colors.BASIC, 100),
    INTERMEDIATE("Intermediate", Colors.INTERMEDIATE, 1000),
    ADVANCED("Advanced", Colors.ADVANCED, 10000);

    public final String coloredName;
    public final float maxPower;

    Tiers(String name, Colors color, float maxPower) {
        this.coloredName = color.getString() + name;
        this.maxPower = maxPower;
    }
}
