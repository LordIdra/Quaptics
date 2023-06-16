package org.metamechanists.death_lasers.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionGroupBuilder;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;

public class LaserEmitter extends DisplayGroupTickerBlock {
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;
    private static final Vector INPUT_VECTOR = new Vector(0.5F, 1.0F, 0.1F);
    private static final Vector OUTPUT_VECTOR = new Vector(0.5F, 1.0F, 0.9F);

    public LaserEmitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }

    @Override
    protected ConnectionGroup createConnectionGroup(Location location) {
        final Location inputPointLocation = location.clone().add(INPUT_VECTOR);
        final Location outputPointLocation = location.clone().add(OUTPUT_VECTOR);
        return new ConnectionGroupBuilder()
                .addConnectionPoint("input", new ConnectionPointInput(inputPointLocation))
                .addConnectionPoint("output", new ConnectionPointOutput(outputPointLocation))
                .build();
    }

    @Override
    protected void doTick() {}
}
