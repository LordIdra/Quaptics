package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_1;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_2;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_1;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_2;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_2_SETTINGS;


@UtilityClass
public class BeamCreation {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(Groups.BEAM_CREATION, SOLAR_CONCENTRATOR_1, RecipeType.NULL, new ItemStack[]{

        }, SOLAR_CONCENTRATOR_1_SETTINGS).register(addon);

        new SolarConcentrator(Groups.BEAM_CREATION, SOLAR_CONCENTRATOR_2, RecipeType.NULL, new ItemStack[]{

        }, SOLAR_CONCENTRATOR_2_SETTINGS).register(addon);

        new EnergyConcentrator(Groups.BEAM_CREATION, ENERGY_CONCENTRATOR_1, RecipeType.NULL, new ItemStack[]{

        }, ENERGY_CONCENTRATOR_1_SETTINGS).register(addon);

        new EnergyConcentrator(Groups.BEAM_CREATION, ENERGY_CONCENTRATOR_2, RecipeType.NULL, new ItemStack[]{

        }, ENERGY_CONCENTRATOR_2_SETTINGS).register(addon);
    }
}
