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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.Keys;
import org.metamechanists.death_lasers.utils.PersistentDataUtils;

import javax.annotation.Nonnull;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private TargetingMode getTargetingMode(ItemStack stack) {
        if (PersistentDataAPI.hasString(stack.getItemMeta(), Keys.TARGETING_MODE)) {
            DEATH_LASERS.getInstance().getLogger().severe("i");
            return TargetingMode.valueOf(PersistentDataUtils.getString(stack, Keys.TARGETING_MODE));
        }
        return TargetingMode.SOURCE_NOT_SELECTED;
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

            DEATH_LASERS.getInstance().getLogger().severe("C");

            if (isItem(stack)
                    && canUse(player, false)
                    && block != null
                    && Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)
                    && event.getInteractEvent().getAction() == Action.RIGHT_CLICK_BLOCK) {

                if (getTargetingMode(stack) == TargetingMode.SOURCE_NOT_SELECTED) {
                    DEATH_LASERS.getInstance().getLogger().severe("A");
                    PersistentDataUtils.setLocation(stack, Keys.LOCATION, block.getLocation());
                    PersistentDataUtils.setString(stack, Keys.TARGETING_MODE,TargetingMode.SOURCE_SELECTED.name());

                } else {
                    DEATH_LASERS.getInstance().getLogger().severe("B");
                    final Location source = PersistentDataUtils.getLocation(stack, Keys.LOCATION);
                    final Location target = block.getLocation();

                    // TODO range/world check

                    BlockStorage.addBlockInfo(source, Keys.LOCATION_X.getKey(), String.valueOf(target.getBlockX()));
                    BlockStorage.addBlockInfo(source, Keys.LOCATION_Y.getKey(), String.valueOf(target.getBlockY()));
                    BlockStorage.addBlockInfo(source, Keys.LOCATION_Z.getKey(), String.valueOf(target.getBlockZ()));

                    PersistentDataUtils.setString(stack, Keys.TARGETING_MODE, TargetingMode.SOURCE_NOT_SELECTED.name());

                    DEATH_LASERS.getInstance().getLogger().severe("1");
                }
            }
        };
    }
}
