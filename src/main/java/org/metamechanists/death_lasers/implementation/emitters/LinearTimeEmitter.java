package org.metamechanists.death_lasers.implementation.emitters;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;


public class LinearTimeEmitter extends LaserEmitter {
    public LinearTimeEmitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }

    @Override
    void doTick(Block block, SlimefunItem item) {
        final ConnectionGroup group = ConnectionPointStorage.getGroupFromGroupLocation(block.getLocation());
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");
        output.tick();
    }
}
