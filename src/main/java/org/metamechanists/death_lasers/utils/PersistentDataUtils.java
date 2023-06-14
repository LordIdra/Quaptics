package org.metamechanists.death_lasers.utils;

import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PersistentDataUtils {
    public static void setString(ItemStack stack, NamespacedKey key, String value) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setString(meta, key, value);
        stack.setItemMeta(meta);
    }
    public static String getString(ItemStack stack, NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        return PersistentDataAPI.getString(meta, key);
    }

    public static void setInt(ItemStack stack, NamespacedKey key, int value) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.setInt(meta, key, value);
        stack.setItemMeta(meta);
    }
    public static int getInt(ItemStack stack, NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        return PersistentDataAPI.getInt(meta, key);
    }

    public static void setLocation(ItemStack stack, NamespacedKey key, Location value) {
        final ItemMeta meta = stack.getItemMeta();
        PersistentDataAPI.set(meta, key, new PersistentDataLocation(), value);
        stack.setItemMeta(meta);
    }
    public static Location getLocation(ItemStack stack, NamespacedKey key) {
        final ItemMeta meta = stack.getItemMeta();
        return PersistentDataAPI.get(meta, key, new PersistentDataLocation());
    }
}
