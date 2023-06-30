package org.metamechanists.quaptics.items;

import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Lore {
    private static final double ROUND_TO_INT_THRESHOLD = 0.0001;
    private static final String ATTRIBUTE_SYMBOL = "&8⇨ ";
    private static final String COUNT_SYMBOL = Colors.COMPONENTS_MISC.getString() + "◎ ";
    private static final String RANGE_SYMBOL = Colors.COMPONENTS_MISC.getString() + "↔ ";
    private static final String DAMAGE_SYMBOL = Colors.COMPONENTS_MISC.getString() + "🗡 ";
    private static final String CHARGE_SYMBOL = Colors.CHARGE.getString() + "◆ ";
    private static final String POWER_SYMBOL = Colors.POWER.getString() + "⏻ ";
    private static final String FREQUENCY_SYMBOL = Colors.FREQUENCY.getString() + "∀ ";
    private static final String PHASE_SYMBOL = Colors.PHASE.getString() + "۞ ";

    private static final String PERCENTAGE_SUFFIX = " &8%";
    private static final String CHARGE_SUFFIX = " &8QEU";
    private static final String POWER_SUFFIX = " &8W";
    private static final String RANGE_SUFFIX = " &8blocks";
    private static final String DAMAGE_SUFFIX = " &8dps";
    private static final String FREQUENCY_SUFFIX = " &8Hz";
    private static final String PHASE_SUFFIX = " &8°";

    private static String format(double x) {
        return Objects.toString(Math.abs((int)(x) - x) < ROUND_TO_INT_THRESHOLD
                ? (int) x
                : x);
    }

    public static @NotNull String[] buildChargeableLore(ConnectedBlock.Settings settings, int filled, String... description) {
        final List<String> lore = new ArrayList<>();

        Collections.addAll(lore, description);
        lore.add(chargeBar(filled, (int) settings.getCapacity()));
        lore.add(chargeValues(filled, (int) settings.getCapacity()));
        lore.add(chargeUsage((int) settings.getEmissionPower()));

        if (settings.getMinFrequency() != 0 || settings.getMaxFrequency() != 0) {
            lore.add(operatingFrequency(settings.getMinFrequency(), settings.getMaxFrequency()));
        }

        return lore.toArray(new String[0]);
    }

    private static @NotNull List<String> fromSettings(@NotNull ConnectedBlock.Settings settings) {
        final List<String> lore = new ArrayList<>();

        if (settings.getTier() != null && settings.getCapacity() != 0) {
            lore.add(operatingPower(settings.getMinPower(), settings.getTier().maxPower));
        }
        if (settings.getEmissionPower() != 0) {
            lore.add(emissionPower(settings.getEmissionPower()));
        }
        if (settings.getPowerLoss() != 0) {
            lore.add(powerLoss(settings.getPowerLoss()));
        }
        if (settings.getCapacity() != 0) {
            lore.add(capacity(settings.getCapacity()));
        }
        if (settings.getMaxFrequency() != 0 || settings.getMinFrequency() != 0) {
            lore.add(operatingFrequency(settings.getMinFrequency(), settings.getMaxFrequency()));
        }
        if (settings.getFrequencyStep() != 0) {
            lore.add(frequencyStep(settings.getFrequencyStep()));
        }
        if (settings.getFrequencyMultiplier() != 0) {
            lore.add(frequencyMultiplier(settings.getFrequencyMultiplier()));
        }
        if (settings.getConnections() != 0) {
            lore.add(maxConnections(settings.getConnections()));
        }
        if (settings.getRange() != 0) {
            lore.add(range(settings.getRange()));
        }
        if (settings.getDamage() != 0) {
            lore.add(damage(settings.getDamage()));
        }

        return lore;
    }
    public static @NotNull String[] create(ConnectedBlock.@NotNull Settings settings, String... description) {
        final List<String> lore = new ArrayList<>();
        lore.add(settings.getTier().name);
        Collections.addAll(lore, description);
        lore.addAll(fromSettings(settings));
        return lore.toArray(new String[0]);
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

    public static String capacity(double capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7Capacity &e" + Objects.toString(capacity) + CHARGE_SUFFIX;
    }

    public static String powerNoArrow(double power) {
        return POWER_SYMBOL + "&7Power &e" + Objects.toString(power) + POWER_SUFFIX;
    }
    public static String emissionPower(double emissionPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Emission Power &e" + format(emissionPower) + POWER_SUFFIX;
    }
    public static String operatingPower(double minPower, double maxPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Operating Power &e" + format(minPower) + " &7- &e" + format(maxPower) + POWER_SUFFIX;
    }
    public static String chargeBar(int filled, int max) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7Charge " + progressBar(filled, max, "&c", "7");
    }
    public static String chargeValues(int filled, int max) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7" + filled + "&8/ &7" + max + CHARGE_SUFFIX;
    }
    public static String chargeUsage(int usage) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Usage &e" + usage + POWER_SUFFIX;
    }
    public static String powerLoss(double powerLoss) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Loss &e" + format(powerLoss*100) + PERCENTAGE_SUFFIX;
    }

    public static String frequencyNoArrow(double frequency) {
        return FREQUENCY_SYMBOL + "&7Frequency &e" + Objects.toString(frequency) + FREQUENCY_SUFFIX;
    }
    public static String operatingFrequency(double minFrequency, double maxFrequency) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Operating Frequency &e" + format(minFrequency) + " &7- &e" + format(maxFrequency) + FREQUENCY_SUFFIX;
    }
    public static String frequencyStep(double frequencyStep) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &e+" + format(frequencyStep) + FREQUENCY_SUFFIX;
    }
    public static String frequencyMultiplier(double frequencyMultiplier) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &ex" + format(frequencyMultiplier);
    }

    public static String phaseNoArrow(int phase) {
        return PHASE_SYMBOL + "&7Phase &e" + Objects.toString(phase) + PHASE_SUFFIX;
    }

    public static String progressBar(int filled, int max, String filledColor, String emptyColor) {
        final String base = "¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦";
        final int divideAt = max%20/filled;
        return filledColor + base.substring(0, divideAt) + emptyColor + base.substring(divideAt);
    }
}
