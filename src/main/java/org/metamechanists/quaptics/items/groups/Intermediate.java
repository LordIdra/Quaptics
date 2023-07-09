package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_2;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_3_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_3_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_3_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_3_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_3_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_3_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_3_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_3_3_SETTINGS;


@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Intermediate {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new EnergyConcentrator(
                Groups.INTERMEDIATE,
                ENERGY_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_2_SETTINGS).register(addon);

        new Lens(
                Groups.INTERMEDIATE,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_3_SETTINGS).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_3_2_SETTINGS).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_3_3_SETTINGS).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_3_2_SETTINGS).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3_3,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_3_3_SETTINGS).register(addon);
    }
}
