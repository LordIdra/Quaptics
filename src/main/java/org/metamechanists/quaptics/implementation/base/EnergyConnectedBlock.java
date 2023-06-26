package org.metamechanists.quaptics.implementation.base;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class EnergyConnectedBlock extends ConnectedBlock implements EnergyNetComponent {
    @Getter
    private final EnergyNetComponentType energyComponentType = EnergyNetComponentType.CONSUMER;
    @Getter
    private final int capacity;
    @Getter
    private final int consumption;
    protected boolean powered = false;

    public EnergyConnectedBlock(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double maxPower, int capacity, int consumption) {
        super(group, item, recipeType, recipe, maxPower);
        this.capacity = capacity;
        this.consumption = consumption;
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onSlimefunTick(@NotNull Block block, SlimefunItem item, Config data) {
        if (getCharge(block.getLocation(), data) >= consumption) {
            removeCharge(block.getLocation(), consumption);
            powered = true;
            return;
        }
        powered = false;
    }
}
