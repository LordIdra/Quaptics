package org.metamechanists.quaptics.implementation.blocks.base;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;

@Getter
@Builder
public class Settings {
    Tier tier;

    private float displayRadius;
    private float connectionRadius;

    private double minPower;
    private double powerLoss;
    private double capacity;
    private double emissionPower;

    private double minFrequency;
    private double maxFrequency;
    private double frequencyStep;
    private double frequencyMultiplier;

    private int connections;

    private float projectileSpeed;
    private int range;
    private double damage;
    private Set<SpawnCategory> targets;
    Material projectileMaterial;
    Material mainMaterial;

    // Built in Charge Methods
    public double stepCharge(final double charge, final double chargeStep) {
        if (charge + chargeStep < 0) {
            return 0;
        }
        return Math.min(charge + chargeStep, capacity);
    }

    public double stepDischarge(final double charge) {
       return stepCharge(charge, -emissionPower / QuapticTicker.QUAPTIC_TICKS_PER_SECOND);
    }

    // Built in Power Methods
    public boolean checkPower(final double power) {
        return minPower <= power && power <= tier.maxPower;
    }

    public double powerLoss(final double power) {
        return power * (1 - powerLoss);
    }

    // Built in Frequency Methods
    public boolean checkFrequency(final double frequency) {
        return minFrequency == 0
                && maxFrequency == 0 || minFrequency <= frequency
                && frequency < maxFrequency;
    }

    public double stepFrequency(final double frequency) {
        return checkFrequency(frequency) ? frequency + frequencyStep : frequency;
    }

    public double multiplyFrequency(final double frequency) {
        return checkFrequency(frequency) ? frequency * frequencyMultiplier : frequency;
    }
}
