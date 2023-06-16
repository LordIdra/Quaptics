package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.entity.display.builders.ItemDisplayBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.utils.ScaryMathsUtils;

import java.util.HashMap;
import java.util.Map;

public class LaserEmitter extends ConnectedBlock {
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;
    private static final Vector INPUT_VECTOR = new Vector(0.5F, 1.0F, 0.1F);
    private static final Vector OUTPUT_VECTOR = new Vector(0.5F, 1.0F, 0.9F);

    public LaserEmitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }

    @Override
    protected Map<String, ConnectionPoint> generateConnectionPoints(Location location) {
        final Map<String, ConnectionPoint> points = new HashMap<>();
        points.put("input", new ConnectionPointInput(location.clone().add(INPUT_VECTOR)));
        points.put("output", new ConnectionPointOutput(location.clone().add(OUTPUT_VECTOR)));
        return points;
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Location location) {
        DisplayGroup displayGroup = new DisplayGroup(location);
        displayGroup.addDisplay(
                "main",
                new ItemDisplayBuilder()
                        .setGroupParentOffset(new Vector(0.5, 0.5, 0.5))
                        .setItemStack(new ItemStack(Material.GLASS))
                        .setTransformation(ScaryMathsUtils.createDisplayTransformationType2(
                                location, 0.5F))
                        .build(displayGroup)
        );
        return displayGroup;
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.BARRIER;
    }
}
