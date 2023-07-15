package org.metamechanists.quaptics.implementation.blocks;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.SpawnCategory;
import org.jetbrains.annotations.NotNull;
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
    private String comparatorVisual;
    private int repeaterDelay;

    private int energyCapacity;
    private int energyConsumption;

    private double minPower;
    private double powerLoss;
    private double chargeCapacity;
    private double emissionPower;
    private double powerMultiplier;
    private double chargePerShot;

    private double minFrequency;
    private double maxFrequency;
    private double frequencyStep;
    private double frequencyMultiplier;

    private int targetPhase;
    private int targetPhaseSpread;

    private int connections;

    private double useInterval;
    private double timePerItem;
    private int timeToMaxEfficiency;
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
}
