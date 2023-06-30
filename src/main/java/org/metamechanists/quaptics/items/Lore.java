package org.metamechanists.quaptics.items;

import org.metamechanists.quaptics.utils.Colors;

import java.util.Objects;

public class Lore {
    private static final String ATTRIBUTE_SYMBOL = "&8⇨ ";
    private static final String COUNT_SYMBOL = Colors.COMPONENTS_MISC.getString() + "◎ ";
    private static final String RANGE_SYMBOL = Colors.COMPONENTS_MISC.getString() + "↔ ";
    private static final String DAMAGE_SYMBOL = Colors.COMPONENTS_MISC.getString() + "🗡 ";
    private static final String POWER_SYMBOL = Colors.POWER.getString() + "⏻ ";
    private static final String FREQUENCY_SYMBOL = Colors.FREQUENCY.getString() + "∀ ";
    private static final String PHASE_SYMBOL = Colors.PHASE.getString() + "۞ ";

    private static final String PERCENTAGE_SUFFIX = " &8%";
    private static final String POWER_SUFFIX = " &8W";
    private static final String RANGE_SUFFIX = " &8blocks";
    private static final String DAMAGE_SUFFIX = " &8dps";
    private static final String FREQUENCY_SUFFIX = " &8Hz";
    private static final String PHASE_SUFFIX = " &8°";

    private static String format(double x) {
        return Objects.toString(((int) x) == x
                ? (int) x
                : x);
    }

    public static String maxConnections(int maxConnections) {
        return ATTRIBUTE_SYMBOL + COUNT_SYMBOL + "&7Max connections: &e" + Objects.toString(maxConnections);
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

    public static String frequencyStep(double frequency) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &e+" + format(frequency) + FREQUENCY_SUFFIX;
    }
    public static String minFrequency(double minFrequency) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Min Frequency &e" + format(minFrequency) + FREQUENCY_SUFFIX;
    }
    public static String maxFrequency(double maxFrequency) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Max Frequency &e" + format(maxFrequency) + FREQUENCY_SUFFIX;
    }
    public static String phase(double phase) {
        return ATTRIBUTE_SYMBOL + PHASE_SYMBOL + "&7Phase &e" + format(phase) + PHASE_SUFFIX;
    }
    public static String minPhase(double minPhase) {
        return ATTRIBUTE_SYMBOL + PHASE_SYMBOL + "&7Min Phase &e" + format(minPhase) + PHASE_SUFFIX;
    }
    public static String maxPhase(double maxPhase) {
        return ATTRIBUTE_SYMBOL + PHASE_SYMBOL + "&7Max Phase &e" + format(maxPhase) + PHASE_SUFFIX;
    }

    public static String frequencyNoArrow(double frequency) {
        return FREQUENCY_SYMBOL + "&7Frequency &e" + Objects.toString(frequency) + FREQUENCY_SUFFIX;
    }

    public static String phaseNoArrow(int phase) {
        return PHASE_SYMBOL + "&7Phase &e" + Objects.toString(phase) + PHASE_SUFFIX;
    }
}
