package org.metamechanists.quaptics.implementation.base;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
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
    public double stepCharge(double charge, double chargeStep) {
        if (charge + chargeStep < 0) {
            return 0;
        }
        return Math.min(charge + chargeStep, capacity);
    }

    // Built in Power Methods
    public boolean checkPower(double power) {
        return this.minPower <= power && power <= tier.maxPower;
    }

    public double powerLoss(double power) {
        return power * (1 - powerLoss);
    }

    // Built in Frequency Methods
    public boolean checkFrequency(double frequency) {
        if (this.minFrequency == 0 && this.maxFrequency == 0) {
            return true;
        }
        return this.minFrequency <= frequency && frequency < this.maxFrequency;
    }

    public double stepFrequency(double frequency) {
        return checkFrequency(frequency) ? frequency + frequencyStep : frequency;
    }

    public double multiplyFrequency(double frequency) {
        return checkFrequency(frequency) ? frequency * frequencyMultiplier : frequency;
    }
}
