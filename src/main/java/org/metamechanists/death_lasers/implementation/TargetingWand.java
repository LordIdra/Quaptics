package org.metamechanists.death_lasers.implementation;

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
import org.metamechanists.death_lasers.Items;
import org.metamechanists.death_lasers.Keys;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.PersistentDataUtils;

import javax.annotation.Nonnull;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
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

            if (!isItem(stack)
                    || !canUse(player, false)
                    || block == null
                    || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)
                    || event.getInteractEvent().getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            if (player.isSneaking()) {
                if (BlockStorage.hasBlockInfo(block) && BlockStorage.check(block) == Items.DEATH_LASER.getItem()) {
                    PersistentDataUtils.setLocation(stack, Keys.SOURCE, block.getLocation());
                } else {
                    player.sendMessage(Language.getLanguageEntry("targeting-wand.not-source"));
                }
                return;
            }

            final Location source = PersistentDataUtils.getLocation(stack, Keys.SOURCE);
            final Location target = block.getLocation();

            if (source.getWorld().getUID() == target.getWorld().getUID()) {
                player.sendMessage(Language.getLanguageEntry("targeting-wand.different-worlds"));
            }

            BlockStorage.addBlockInfo(source, Keys.LOCATION_X.getKey(), String.valueOf(target.getBlockX()));
            BlockStorage.addBlockInfo(source, Keys.LOCATION_Y.getKey(), String.valueOf(target.getBlockY()));
            BlockStorage.addBlockInfo(source, Keys.LOCATION_Z.getKey(), String.valueOf(target.getBlockZ()));
        };
    }
}
