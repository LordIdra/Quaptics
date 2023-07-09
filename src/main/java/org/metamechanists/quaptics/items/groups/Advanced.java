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

import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_3;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_4_SETTINGS;


@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Advanced {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new EnergyConcentrator(
                Groups.ADVANCED,
                ENERGY_CONCENTRATOR_3,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_3_SETTINGS).register(addon);

        new Lens(
                Groups.ADVANCED,
                LENS_4,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_4_SETTINGS).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_4_2_SETTINGS).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4_3,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_4_3_SETTINGS).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4_4,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_4_4_SETTINGS).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_4_2_SETTINGS).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4_3,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_4_3_SETTINGS).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4_4,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_4_4_SETTINGS).register(addon);
    }
}
