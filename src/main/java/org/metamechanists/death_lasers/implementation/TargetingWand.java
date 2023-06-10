package org.metamechanists.death_lasers.implementation;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.Keys;

import javax.annotation.Nonnull;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private void setTargetingMode(ItemStack stack, TargetingMode mode) {
        PersistentDataAPI.setString(stack.getItemMeta(), Keys.TARGETING_MODE, mode.name());
    }

    private TargetingMode getTargetingMode(ItemStack stack) {
        if (PersistentDataAPI.hasString(stack.getItemMeta(), Keys.TARGETING_MODE)) {
            return TargetingMode.valueOf(PersistentDataAPI.getString(stack.getItemMeta(), Keys.LOCATION_WORLD));
        }
        return TargetingMode.SOURCE_NOT_SELECTED;
    }


    private void setSourceData(ItemStack stack, Location location) {
        PersistentDataAPI.setUUID(stack.getItemMeta(), Keys.LOCATION_WORLD, location.getWorld().getUID());
        PersistentDataAPI.setInt(stack.getItemMeta(), Keys.LOCATION_X, location.getBlockX());
        PersistentDataAPI.setInt(stack.getItemMeta(), Keys.LOCATION_Y, location.getBlockY());
        PersistentDataAPI.setInt(stack.getItemMeta(), Keys.LOCATION_Z, location.getBlockZ());
    }

    private Location getSourceData(ItemStack stack) {
        return new Location(
                DEATH_LASERS.getInstance().getServer().getWorld(PersistentDataAPI.getString(stack.getItemMeta(), Keys.LOCATION_WORLD)),
                PersistentDataAPI.getInt(stack.getItemMeta(), Keys.LOCATION_X),
                PersistentDataAPI.getInt(stack.getItemMeta(), Keys.LOCATION_Y),
                PersistentDataAPI.getInt(stack.getItemMeta(), Keys.LOCATION_Z));
    }

    @Override
    public void preRegister() {
        addItemHandler(onBlockInteract());
    }

    @Nonnull
    private ItemUseHandler onBlockInteract() {
        return event -> {
            final Player player = event.getPlayer();
            final Block block = event.getClickedBlock().orElse(null);
            final ItemStack stack = event.getItem();

            if (isItem(stack)
                    && canUse(player, false)
                    && block != null
                    && Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)
                    && player.isSneaking()
                    && event.getInteractEvent().getAction() == Action.RIGHT_CLICK_BLOCK
                    && block.getType() == Material.SCULK_SHRIEKER) {
                if (getTargetingMode(stack) == TargetingMode.SOURCE_NOT_SELECTED) {
                    setSourceData(stack, block.getLocation());
                    setTargetingMode(stack, TargetingMode.SOURCE_SELECTED);
                } else {
                    final Location source = getSourceData(stack);
                    final Location target = block.getLocation();

                    // TODO range/world check

                    BlockStorage.addBlockInfo(source, Keys.LOCATION_X.getKey(), String.valueOf(target.getBlockX()));
                    BlockStorage.addBlockInfo(source, Keys.LOCATION_Y.getKey(), String.valueOf(target.getBlockY()));
                    BlockStorage.addBlockInfo(source, Keys.LOCATION_Z.getKey(), String.valueOf(target.getBlockZ()));

                    setTargetingMode(stack, TargetingMode.SOURCE_NOT_SELECTED);

                    DEATH_LASERS.getInstance().getLogger().severe("1");
                }
            }
        };
    }
}
