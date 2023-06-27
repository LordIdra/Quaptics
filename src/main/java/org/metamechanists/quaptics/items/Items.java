package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.*;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.metamechanists.quaptics.items.ItemStacks.*;

public class Items {
    public static final TargetingWand targetingWand = new TargetingWand(
            Groups.MAIN_GROUP,
            TARGETING_WAND,
            RecipeType.NULL,
            new ItemStack[] {});

    @Getter
    private static final Map<String, ConnectedBlock> blocks = new LinkedHashMap<>();

    static {{
        blocks.put("SOLAR_CONCENTRATOR_1", new SolarConcentrator(
                Groups.MAIN_GROUP,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                1));

        blocks.put("SOLAR_CONCENTRATOR_2", new SolarConcentrator(
                Groups.MAIN_GROUP,
                SOLAR_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                10));

        blocks.put("ENERGY_CONCENTRATOR_1", new EnergyConcentrator(
                Groups.MAIN_GROUP,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                30,
                30,
                15));

        blocks.put("ENERGY_CONCENTRATOR_2", new EnergyConcentrator(
                Groups.MAIN_GROUP,
                ENERGY_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                160,
                160,
                200));

        blocks.put("ENERGY_CONCENTRATOR_3", new EnergyConcentrator(
                Groups.MAIN_GROUP,
                ENERGY_CONCENTRATOR_3,
                RecipeType.NULL,
                new ItemStack[]{},
                680,
                680,
                2500));

        blocks.put("QP_LENS_1", new Lens(
                Groups.MAIN_GROUP,
                LENS_1,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                0.1));

        blocks.put("QP_LENS_2", new Lens(
                Groups.MAIN_GROUP,
                LENS_2,
                RecipeType.NULL,
                new ItemStack[]{},
                100,
                0.07));

        blocks.put("QP_LENS_3", new Lens(
                Groups.MAIN_GROUP,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                0.04));

        blocks.put("QP_LENS_4", new Lens(
                Groups.MAIN_GROUP,
                LENS_4,
                RecipeType.NULL,
                new ItemStack[]{},
                10000,
                0.02));

        blocks.put("QP_COMBINER_1", new Combiner(
                Groups.MAIN_GROUP,
                COMBINER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                0.2));

        blocks.put("QP_COMBINER_2", new Combiner(
                Groups.MAIN_GROUP,
                COMBINER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                100,
                0.14));

        blocks.put("QP_COMBINER_3", new Combiner(
                Groups.MAIN_GROUP,
                COMBINER_3,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                0.08));

        blocks.put("QP_COMBINER_4", new Combiner(
                Groups.MAIN_GROUP,
                COMBINER_4,
                RecipeType.NULL,
                new ItemStack[]{},
                10000,
                0.05));

        blocks.put("QP_SPLITTER_1", new Combiner(
                Groups.MAIN_GROUP,
                SPLITTER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                0.2));

        blocks.put("QP_SPLITTER_2", new Combiner(
                Groups.MAIN_GROUP,
                SPLITTER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                100,
                0.14));

        blocks.put("QP_SPLITTER_3", new Combiner(
                Groups.MAIN_GROUP,
                SPLITTER_3,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                0.08));

        blocks.put("QP_SPLITTER_4", new Combiner(
                Groups.MAIN_GROUP,
                SPLITTER_4,
                RecipeType.NULL,
                new ItemStack[]{},
                10000,
                0.05));

        blocks.put("QP_TURRET", new Turret(
                Groups.MAIN_GROUP,
                TURRET,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                30,
                10,
                2));
    }}

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        targetingWand.register(addon);

        blocks.values().forEach(block -> block.register(addon));
    }
}
