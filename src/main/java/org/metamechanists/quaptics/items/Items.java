package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.concentrators.EnergyConcentrator;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Turret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.metamechanists.quaptics.items.ItemStacks.*;

public class Items {
    @Getter
    private static final Map<String, ConnectedBlock> blocks = new LinkedHashMap<>();

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new TargetingWand(
                Groups.TOOLS,
                TARGETING_WAND,
                RecipeType.NULL,
                new ItemStack[] {}).register(addon);

        new SolarConcentrator(
                Groups.PRIMITIVE,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                1,
                Tiers.PRIMITIVE.maxPower).register(addon);

        new SolarConcentrator(
                Groups.BASIC,
                SOLAR_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                10,
                Tiers.BASIC.maxPower).register(addon);

        new EnergyConcentrator(
                Groups.BASIC,
                ENERGY_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                30,
                30,
                15,
                Tiers.BASIC.maxPower).register(addon);

        new EnergyConcentrator(
                Groups.INTERMEDIATE,
                ENERGY_CONCENTRATOR_2,
                RecipeType.NULL,
                new ItemStack[]{},
                160,
                160,
                200,
                Tiers.INTERMEDIATE.maxPower).register(addon);

        new EnergyConcentrator(
                Groups.ADVANCED,
                ENERGY_CONCENTRATOR_3,
                RecipeType.NULL,
                new ItemStack[]{},
                680,
                680,
                2500,
                Tiers.ADVANCED.maxPower).register(addon);

        new Lens(
                Groups.PRIMITIVE,
                LENS_1,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.PRIMITIVE.maxPower,
                0.1).register(addon);

        new Lens(
                Groups.BASIC,
                LENS_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.BASIC.maxPower,
                0.07).register(addon);

        new Lens(
                Groups.INTERMEDIATE,
                LENS_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.INTERMEDIATE.maxPower,
                0.04).register(addon);

        new Lens(
                Groups.ADVANCED,
                LENS_4,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.ADVANCED.maxPower,
                0.02).register(addon);

        new Combiner(
                Groups.PRIMITIVE,
                COMBINER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.PRIMITIVE.maxPower,
                0.2).register(addon);

        new Combiner(
                Groups.BASIC,
                COMBINER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.BASIC.maxPower,
                0.14).register(addon);

        new Combiner(
                Groups.INTERMEDIATE,
                COMBINER_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.INTERMEDIATE.maxPower,
                0.08).register(addon);

        new Combiner(
                Groups.ADVANCED,
                COMBINER_4,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.ADVANCED.maxPower,
                0.05).register(addon);

        new Splitter(
                Groups.PRIMITIVE,
                SPLITTER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.PRIMITIVE.maxPower,
                0.2).register(addon);

        new Splitter(
                Groups.BASIC,
                SPLITTER_2,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.BASIC.maxPower,
                0.14).register(addon);

        new Splitter(
                Groups.INTERMEDIATE,
                SPLITTER_3,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.INTERMEDIATE.maxPower,
                0.08).register(addon);

        new Splitter(
                Groups.ADVANCED,
                SPLITTER_4,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.ADVANCED.maxPower,
                0.05).register(addon);

        new Turret(
                Groups.PRIMITIVE,
                TURRET,
                RecipeType.NULL,
                new ItemStack[]{},
                Tiers.PRIMITIVE.maxPower,
                5,
                10,
                2).register(addon);

        Slimefun.getRegistry().getAllSlimefunItems().stream()
                .filter(slimefunItem -> slimefunItem instanceof ConnectedBlock)
                .map(slimefunItem -> (ConnectedBlock) slimefunItem)
                .forEach(connectedBlock -> blocks.put(connectedBlock.getId(), connectedBlock));
    }
}
