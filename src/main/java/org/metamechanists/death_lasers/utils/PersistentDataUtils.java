package org.metamechanists.death_lasers.utils;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PersistentDataUtils {
    public static void clear(ItemStack stack, NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.remove(meta, key);
        stack.setItemMeta(meta);
    }

    public static void setString(ItemStack stack, NamespacedKey key, String value) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setString(meta, key, value);
        stack.setItemMeta(meta);
    }
    public static String getString(ItemStack stack, NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        return PersistentDataAPI.getString(meta, key);
    }
}
