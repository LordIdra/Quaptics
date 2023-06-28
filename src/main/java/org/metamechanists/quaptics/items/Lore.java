package org.metamechanists.quaptics.items;

import org.metamechanists.quaptics.utils.Colors;

import java.util.Objects;

public class Lore {
    private final static String ATTRIBUTE_SYMBOL = "&8⇨ ";
    private final static String RANGE_SYMBOL = Colors.TURRET.getString() + "↔ ";
    private final static String DAMAGE_SYMBOL = Colors.TURRET.getString() + "🗡 ";
    private final static String POWER_SYMBOL = Colors.POWER.getString() + "⏻ ";
    private final static String FREQUENCY_SYMBOL = Colors.FREQUENCY.getString() + "λ ";
    private final static String PHASE_SYMBOL = Colors.PHASE.getString() + "◎ ";

    private final static String PERCENTAGE_SUFFIX = " &8%";
    private final static String POWER_SUFFIX = " &8W";
    private final static String RANGE_SUFFIX = " &8blocks";
    private final static String DAMAGE_SUFFIX = " &8dps";
    private final static String FREQUENCY_SUFFIX = " &8Hz";
    private final static String PHASE_SUFFIX = " &8°";

    private static String format(double x) {
        return Objects.toString(((int) x) == x
                ? (int) x
                : x);
    }

    public static String range(int range) {
        return ATTRIBUTE_SYMBOL + RANGE_SYMBOL + "&7Range &e" + Objects.toString(range) + RANGE_SUFFIX;
    }
    public static String damage(double damage) {
        return ATTRIBUTE_SYMBOL + DAMAGE_SYMBOL + "&7Damage &e" + format(damage) + DAMAGE_SUFFIX;
    }

    public static String powerNoArrow(double power) {
        return POWER_SYMBOL + "&7Power &e" + Objects.toString(power) + POWER_SUFFIX;
    }
    public static String emissionPower(double emissionPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Emission Power &e" + format(emissionPower) + POWER_SUFFIX;
    }
    public static String minPower(double minPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Min Power &e" + format(minPower) + POWER_SUFFIX;
    }
    public static String maxPower(double maxPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Max Power &e" + format(maxPower) + POWER_SUFFIX;
    }
    public static String powerLoss(int powerLossPercentage) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Loss &e" + Objects.toString(powerLossPercentage) + PERCENTAGE_SUFFIX;
    }

    public static String frequencyNoArrow(double frequency) {
        return FREQUENCY_SYMBOL + "&7Frequency &e" + Objects.toString(frequency) + FREQUENCY_SUFFIX;
    }

    public static String phaseNoArrow(int phase) {
        return PHASE_SYMBOL + "&7Phase &e" + Objects.toString(phase) + PHASE_SUFFIX;
    }
}
