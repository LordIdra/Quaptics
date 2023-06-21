package org.metamechanists.death_lasers.items;

import org.metamechanists.death_lasers.utils.Colors;

import java.util.Objects;

public class Lore {
    private final static String attributeSymbol = "&8⇨ ";
    private final static String powerSymbol = Colors.POWER.getString() + "⏻ ";
    private final static String powerSuffix = Colors.POWER.getString() + " &8W";
    private final static String frequencySymbol = Colors.POWER.getString() + "λ ";
    private final static String frequencySuffix = Colors.POWER.getString() + " &8Hz";
    private final static String phaseSymbol = Colors.POWER.getString() + "◎ ";
    private final static String phaseSuffix = Colors.POWER.getString() + " &8°";
    private final static String percentageSuffix = Colors.POWER.getString() + " &8%";

    public static String powerWithoutAttributeSymbol(double power) {
        return powerSymbol + "&7Power &e" + Objects.toString(power) + powerSuffix;
    }
    public static String emissionPower(double emissionPower) {
        return attributeSymbol + powerSymbol + "&7Emission Power &e" + Objects.toString(emissionPower) + powerSuffix;
    }
    public static String maxPower(double maxPower) {
        return attributeSymbol + powerSymbol + "&7Max Power &e" + Objects.toString(maxPower) + powerSuffix;
    }
    public static String powerLoss(int powerLossPercentage) {
        return attributeSymbol + powerSymbol + "&7Max Power Loss &e" + Objects.toString(powerLossPercentage) + percentageSuffix;
    }

    public static String frequencyWithoutAttributeSymbol(double frequency) {
        return frequencySymbol + "&7Frequency &e" + Objects.toString(frequency) + frequencySuffix;
    }

    public static String phaseWithoutAttributeSymbol(int phase) {
        return phaseSymbol + "&7Phase &e" + Objects.toString(phase) + phaseSuffix;
    }
}
