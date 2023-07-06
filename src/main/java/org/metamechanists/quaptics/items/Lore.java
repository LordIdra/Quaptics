package org.metamechanists.quaptics.items;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.utils.Colors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class Lore {
    private final double ROUND_TO_INT_THRESHOLD = 0.0001;
    private final String ATTRIBUTE_SYMBOL = "&8⇨ ";
    private final String COUNT_SYMBOL = Colors.COMPONENTS_MISC.getFormattedColor() + "◎ ";
    private final String RANGE_SYMBOL = Colors.COMPONENTS_MISC.getFormattedColor() + "↔ ";
    private final String SPEED_SYMBOL = Colors.COMPONENTS_MISC.getFormattedColor() + "→ ";
    private final String DAMAGE_SYMBOL = Colors.COMPONENTS_MISC.getFormattedColor() + "🗡 ";
    private final String CHARGE_SYMBOL = Colors.CHARGE.getFormattedColor() + "◆ ";
    private final String POWER_SYMBOL = Colors.POWER.getFormattedColor() + "⏻ ";
    private final String FREQUENCY_SYMBOL = Colors.FREQUENCY.getFormattedColor() + "∀ ";
    private final String PHASE_SYMBOL = Colors.PHASE.getFormattedColor() + "۞ ";

    private final String PERCENTAGE_SUFFIX = " &8%";
    private final String CHARGE_SUFFIX = " &8QEU";
    private final String POWER_SUFFIX = " &8W";
    private final String RANGE_SUFFIX = " &8blocks";
    private final String SPEED_SUFFIX = " &8blocks/s";
    private final String DAMAGE_SUFFIX = " &8dps";
    private final String FREQUENCY_SUFFIX = " &8Hz";
    private final String USE_INTERVAL_SUFFIX = " &8seconds";
    private final String PHASE_SUFFIX = " &8°";
    private final double SLIMEFUN_TICKS_PER_SECOND = 2.0;

    private String format(final double x) {
        return Objects.toString(Math.abs((int)(x) - x) < ROUND_TO_INT_THRESHOLD
                ? (int) x
                : x);
    }

    public @NotNull String[] buildChargeableLore(final @NotNull Settings settings, final int charge, final String... description) {
        final List<String> lore = new ArrayList<>();

        Collections.addAll(lore, description);
        lore.add(chargeBar(charge, (int) settings.getCapacity()));
        lore.add(chargeValues(charge, (int) settings.getCapacity()));
        lore.add(chargeUsage((int) settings.getEmissionPower()));

        if (settings.getMinFrequency() != 0 || settings.getMaxFrequency() != 0) {
            lore.add(operatingFrequency(settings.getMinFrequency(), settings.getMaxFrequency()));
        }

        return lore.toArray(new String[0]);
    }

    private @NotNull List<String> fromSettings(@NotNull final Settings settings) {
        final List<String> lore = new ArrayList<>();

        if (settings.getTier() != null && (settings.getEmissionPower() == 0 || settings.getCapacity() != 0)) {
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
        if (settings.getProjectileSpeed() != 0) {
            lore.add(projectileSpeed(settings.getProjectileSpeed()));
        }
        if (settings.getDamage() != 0) {
            lore.add(damage(settings.getDamage()));
        }
        if (settings.getUseInterval() != 0) {
            lore.add(useInterval(settings.getUseInterval()));
        }

        return lore;
    }
    public @NotNull String[] create(@NotNull final Settings settings, final String... description) {
        final List<String> lore = new ArrayList<>();
        lore.add(settings.getTier().name);
        Collections.addAll(lore, description);
        lore.addAll(fromSettings(settings));
        return lore.toArray(new String[0]);
    }

    public String maxConnections(final int maxConnections) {
        return ATTRIBUTE_SYMBOL + COUNT_SYMBOL + "&7Max connections: &e" + Objects.toString(maxConnections);
    }
    public String range(final int range) {
        return ATTRIBUTE_SYMBOL + RANGE_SYMBOL + "&7Range &e" + Objects.toString(range) + RANGE_SUFFIX;
    }
    public String projectileSpeed(final double speed) {
        return ATTRIBUTE_SYMBOL + SPEED_SYMBOL + "&7Projectile Speed &e" + format(speed) + SPEED_SUFFIX;
    }
    public String damage(final double damage) {
        return ATTRIBUTE_SYMBOL + DAMAGE_SYMBOL + "&7Damage &e" + format(damage/SLIMEFUN_TICKS_PER_SECOND) + DAMAGE_SUFFIX;
    }
    public String useInterval(final double useInterval) {
        return ATTRIBUTE_SYMBOL + SPEED_SYMBOL + "&7Use Interval &e" + format(useInterval) + USE_INTERVAL_SUFFIX;
    }

    public String capacity(final double capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7Capacity &e" + Objects.toString(capacity) + CHARGE_SUFFIX;
    }

    public String powerNoArrow(final double power) {
        return POWER_SYMBOL + "&7Power &e" + Objects.toString(power) + POWER_SUFFIX;
    }
    public String emissionPower(final double emissionPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Emission Power &e" + format(emissionPower) + POWER_SUFFIX;
    }
    public String operatingPower(final double minPower, final double maxPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Operating Power &e" + format(minPower) + " &7- &e" + format(maxPower) + POWER_SUFFIX;
    }
    public String powerLoss(final double powerLoss) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Loss &e" + format(powerLoss*100) + PERCENTAGE_SUFFIX;
    }

    public String chargeBar(final int charge, final int capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7Charge " + progressBar(charge, capacity, Colors.CHARGE.getFormattedColor(), "&7");
    }
    public String chargeBarRaw(final int charge, final int capacity) {
        return progressBar(charge, capacity, Colors.CHARGE.getFormattedColor(), "&7");
    }
    public String chargeValues(final int charge, final int capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7" + charge + " &8/ &7" + capacity + CHARGE_SUFFIX;
    }
    public String chargeValuesRaw(final int charge, final int capacity) {
        return "&7" + charge + " &8/ &7" + capacity + CHARGE_SUFFIX;
    }
    public String chargeUsage(final int usage) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Usage &e" + usage + POWER_SUFFIX;
    }

    public String frequencyNoArrow(final double frequency) {
        return FREQUENCY_SYMBOL + "&7Frequency &e" + Objects.toString(frequency) + FREQUENCY_SUFFIX;
    }
    public String operatingFrequency(final double minFrequency, final double maxFrequency) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Operating Frequency &e" + format(minFrequency)
                + (maxFrequency == 0 ? "+" : " &7- &e" + format(maxFrequency))
                + FREQUENCY_SUFFIX;
    }
    public String frequencyStep(final double frequencyStep) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &e+" + format(frequencyStep) + FREQUENCY_SUFFIX;
    }
    public String frequencyMultiplier(final double frequencyMultiplier) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &ex" + format(frequencyMultiplier);
    }

    public String phaseNoArrow(final int phase) {
        return PHASE_SYMBOL + "&7Phase &e" + Objects.toString(phase) + PHASE_SUFFIX;
    }

    public String progressBar(final int filled, final int max, final String filledColor, final String emptyColor) {
        final String base = "¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦";
        final int divideAt = filled/(max/20);
        return filledColor + base.substring(0, divideAt) + emptyColor + base.substring(divideAt);
    }
}
