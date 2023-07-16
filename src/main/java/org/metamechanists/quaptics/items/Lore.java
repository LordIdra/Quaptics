package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.utils.LoreBuilder;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.Colors;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class Lore {
    private final double ROUND_TO_INT_THRESHOLD = 0.0001;
    private final String ATTRIBUTE_SYMBOL = "&8⇨ ";
    private final String COUNT_SYMBOL = Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "◎ ";
    private final String RANGE_SYMBOL = Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "↔ ";
    private final String SPEED_SYMBOL = Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "→ ";
    private final String DAMAGE_SYMBOL = Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "🗡 ";
    private final String CHARGE_SYMBOL = Colors.CHARGE.getFormattedColor() + "◆ ";
    private final String POWER_SYMBOL = Colors.POWER.getFormattedColor() + "⏻ ";
    private final String FREQUENCY_SYMBOL = Colors.FREQUENCY.getFormattedColor() + "∀ ";
    private final String PHASE_SYMBOL = Colors.PHASE.getFormattedColor() + "۞ ";
    private final String PERCENTAGE_SUFFIX = " &8%";
    private final String CHARGE_SUFFIX = " &8QEU";
    private final String POWER_SUFFIX = " &8W";
    private final String RANGE_SUFFIX = " &8blocks";
    private final String DAMAGE_SUFFIX = " &8dps";
    private final String FREQUENCY_SUFFIX = " &8Hz";
    private final String SECONDS_SUFFIX = " &8seconds";
    private final String MINUTES_SUFFIX = " &8minutes";
    private final String PHASE_SUFFIX = " &8°";
    private final String MULTIBLOCK_SYMBOL = "&3★";
    private final double SLIMEFUN_TICKS_PER_SECOND = 2.0;

    private @NotNull DecimalFormat getDecimalFormat0dp() {
        final DecimalFormat decimalFormat = new DecimalFormat("#");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat;
    }
    private @NotNull DecimalFormat getDecimalFormat1dp() {
        final DecimalFormat decimalFormat = new DecimalFormat("#.0");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat;
    }
    private @NotNull DecimalFormat getDecimalFormat2dp() {
        final DecimalFormat decimalFormat = new DecimalFormat("#.00");
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        return decimalFormat;
    }

    private String formatZeroDp(final double x) {
        return getDecimalFormat0dp().format(x);
    }
    private String formatOneDp(final double x) {
        return getDecimalFormat1dp().format(x);
    }
    private String formatTwoDp(final double x) {
        return getDecimalFormat2dp().format(x);
    }

    public @NotNull String[] buildChargeableLore(final @NotNull Settings settings, final int charge, final String... description) {
        final List<String> lore = new ArrayList<>();

        Collections.addAll(lore, description);
        lore.add(chargeBar(charge, (int) settings.getChargeCapacity()));
        lore.add(chargeValues(charge, (int) settings.getChargeCapacity()));
        lore.add(chargePerUse((int) settings.getOutputPower()));

        if (settings.getMinFrequency() != 0 || settings.getMaxFrequency() != 0) {
            lore.add(operatingFrequency(settings.getMinFrequency(), settings.getMaxFrequency()));
        }

        return lore.toArray(new String[0]);
    }

    private @NotNull List<String> fromSettings(@NotNull final Settings settings) {
        final List<String> lore = new ArrayList<>();

        if (settings.getTier() != null && !settings.isOperatingPowerHidden()) {
            lore.add(operatingPower(settings.getMinPower(), settings.getTier().maxPower));
        }
        if (settings.getPowerThreshold() != 0) {
            lore.add(powerThreshold(settings.getPowerThreshold()));
        }
        if (settings.getOutputPower() != 0) {
            lore.add(powerOutput(settings.getOutputPower()));
        }
        if (settings.getMaxOutputPower() != 0) {
            lore.add(maxPowerOutput(settings.getMaxOutputPower()));
        }
        if (settings.getPowerLoss() != 0) {
            lore.add(powerLoss(settings.getPowerLoss()));
        }
        if (settings.getPowerMultiplier() != 0) {
            lore.add(powerMultiplier(settings.getPowerMultiplier()));
        }
        if (settings.getChargeCapacity() != 0) {
            lore.add(capacity(settings.getChargeCapacity()));
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
        if (settings.getUseInterval() != 0) {
            lore.add(useInterval(settings.getUseInterval() / QuapticTicker.TICKS_PER_SECOND));
        }
        if (settings.getTimePerItem() != 0) {
            lore.add(timePerItem(settings.getTimePerItem()));
        }
        if (settings.getTimeToMaxEfficiency() != 0) {
            lore.add(timeToMaxEfficiency(settings.getTimeToMaxEfficiency() / 60.0));
        }
        if (settings.getTargetPhase() != 0) {
            lore.add(targetPhase(settings.getTargetPhase()));
        }
        if (settings.getLuckLevel() != 0) {
            lore.add(luckLevel(settings.getLuckLevel()));
        }
        if (settings.getFireResistanceLevel() != 0) {
            lore.add(fireResistanceLevel(settings.getFireResistanceLevel()));
        }
        if (settings.getExperienceMultiplier() != 0) {
            lore.add(experienceMultiplier(settings.getExperienceMultiplier()));
        }
        if (settings.getPowerEfficiency() != 0) {
            lore.add(powerEfficiency(settings.getPowerEfficiency()));
        }
        if (settings.getEnergyConsumption() != 0) {
            lore.add(LoreBuilder.powerPerSecond(settings.getEnergyConsumption()));
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

    // Multiblocks
    public String multiblock() {
        return MULTIBLOCK_SYMBOL + Colors.MULTIBLOCKS.getFormattedColor() + " Multiblock &8(right click with the Multiblock Wand)";
    }
    public String multiblockComponent() {
        return MULTIBLOCK_SYMBOL + Colors.MULTIBLOCKS.getFormattedColor() + " Multiblock Component";
    }

    // Misc
    public String maxConnections(final int maxConnections) {
        return ATTRIBUTE_SYMBOL + COUNT_SYMBOL + "&7Max connections: &e" + Objects.toString(maxConnections);
    }
    public String range(final int range) {
        return ATTRIBUTE_SYMBOL + RANGE_SYMBOL + "&7Range &e" + Objects.toString(range) + RANGE_SUFFIX;
    }
    public String damage(final double damage) {
        return ATTRIBUTE_SYMBOL + DAMAGE_SYMBOL + "&7Damage &e" + formatOneDp(damage/SLIMEFUN_TICKS_PER_SECOND) + DAMAGE_SUFFIX;
    }
    public String useInterval(final double useInterval) {
        return ATTRIBUTE_SYMBOL + SPEED_SYMBOL + "&7Use Interval &e" + formatOneDp(useInterval) + SECONDS_SUFFIX;
    }
    public String timePerItem(final double timePerItem) {
        return ATTRIBUTE_SYMBOL + SPEED_SYMBOL + "&7Time per item &e" + formatOneDp(timePerItem) + SECONDS_SUFFIX;
    }
    public String timeToMaxEfficiency(final double timeToMaxEfficiency) {
        return ATTRIBUTE_SYMBOL + SPEED_SYMBOL + "&7Time to max efficiency &e" + formatZeroDp(timeToMaxEfficiency) + MINUTES_SUFFIX;
    }
    public String luckLevel(final int luckLevel) {
        return ATTRIBUTE_SYMBOL + COUNT_SYMBOL + "&7Luck Level &e" + formatZeroDp(luckLevel);
    }
    public String fireResistanceLevel(final int fireResistanceLevel) {
        return ATTRIBUTE_SYMBOL + COUNT_SYMBOL + "&7Fire Resistance Level &e" + formatZeroDp(fireResistanceLevel);
    }
    public String experienceMultiplier(final double experienceMultiplier) {
        return ATTRIBUTE_SYMBOL + COUNT_SYMBOL + "&7Experience Multiplier &e" + formatZeroDp(experienceMultiplier) + "x";
    }

    // Power
    public String powerInfoPanel(final double power) {
        return POWER_SYMBOL + "&7Power &e" + formatTwoDp(power) + POWER_SUFFIX;
    }
    public String powerOutput(final double powerOutput) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Output &e" + formatZeroDp(powerOutput) + POWER_SUFFIX;
    }
    public String powerThreshold(final double powerThreshold) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Threshold &e" + formatZeroDp(powerThreshold) + POWER_SUFFIX;
    }
    public String maxPowerOutput(final double maxPowerOutput) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Max Power Output &e" + formatZeroDp(maxPowerOutput) + POWER_SUFFIX;
    }
    public String operatingPower(final double minPower, final double maxPower) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Operating Power &e" + formatZeroDp(minPower) + " &7- &e" + formatZeroDp(maxPower) + POWER_SUFFIX;
    }
    public String powerLoss(final double powerLoss) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Loss &e" + formatZeroDp(powerLoss*100) + PERCENTAGE_SUFFIX;
    }
    public String powerMultiplier(final double powerMultiplier) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Multiplier &e" + formatZeroDp(powerMultiplier*100) + PERCENTAGE_SUFFIX;
    }
    public String powerOutput(final double outputPower, final double maxOutputPower) {
        return (outputPower >= maxOutputPower ? "&a" : "&6") + formatZeroDp(outputPower) + "&7 / " + "&a" + formatZeroDp(maxOutputPower);
    }
    public String powerEfficiency(final double powerEfficiency) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Power Efficiency &e" + formatZeroDp(powerEfficiency) + POWER_SUFFIX;
    }

    // Charge
    public String chargeValues(final double charge, final double capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7" + formatZeroDp(charge) + " &8/ &7" + formatZeroDp(capacity) + CHARGE_SUFFIX;
    }
    public String chargeValuesRaw(final double charge, final double capacity) {
        return "&7" + formatZeroDp(charge) + " &8/ &7" + formatZeroDp(capacity) + CHARGE_SUFFIX;
    }
    public String chargePerUse(final double usage) {
        return ATTRIBUTE_SYMBOL + POWER_SYMBOL + "&7Charge per use &e" + formatZeroDp(usage) + POWER_SUFFIX;
    }
    public String capacity(final double capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7Capacity &e" + formatZeroDp(capacity) + CHARGE_SUFFIX;
    }

    // Frequency
    public String frequencyInfoPanel(final double frequency) {
        return FREQUENCY_SYMBOL + "&7Frequency &e" + formatTwoDp(frequency) + FREQUENCY_SUFFIX;
    }
    public String operatingFrequency(final double minFrequency, final double maxFrequency) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Operating Frequency &e" + formatZeroDp(minFrequency)
                + (maxFrequency == 0 ? "+" : " &7- &e" + formatZeroDp(maxFrequency))
                + FREQUENCY_SUFFIX;
    }
    public String frequencyStep(final double frequencyStep) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &e+" + formatZeroDp(frequencyStep) + FREQUENCY_SUFFIX;
    }
    public String frequencyMultiplier(final double frequencyMultiplier) {
        return ATTRIBUTE_SYMBOL + FREQUENCY_SYMBOL + "&7Frequency &ex" + formatZeroDp(frequencyMultiplier);
    }

    // Phase
    public String phaseInfoPanel(final int phase) {
        return PHASE_SYMBOL + "&7Phase &e" + Objects.toString(phase) + PHASE_SUFFIX;
    }
    public String targetPhase(final int targetPhase) {
        return ATTRIBUTE_SYMBOL + PHASE_SYMBOL + "&7Target Phase &e" + Objects.toString(targetPhase) + PHASE_SUFFIX;
    }
    public String phaseChange(final int phaseChange) {
        return ATTRIBUTE_SYMBOL + PHASE_SYMBOL + "&7Phase Change &e" + Objects.toString(phaseChange) + PHASE_SUFFIX;
    }

    // Bar
    public String progressBar(final double value, final double max, final String filledColor, final String emptyColor, final String finishedColor) {
        final String base = "¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦¦";
        if (value >= max) {
            return finishedColor + base;
        }
        final int divideAt = (int) (value/(max/20));
        return filledColor + base.substring(0, divideAt) + emptyColor + base.substring(divideAt);
    }
    public String twoWayProgressBar(final double value, final double max, final String negativeColor, final String positiveColor, final String emptyColor) {
        int divideAt = (int) (value/(max/10));
        String firstHalf = "¦¦¦¦¦¦¦¦¦¦";
        String secondHalf = "¦¦¦¦¦¦¦¦¦¦";

        if (divideAt < 0) {
            divideAt += 10;
            firstHalf = firstHalf.substring(0, divideAt) + negativeColor + firstHalf.substring(divideAt);
        } else if(divideAt > 0) {
            secondHalf = positiveColor + secondHalf.substring(0, divideAt) + emptyColor + secondHalf.substring(divideAt);
        }

        return emptyColor + firstHalf + emptyColor + secondHalf;
    }
    public String thresholdBar(final double inputPower, final double minInputPower) {
        return progressBar(inputPower, minInputPower, "&6", "&7", "&a");
    }
    public String efficiencyBar(final double secondsSinceStarted, final double maxSeconds) {
        return progressBar(secondsSinceStarted, maxSeconds, "&6", "&7", "&a");
    }
    public String chargeBar(final double charge, final double capacity) {
        return ATTRIBUTE_SYMBOL + CHARGE_SYMBOL + "&7Charge " + progressBar(charge, capacity, Colors.CHARGE.getFormattedColor(), "&7", Colors.CHARGE.getFormattedColor());
    }
    public String chargeBarRaw(final double charge, final double capacity) {
        return progressBar(charge, capacity, Colors.CHARGE.getFormattedColor(), "&7", Colors.CHARGE.getFormattedColor());
    }
}
