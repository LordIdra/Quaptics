package org.metamechanists.quaptics.items;

import org.metamechanists.quaptics.utils.Colors;

import java.util.Objects;

public class Lore {
    private final static String attributeSymbol = "&8⇨ ";
    private final static String countSymbol = Colors.COUNT.getString() + "→ ";
    private final static String rangeSymbol = Colors.TURRET.getString() + "↔ ";
    private final static String damageSymbol = Colors.TURRET.getString() + "🗡 ";
    private final static String powerSymbol = Colors.POWER.getString() + "⏻ ";
    private final static String frequencySymbol = Colors.FREQUENCY.getString() + "λ ";
    private final static String phaseSymbol = Colors.PHASE.getString() + "◎ ";

    private final static String percentageSuffix = " &8%";
    private final static String powerSuffix = " &8W";
    private final static String rangeSuffix = " &8blocks";
    private final static String damageSuffix = " &8dps";
    private final static String frequencySuffix = " &8Hz";
    private final static String phaseSuffix = " &8°";

    public static String maxConnections(int connections) {
        return countSymbol + "&7Max Connections &e" + Objects.toString(connections);
    }
    public static String range(int range) {
        return attributeSymbol + rangeSymbol + "&7Range &e" + Objects.toString(range) + rangeSuffix;
    }
    public static String damage(double damage) {
        return attributeSymbol + damageSymbol + "&7Damage &e" + Objects.toString(damage) + damageSuffix;
    }

    public static String powerNoArrow(double power) {
        return powerSymbol + "&7Power &e" + Objects.toString(power) + powerSuffix;
    }
    public static String emissionPower(double emissionPower) {
        return attributeSymbol + powerSymbol + "&7Emission Power &e" + Objects.toString(emissionPower) + powerSuffix;
    }
    public static String powerConsumption(double powerConsumption) {
        return attributeSymbol + powerSymbol + "&7Power Consumption &e" + Objects.toString(powerConsumption) + powerSuffix;
    }
    public static String maxPower(double maxPower) {
        return attributeSymbol + powerSymbol + "&7Max Power &e" + Objects.toString(maxPower) + powerSuffix;
    }
    public static String powerLoss(int powerLossPercentage) {
        return attributeSymbol + powerSymbol + "&7Max Power Loss &e" + Objects.toString(powerLossPercentage) + percentageSuffix;
    }

    public static String frequencyNoArrow(double frequency) {
        return frequencySymbol + "&7Frequency &e" + Objects.toString(frequency) + frequencySuffix;
    }

    public static String phaseNoArrow(int phase) {
        return phaseSymbol + "&7Phase &e" + Objects.toString(phase) + phaseSuffix;
    }
}
