package org.metamechanists.death_lasers.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.implementation.base.ConnectedBlock;
import org.metamechanists.death_lasers.implementation.blocks.DarkPrism;
import org.metamechanists.death_lasers.implementation.blocks.Emitter;
import org.metamechanists.death_lasers.implementation.blocks.Lens;
import org.metamechanists.death_lasers.implementation.blocks.WhitePrism;
import org.metamechanists.death_lasers.implementation.tools.TargetingWand;

import java.util.HashMap;
import java.util.Map;

import static org.metamechanists.death_lasers.items.ItemStacks.DARK_PRISM;
import static org.metamechanists.death_lasers.items.ItemStacks.EMITTER;
import static org.metamechanists.death_lasers.items.ItemStacks.LENS;
import static org.metamechanists.death_lasers.items.ItemStacks.TARGETING_WAND;
import static org.metamechanists.death_lasers.items.ItemStacks.WHITE_PRISM;

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
    }}

    public static void initialize() {
        final SlimefunAddon addon = DEATH_LASERS.getInstance();

        targetingWand.register(addon);

        blocks.values().forEach(block -> block.register(addon));
    }
}
