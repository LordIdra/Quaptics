package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Combiner;
import org.metamechanists.quaptics.implementation.blocks.Emitter;
import org.metamechanists.quaptics.implementation.blocks.Lens;
import org.metamechanists.quaptics.implementation.blocks.Turret;
import org.metamechanists.quaptics.implementation.blocks.Splitter;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;

import java.util.HashMap;
import java.util.Map;

import static org.metamechanists.quaptics.items.ItemStacks.*;

public class Items {
    public static final TargetingWand targetingWand = new TargetingWand(
            Groups.MAIN_GROUP,
            TARGETING_WAND,
            RecipeType.NULL,
            new ItemStack[] {});

    @Getter
    private static final Map<String, ConnectedBlock> blocks = new HashMap<>();

    static {{
        blocks.put("EMITTER", new Emitter(
                Groups.MAIN_GROUP,
                EMITTER,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                100,
                20));

        blocks.put("LENS", new Lens(
                Groups.MAIN_GROUP,
                LENS,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                0.1));

        blocks.put("COMBINER", new Combiner(
                Groups.MAIN_GROUP,
                COMBINER,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                0.2));

        blocks.put("SPLITTER", new Splitter(
                Groups.MAIN_GROUP,
                SPLITTER,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                0.2));

        blocks.put("TURRET", new Turret(
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
