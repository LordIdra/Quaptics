package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.RecipeTypes;


@UtilityClass
public class CraftingComponents {
    public final SlimefunItemStack PHASE_CRYSTAL_1 = new SlimefunItemStack("QP_PHASE_CRYSTAL_1", getPhaseCrystal(), "&7Phase Crystal", Lore.phaseChange(1));
    public final SlimefunItemStack PHASE_CRYSTAL_5 = new SlimefunItemStack("QP_PHASE_CRYSTAL_5", getPhaseCrystal(), "&7Phase Crystal", Lore.phaseChange(5));
    public final SlimefunItemStack PHASE_CRYSTAL_15 = new SlimefunItemStack("QP_PHASE_CRYSTAL_15", getPhaseCrystal(), "&7Phase Crystal", Lore.phaseChange(15));
    public final SlimefunItemStack PHASE_CRYSTAL_45 = new SlimefunItemStack("QP_PHASE_CRYSTAL_45", getPhaseCrystal(), "&7Phase Crystal", Lore.phaseChange(45));
    public final SlimefunItemStack PHASE_CRYSTAL_90 = new SlimefunItemStack("QP_PHASE_CRYSTAL_90", getPhaseCrystal(), "&7Phase Crystal", Lore.phaseChange(90));
    public final SlimefunItemStack PHASE_CRYSTAL_180 = new SlimefunItemStack("QP_PHASE_CRYSTAL_180", getPhaseCrystal(), "&7Phase Crystal", Lore.phaseChange(180));

    private @NotNull ItemStack getPhaseCrystal() {
        final ItemStack itemStack = new ItemStack(Material.QUARTZ);
        itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return itemStack;
    }

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SlimefunItem(
                Groups.CRAFTING_COMPONENTS,
                PHASE_CRYSTAL_1,
                RecipeTypes.RECIPE_REFINING,
                new ItemStack[]{
                        null, null, null,
                        null, new ItemStack(Material.QUARTZ), null,
                        null, null, null
                }).register(addon);

        new SlimefunItem(
                Groups.CRAFTING_COMPONENTS,
                PHASE_CRYSTAL_5,
                RecipeTypes.RECIPE_INFUSION,
                new ItemStack[]{
                        null, null, null,
                        null, PHASE_CRYSTAL_1, null,
                        null, null, null
                }).register(addon);

        new SlimefunItem(
                Groups.CRAFTING_COMPONENTS,
                PHASE_CRYSTAL_15,
                RecipeTypes.RECIPE_INFUSION,
                new ItemStack[]{
                        null, null, null,
                        null, PHASE_CRYSTAL_5, null,
                        null, null, null
                }).register(addon);

        new SlimefunItem(
                Groups.CRAFTING_COMPONENTS,
                PHASE_CRYSTAL_45,
                RecipeTypes.RECIPE_INFUSION,
                new ItemStack[]{
                        null, null, null,
                        null, PHASE_CRYSTAL_15, null,
                        null, null, null
                }).register(addon);

        new SlimefunItem(
                Groups.CRAFTING_COMPONENTS,
                PHASE_CRYSTAL_90,
                RecipeTypes.RECIPE_INFUSION,
                new ItemStack[]{
                        null, null, null,
                        null, PHASE_CRYSTAL_45, null,
                        null, null, null
                }).register(addon);
    }
}
