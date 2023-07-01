package org.metamechanists.quaptics.implementation.base;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.Keys;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class EnergyConnectedBlock extends ConnectedBlock implements EnergyNetComponent {
    private static final EnergyNetComponentType COMPONENT_TYPE = EnergyNetComponentType.CONSUMER;
    @Getter
    private final int capacity;
    @Getter
    private final int consumption;

    protected EnergyConnectedBlock(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                                   final Settings settings, final int capacity, final int consumption) {
        super(group, item, recipeType, recipe, settings);
        this.capacity = capacity;
        this.consumption = consumption;
    }

    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return COMPONENT_TYPE;
    }

    private static void setPowered(final Location location, final boolean powered) {
        BlockStorage.addBlockInfo(location, Keys.BS_POWERED, powered ? "true" : "false");
    }

    protected static boolean isPowered(final Location location) {
        final String powered = BlockStorage.getLocationInfo(location, Keys.BS_POWERED);
        return "true".equals(powered);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        if (getCharge(block.getLocation(), data) >= consumption) {
            removeCharge(block.getLocation(), consumption);
            setPowered(block.getLocation(), true);
            return;
        }
        setPowered(block.getLocation(), false);
    }
}
