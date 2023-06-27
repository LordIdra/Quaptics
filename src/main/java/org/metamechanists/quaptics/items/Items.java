package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Turret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.metamechanists.quaptics.items.ItemStacks.*;

public class Items {
    public static final TargetingWand targetingWand = new TargetingWand(
            Groups.TOOLS,
            TARGETING_WAND,
            RecipeType.NULL,
            new ItemStack[] {});

    @Getter
    private static final Map<String, ConnectedBlock> blocks = new LinkedHashMap<>();

    static {{
        blocks.put("SOLAR_CONCENTRATOR_1", new SolarConcentrator(
                Groups.PRIMITIVE,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                1));

        blocks.put("SOLAR_CONCENTRATOR_2", new SolarConcentrator(
                Groups.BASIC,
                SOLAR_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                10));

        blocks.put("ENERGY_CONCENTRATOR_1", new EnergyConcentrator(
                Groups.BASIC,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                30,
                30,
                15));

        blocks.put("ENERGY_CONCENTRATOR_2", new EnergyConcentrator(
                Groups.INTERMEDIATE,
                ENERGY_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                160,
                160,
                200));

        blocks.put("ENERGY_CONCENTRATOR_3", new EnergyConcentrator(
                Groups.ADVANCED,
                ENERGY_CONCENTRATOR_3,
                RecipeType.NULL,
                new ItemStack[]{},
                680,
                680,
                2500));

        blocks.put("QP_LENS_1", new Lens(
                Groups.PRIMITIVE,
                LENS_1,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                0.1));

        blocks.put("QP_LENS_2", new Lens(
                Groups.BASIC,
                LENS_2,
                RecipeType.NULL,
                new ItemStack[]{},
                100,
                0.07));

        blocks.put("QP_LENS_3", new Lens(
                Groups.INTERMEDIATE,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                0.04));

        blocks.put("QP_LENS_4", new Lens(
                Groups.ADVANCED,
                LENS_4,
                RecipeType.NULL,
                new ItemStack[]{},
                10000,
                0.02));

        blocks.put("QP_COMBINER_1", new Combiner(
                Groups.PRIMITIVE,
                COMBINER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                0.2));

        blocks.put("QP_COMBINER_2", new Combiner(
                Groups.BASIC,
                COMBINER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                100,
                0.14));

        blocks.put("QP_COMBINER_3", new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                0.08));

        blocks.put("QP_COMBINER_4", new Combiner(
                Groups.ADVANCED,
                COMBINER_4,
                RecipeType.NULL,
                new ItemStack[]{},
                10000,
                0.05));

        blocks.put("QP_SPLITTER_1", new Combiner(
                Groups.PRIMITIVE,
                SPLITTER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                0.2));

        blocks.put("QP_SPLITTER_2", new Combiner(
                Groups.BASIC,
                SPLITTER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                100,
                0.14));

        blocks.put("QP_SPLITTER_3", new Combiner(
                Groups.INTERMEDIATE,
                SPLITTER_3,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                0.08));

        blocks.put("QP_SPLITTER_4", new Combiner(
                Groups.ADVANCED,
                SPLITTER_4,
                RecipeType.NULL,
                new ItemStack[]{},
                10000,
                0.05));

        blocks.put("QP_TURRET", new Turret(
                Groups.PRIMITIVE,
                TURRET,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                5,
                10,
                2));
    }}

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        targetingWand.register(addon);

        blocks.values().forEach(block -> block.register(addon));
    }
}
