package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.testing.OscillatingConcentrator;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

@UtilityClass
public class Testing {

    private final Settings OSCILLATING_CONCENTRATOR_SETTINGS = Settings.builder()
            .tier(Tier.TESTING)
            .displayRadius(0.45F)
            .connectionRadius(0.45F)
            .emissionPower(1)
            .build();

    private final SlimefunItemStack OSCILLATING_CONCENTRATOR = new SlimefunItemStack(
            "QP_TESTING_OSCILLATING_CONCENTRATOR",
            Material.BLACK_STAINED_GLASS_PANE,
            "&8Oscillating Concentrator &FI",
            Lore.create(OSCILLATING_CONCENTRATOR_SETTINGS,
                    "&7● Toggles power on/off every tick",
                    "&7● Concentrates epic admin hax into a quaptic ray"));

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new OscillatingConcentrator(
                Groups.PRIMITIVE,
                OSCILLATING_CONCENTRATOR,
                RecipeType.NULL,
                new ItemStack[]{},
                OSCILLATING_CONCENTRATOR_SETTINGS).register(addon);
    }
}
