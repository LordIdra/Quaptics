package org.metamechanists.quaptics.connections;

import lombok.Getter;
import org.bukkit.Material;

public enum ConnectionPointType {
    INPUT(Material.RED_CONCRETE),
    OUTPUT(Material.LIME_CONCRETE);

    @Getter
    private final Material material;

    ConnectionPointType(final Material material) {
        this.material = material;
    }
}
