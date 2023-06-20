package org.metamechanists.death_lasers.items;

import org.metamechanists.death_lasers.utils.Colors;

import java.util.Objects;

public class Lore {
    public static String emissionPower(double emissionPower) {
        return "&8⇨ " + Colors.EMISSION_POWER.getString() + "⏻ &7Emission Power &e" + Objects.toString(emissionPower) + " &8W";
    }

    public static String maxPower(double maxPower) {
        return "&8⇨ " + Colors.MAX_POWER.getString() + "⏻ &7Max Power &e" + Objects.toString(maxPower) + " &8W";
    }

    public static String powerLoss(double powerLoss) {
        return "&8⇨ " + Colors.POWER_LOSS.getString() + "⏻ &7Power Loss &e" + Objects.toString(powerLoss) + " &8W";
    }
}
