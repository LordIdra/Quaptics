package org.metamechanists.death_lasers.implementation;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;

import java.util.HashMap;
import java.util.Map;

public class BlockTemplate extends ConnectedBlock {
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;

    public BlockTemplate(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }


    @Override
    public void onInputLinkUpdated(ConnectionGroup group) {
        // TODO logic when an input is updated
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);

        // TODO create block displays to represent this component

        return displayGroup;
    }

    @Override
    protected Map<String, ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final Map<String, ConnectionPoint> points = new HashMap<>();

        // TODO create connection points for this component

        return points;
    }

    @Override
    protected Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to) {
        // TODO connection point position update logic
        return null;
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        // TODO set base material
        return Material.STRUCTURE_VOID;
    }
}
