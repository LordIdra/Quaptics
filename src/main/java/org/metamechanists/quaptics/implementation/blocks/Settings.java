package org.metamechanists.quaptics.implementation.blocks;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.QuapticTicker;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.items.Tier;

import java.util.Optional;
import java.util.Set;

@Getter
@Builder
public class Settings {
    Tier tier;

    private float displayRadius;
    private float connectionRadius;
    private float rotationY;

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

    public boolean isOperational(final @NotNull Link inputLink) {
        return inputLink.isEnabled()
                && inputLink.getPower() >= minPower
                && inputLink.getPower() <= tier.maxPower
                && inputLink.getFrequency() >= minFrequency
                && ((maxFrequency == 0) || (inputLink.getFrequency() <= maxFrequency));
    }

    public boolean isOperational(final @NotNull Optional<? extends Link> inputLink) {
        return inputLink.isPresent() && isOperational(inputLink.get());
    }



    public double stepCharge(final double charge, final double chargeStep) {
        if (charge + chargeStep < 0) {
            return 0;
        }
        return Math.min(charge + chargeStep, capacity);
    }

    public double stepDischarge(final double charge) {
       return stepCharge(charge, -emissionPower / QuapticTicker.QUAPTIC_TICKS_PER_SECOND);
    }
}
