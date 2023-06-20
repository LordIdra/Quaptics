package org.metamechanists.death_lasers.items;

import org.metamechanists.death_lasers.utils.Colors;

import java.util.Objects;

public class Lore {
    public static String emissionPower(double emissionPower) {
        return "&8⇨ &c⏻ " + Colors.EMISSION_POWER.getString() + "Emission Power &7" + Objects.toString(emissionPower) + "&8W";
    }

    public static String maxPower(double maxPower) {
        return "&8⇨ &c⏻ " + Colors.MAX_POWER + "Max Power &7" + Objects.toString(maxPower) + "&8W";
    }

    public static String powerLoss(double powerLoss) {
        return "&8⇨ &c⏻ " + Colors.POWER_LOSS + "&7Power Loss &7" + Objects.toString(powerLoss) + "&8W";
    }
}
