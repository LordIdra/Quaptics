package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand;
import org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun;
import org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun;
import org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand;
import org.metamechanists.quaptics.items.Groups;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.BILLON_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.HARDENED_METAL_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.HOLOGRAM_PROJECTOR;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.POWER_CRYSTAL;
import static org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWand.MULTIBLOCK_WAND;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_3;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_4;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_1;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_2;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWand.TARGETING_WAND;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.DIELECTRIC_1;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.DIELECTRIC_2;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.DIELECTRIC_4;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.ENERGY_CONCENTRATION_ELEMENT_1;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.ENERGY_CONCENTRATION_ELEMENT_2;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.ENERGY_CONCENTRATION_ELEMENT_3;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSCEIVER_1;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSCEIVER_2;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSCEIVER_3;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSCEIVER_4;


@UtilityClass
public class Tools {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new TargetingWand(Groups.TOOLS, TARGETING_WAND, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, new ItemStack(Material.LAPIS_BLOCK),
                null, new ItemStack(Material.STICK), null,
                new ItemStack(Material.STICK), null, null
        }).register(addon);

        new MultiblockWand(Groups.TOOLS, MULTIBLOCK_WAND, RecipeType.NULL, new ItemStack[]{
                null, null, HOLOGRAM_PROJECTOR,
                null, new ItemStack(Material.STICK), null,
                new ItemStack(Material.STICK), null, null
        }).register(addon);



        new ModulatedRayGun(Groups.TOOLS, RAY_GUN_1, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                ENERGY_CONCENTRATION_ELEMENT_1, new ItemStack(Material.IRON_INGOT), TRANSCEIVER_1,
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), null
        }, RAY_GUN_1_SETTINGS).register(addon);

        new ModulatedRayGun(Groups.TOOLS, RAY_GUN_2, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                ENERGY_CONCENTRATION_ELEMENT_2, RAY_GUN_1, TRANSCEIVER_2,
                DIELECTRIC_1, BILLON_INGOT, null
        }, RAY_GUN_2_SETTINGS).register(addon);

        new DirectRayGun(Groups.TOOLS, RAY_GUN_3, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                ENERGY_CONCENTRATION_ELEMENT_3, RAY_GUN_2, TRANSCEIVER_3,
                DIELECTRIC_2, HARDENED_METAL_INGOT, null
        }, RAY_GUN_3_SETTINGS).register(addon);

        new DirectRayGun(Groups.TOOLS, RAY_GUN_4, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                ENERGY_CONCENTRATION_ELEMENT_3, RAY_GUN_3, TRANSCEIVER_4,
                DIELECTRIC_4, POWER_CRYSTAL, null
        }, RAY_GUN_4_SETTINGS).register(addon);
    }
}
