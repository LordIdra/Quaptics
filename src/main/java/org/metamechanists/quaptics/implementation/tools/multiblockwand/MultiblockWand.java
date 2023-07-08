package org.metamechanists.quaptics.implementation.tools.multiblockwand;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MultiblockWand extends SlimefunItem {
    public MultiblockWand(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    public static void removeProjection(final ItemStack itemStack) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(itemStack);
        final List<UUID> uuids = traverser.getUuidList("uuids");
        if (uuids == null) {
            return;
        }

        uuids.stream()
                .map(Bukkit::getEntity)
                .filter(Objects::nonNull)
                .forEach(Entity::remove);
    }
}
