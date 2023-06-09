package org.metamechanists.death_lasers.implementation;

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
import org.metamechanists.death_lasers.lasers.Lasers;
import org.metamechanists.death_lasers.lasers.beam.Beam;
import org.metamechanists.death_lasers.lasers.beam.BlockDisplayBeam;
import org.metamechanists.death_lasers.lasers.storage.BeamStorage;
import org.metamechanists.death_lasers.lasers.ticker.factory.LinearTimeTickerFactory;

import javax.annotation.Nonnull;
import java.util.List;

public class DeathLaser extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;
    @Getter
    private final int capacity;
    private final int consumption;

    public DeathLaser(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe);
        this.consumption = consumption;
        this.capacity = capacity;
        addItemHandler(onPlace(), onBreak());
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@NotNull BlockPlaceEvent e) {
                final Location location = e.getBlock().getLocation();
                final Beam linearRedBeam = new BlockDisplayBeam(
                        new LinearTimeTickerFactory(
                                Lasers.testDisplay(location),
                                location,
                                location.add(new Vector(5, 0, 0)),
                                20),
                        Lasers.testTimer,
                        true);
                BeamStorage.add(location, linearRedBeam);
            }
        };
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new BlockBreakHandler(false, false) {

            @Override
            public void onPlayerBreak(@NotNull BlockBreakEvent e, @NotNull ItemStack item, @NotNull List<ItemStack> drops) {
                BeamStorage.scheduleLocationRemoval(e.getBlock().getLocation());
            }
        };
    }

    @NotNull
    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                int charge = getCharge(b.getLocation(), data);
                if (charge >= consumption) {
                    removeCharge(b.getLocation(), consumption);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }
}
