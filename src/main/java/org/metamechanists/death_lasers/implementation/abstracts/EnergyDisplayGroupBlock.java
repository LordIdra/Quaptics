package org.metamechanists.death_lasers.implementation.abstracts;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
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
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

import static dev.sefiraat.sefilib.slimefun.blocks.DisplayGroupBlock.KEY_UUID;

public abstract class EnergyDisplayGroupBlock extends SimpleSlimefunItem<BlockTicker> implements EnergyNetComponent {
    @Getter
    protected final int capacity;
    protected final int consumption;

    public EnergyDisplayGroupBlock(
            ItemGroup group, SlimefunItemStack item,
            RecipeType recipeType, ItemStack[] recipe,
            int capacity, int consumption) {
        super(group, item, recipeType, recipe);
        this.consumption = consumption;
        this.capacity = capacity;
    }

    protected abstract DisplayGroup generateDisplayGroup(Location location);
    @NotNull
    protected abstract Material getBaseMaterial();
    protected abstract void onPlace(BlockPlaceEvent event);
    protected abstract void onBreak(BlockBreakEvent event);

    protected void onSlimefunTick(Block block, SlimefunItem item, Config data) {}

    @Override
    public @NotNull BlockTicker getItemHandler() {
        return new BlockTicker() {
            @Override
            public void tick(Block block, SlimefunItem item, Config data) {
                onSlimefunTick(block, item, data);
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void preRegister() {
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                        final Location location = event.getBlock().getLocation();
                        final DisplayGroup displayGroup = generateDisplayGroup(location.clone());
                        setUUID(displayGroup, location);
                        event.getBlock().setType(getBaseMaterial());
                        onPlace(event);
                    }
                },

                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent event, ItemStack item, List<ItemStack> drops) {
                        final Location location = event.getBlock().getLocation();
                        final DisplayGroup displayGroup = getDisplayGroup(location.clone());
                        if (displayGroup == null) {
                            return;
                        }
                        displayGroup.remove();
                        event.getBlock().setType(Material.AIR);
                        onBreak(event);
                    }
                }
        );
    }

    private void setUUID(@Nonnull DisplayGroup displayGroup, @Nonnull Location location) {
        BlockStorage.addBlockInfo(location, KEY_UUID, displayGroup.getParentUUID().toString());
    }

    @OverridingMethodsMustInvokeSuper
    public UUID getUUID(@Nonnull Location location) {
        final String uuid = BlockStorage.getLocationInfo(location, KEY_UUID);
        return UUID.fromString(uuid);
    }

    @OverridingMethodsMustInvokeSuper
    public DisplayGroup getDisplayGroup(@Nonnull Location location) {
        final UUID uuid = getUUID(location);
        return DisplayGroup.fromUUID(uuid);
    }
}
