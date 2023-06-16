package org.metamechanists.death_lasers.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class DisplayGroupTickerBlock extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {
    @Getter
    protected final int capacity;
    protected final int consumption;

    public DisplayGroupTickerBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe);
        this.consumption = consumption;
        this.capacity = capacity;
        addItemHandler(onBreak(), onPlace());
    }

    protected abstract ConnectionGroup createConnectionGroup(Location location);

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent e, @NotNull ItemStack item, @NotNull List<ItemStack> drops) {
                final Location sourceGroupLocation = e.getBlock().getLocation();
                ConnectionPointStorage.removeConnectionPointGroup(sourceGroupLocation);
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent event) {
                Location location = event.getBlock().getLocation();
                ConnectionPointStorage.addConnectionPointGroup(location, createConnectionGroup(location));
            }
        };
    }

    @NotNull
    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public void tick(Block block, SlimefunItem item, Config data) {
                int charge = getCharge(block.getLocation(), data);
                if (charge >= consumption) {
                    removeCharge(block.getLocation(), consumption);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }
}
