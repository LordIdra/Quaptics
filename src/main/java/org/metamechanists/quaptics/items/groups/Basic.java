package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_1;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator.ENERGY_CONCENTRATOR_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_2;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_2_HOSTILE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_2_HOSTILE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_2_PASSIVE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_2_PASSIVE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_2_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_2_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_2_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_2_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_2_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_2_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_2_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_2_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_1;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_1;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_1_SETTINGS;


@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Basic {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(Groups.BASIC, SOLAR_CONCENTRATOR_2, RecipeType.NULL, new ItemStack[] {

                }, SOLAR_CONCENTRATOR_2_SETTINGS).register(addon);

        new EnergyConcentrator(
                Groups.BASIC,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                ENERGY_CONCENTRATOR_1_SETTINGS).register(addon);

        new Lens(
                Groups.BASIC,
                LENS_2,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_2_SETTINGS).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_2_2_SETTINGS).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2_3,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_2_3_SETTINGS).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_2_2_SETTINGS).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2_3,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_2_3_SETTINGS).register(addon);

        new Repeater(
                Groups.BASIC,
                REPEATER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                REPEATER_1_SETTINGS).register(addon);

        new Scatterer(
                Groups.BASIC,
                SCATTERER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                SCATTERER_1_SETTINGS).register(addon);

        new DirectTurret(
                Groups.BASIC,
                TURRET_2_HOSTILE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_2_HOSTILE_SETTINGS).register(addon);

        new DirectTurret(
                Groups.BASIC,
                TURRET_2_PASSIVE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_2_PASSIVE_SETTINGS).register(addon);

        new Transformer(
                Groups.BASIC,
                TRANSFORMER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                TRANSFORMER_1_SETTINGS).register(addon);
    }
}
