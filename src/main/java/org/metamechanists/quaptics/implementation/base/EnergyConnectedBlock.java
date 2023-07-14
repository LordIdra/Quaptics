package org.metamechanists.quaptics.implementation.base;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class EnergyConnectedBlock extends ConnectedBlock implements EnergyNetComponent {
    private static final EnergyNetComponentType COMPONENT_TYPE = EnergyNetComponentType.CONSUMER;

    protected EnergyConnectedBlock(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @NotNull
    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return COMPONENT_TYPE;
    }

    protected static boolean hasEnoughEnergy(final Location location) {
        return BlockStorageAPI.getBoolean(location, Keys.BS_ENOUGH_ENERGY);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        super.onSlimefunTick(block, item, data);
        final boolean powered = getCharge(block.getLocation(), data) >= settings.getEnergyConsumption();
        if (powered) {
            removeCharge(block.getLocation(), settings.getEnergyConsumption());
        }
        BlockStorageAPI.set(block.getLocation(), Keys.BS_ENOUGH_ENERGY, powered);
    }

    @Override
    public int getCapacity() {
        return settings.getEnergyCapacity();
    }
}
