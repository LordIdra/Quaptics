package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.DarkPrism;
import org.metamechanists.quaptics.implementation.blocks.Emitter;
import org.metamechanists.quaptics.implementation.blocks.Lens;
import org.metamechanists.quaptics.implementation.blocks.Turret;
import org.metamechanists.quaptics.implementation.blocks.WhitePrism;
import org.metamechanists.quaptics.implementation.tools.TargetingWand;

import java.util.HashMap;
import java.util.Map;

import static org.metamechanists.quaptics.items.ItemStacks.DARK_PRISM;
import static org.metamechanists.quaptics.items.ItemStacks.EMITTER;
import static org.metamechanists.quaptics.items.ItemStacks.LENS;
import static org.metamechanists.quaptics.items.ItemStacks.TARGETING_WAND;
import static org.metamechanists.quaptics.items.ItemStacks.TURRET;
import static org.metamechanists.quaptics.items.ItemStacks.WHITE_PRISM;

public class Items {
    public static final TargetingWand targetingWand = new TargetingWand(
            Groups.DEATH_LASER_GROUP,
            TARGETING_WAND,
            RecipeType.NULL,
            new ItemStack[] {});

    @Getter
    private static final Map<String, ConnectedBlock> blocks = new HashMap<>();

    static {{
        blocks.put("EMITTER", new Emitter(
                Groups.DEATH_LASER_GROUP,
                EMITTER,
                RecipeType.NULL,
                new ItemStack[]{},
                1000,
                100,
                20));

        blocks.put("LENS", new Lens(
                Groups.DEATH_LASER_GROUP,
                LENS,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                0.1));

        blocks.put("DARK_PRISM", new DarkPrism(
                Groups.DEATH_LASER_GROUP,
                DARK_PRISM,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                0.2));

        blocks.put("WHITE_PRISM", new WhitePrism(
                Groups.DEATH_LASER_GROUP,
                WHITE_PRISM,
                RecipeType.NULL,
                new ItemStack[]{},
                40,
                0.2));

        blocks.put("TURRET", new Turret(
                Groups.DEATH_LASER_GROUP,
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
