package org.metamechanists.aircraft.utils;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class PersistentDataUtils {
    public void clear(@NotNull final ItemStack stack, @NotNull final NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.remove(meta, key);
        stack.setItemMeta(meta);
    }
    public void setString(@NotNull final ItemStack stack, @NotNull final NamespacedKey key, @NotNull final String value) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setString(meta, key, value);
        stack.setItemMeta(meta);
    }
    public String getString(@NotNull final ItemStack stack, @NotNull final NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        return PersistentDataAPI.getString(meta, key);
    }
}
