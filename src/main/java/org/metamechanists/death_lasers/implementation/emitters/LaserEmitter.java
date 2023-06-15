package org.metamechanists.death_lasers.implementation.emitters;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.connections.ConnectionGroupBuilder;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class LaserEmitter extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {
    private static final Vector INPUT_VECTOR = new Vector(0.5F, 1.0F, 0.1F);
    private static final Vector OUTPUT_VECTOR = new Vector(0.5F, 1.0F, 0.9F);
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;
    @Getter
    private final int capacity;
    private final int consumption;

    public LaserEmitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe);
        this.consumption = consumption;
        this.capacity = capacity;
        addItemHandler(onBreak(), onPlace());
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {
            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent event) {
                final Location sourceGroupLocation = event.getBlock().getLocation();
                final Location inputPointLocation = sourceGroupLocation.clone().add(INPUT_VECTOR);
                final Location outputPointLocation = sourceGroupLocation.clone().add(OUTPUT_VECTOR);
                ConnectionPointStorage.addConnectionPointGroup(sourceGroupLocation,
                        new ConnectionGroupBuilder()
                                .addConnectionPoint("input", new ConnectionPointInput(inputPointLocation))
                                .addConnectionPoint("output", new ConnectionPointOutput(outputPointLocation))
                                .build());
            }
        };
    }

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
