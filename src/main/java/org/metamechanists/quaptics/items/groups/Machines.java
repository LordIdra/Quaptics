package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper;
import org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector;
import org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker;
import org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret;
import org.metamechanists.quaptics.items.Groups;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.BIG_CAPACITOR;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.CARBONADO_EDGED_CAPACITOR;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.DURALUMIN_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.ELECTRIC_MOTOR;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.ELECTRO_MAGNET;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.FERROSILICON;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.HARDENED_METAL_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.HOLOGRAM_PROJECTOR;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.POWER_CRYSTAL;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.REDSTONE_ALLOY;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.REINFORCED_ALLOY_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.SMALL_CAPACITOR;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_2;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_3;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_4;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_2;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector.ITEM_PROJECTOR;
import static org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector.ITEM_PROJECTOR_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_2;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_3;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad.LAUNCHPAD;
import static org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad.LAUNCHPAD_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.*;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.*;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_3;
import static org.metamechanists.quaptics.implementation.tools.raygun.DirectRayGun.RAY_GUN_4;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_1;
import static org.metamechanists.quaptics.implementation.tools.raygun.ModulatedRayGun.RAY_GUN_2;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.*;


@UtilityClass
public class Machines {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new ModulatedTurret(Groups.MACHINES, TURRET_1_HOSTILE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.GUNPOWDER), null,
                RECEIVER_1, RAY_GUN_1, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_1_HOSTILE_SETTINGS).register(addon);

        new ModulatedTurret(Groups.MACHINES, TURRET_1_PASSIVE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.WHEAT), null,
                RECEIVER_1, RAY_GUN_1, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_1_PASSIVE_SETTINGS).register(addon);

        new ModulatedTurret(Groups.MACHINES, TURRET_2_HOSTILE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.GUNPOWDER), null,
                RECEIVER_2, RAY_GUN_2, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_2_HOSTILE_SETTINGS).register(addon);

        new ModulatedTurret(Groups.MACHINES, TURRET_2_PASSIVE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.WHEAT), null,
                RECEIVER_2, RAY_GUN_2, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_2_PASSIVE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_3_HOSTILE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.GUNPOWDER), null,
                RECEIVER_3, RAY_GUN_3, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_3_HOSTILE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_3_PASSIVE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.WHEAT), null,
                RECEIVER_3, RAY_GUN_3, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_3_PASSIVE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_4_HOSTILE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.GUNPOWDER), null,
                RECEIVER_4, RAY_GUN_4, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_4_HOSTILE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_4_PASSIVE, RecipeType.NULL, new ItemStack[]{
                null, new ItemStack(Material.WHEAT), null,
                RECEIVER_4, RAY_GUN_4, TURRET_BARREL,
                TURRET_BASE, TURRET_TARGETING_MECHANISM, TURRET_BASE
        }, TURRET_4_PASSIVE_SETTINGS).register(addon);



        new Charger(Groups.MACHINES, CHARGER_1, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.IRON_INGOT), TRANSCEIVER_1, new ItemStack(Material.IRON_INGOT),
                RECEIVER_1, null, DIELECTRIC_1,
                new ItemStack(Material.IRON_INGOT), TRANSCEIVER_1, new ItemStack(Material.IRON_INGOT)

        }, CHARGER_1_SETTINGS).register(addon);

        new Charger(Groups.MACHINES, CHARGER_2, RecipeType.NULL, new ItemStack[]{
                DURALUMIN_INGOT, TRANSCEIVER_2, DURALUMIN_INGOT,
                RECEIVER_2, null, SMALL_CAPACITOR,
                DURALUMIN_INGOT, TRANSCEIVER_2, DURALUMIN_INGOT
        }, CHARGER_2_SETTINGS).register(addon);

        new Charger(Groups.MACHINES, CHARGER_3, RecipeType.NULL, new ItemStack[]{
                REDSTONE_ALLOY, TRANSCEIVER_3, REDSTONE_ALLOY,
                RECEIVER_3, null, BIG_CAPACITOR,
                REDSTONE_ALLOY, TRANSCEIVER_3, REDSTONE_ALLOY
        }, CHARGER_3_SETTINGS).register(addon);

        new Charger(Groups.MACHINES, CHARGER_4, RecipeType.NULL, new ItemStack[]{
                REINFORCED_ALLOY_INGOT, TRANSCEIVER_4, REINFORCED_ALLOY_INGOT,
                RECEIVER_4, null, CARBONADO_EDGED_CAPACITOR,
                REINFORCED_ALLOY_INGOT, TRANSCEIVER_4, REINFORCED_ALLOY_INGOT
        }, CHARGER_4_SETTINGS).register(addon);



        new MultiblockClicker(Groups.MACHINES, MULTIBLOCK_CLICKER_1, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK),
                RECEIVER_2, ELECTRIC_MOTOR, new ItemStack(Material.DISPENSER),
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK)
        }, MULTIBLOCK_CLICKER_1_SETTINGS).register(addon);

        new MultiblockClicker(Groups.MACHINES, MULTIBLOCK_CLICKER_2, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK),
                RECEIVER_2, ELECTRIC_MOTOR, MULTIBLOCK_CLICKER_1,
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK)
        }, MULTIBLOCK_CLICKER_2_SETTINGS).register(addon);

        new MultiblockClicker(Groups.MACHINES, MULTIBLOCK_CLICKER_3, RecipeType.NULL, new ItemStack[]{
                HARDENED_METAL_INGOT, new ItemStack(Material.COPPER_BLOCK), HARDENED_METAL_INGOT,
                RECEIVER_3, ELECTRIC_MOTOR, MULTIBLOCK_CLICKER_2,
                HARDENED_METAL_INGOT, new ItemStack(Material.COPPER_BLOCK), HARDENED_METAL_INGOT
        }, MULTIBLOCK_CLICKER_3_SETTINGS).register(addon);



        new DataStripper(Groups.MACHINES, DATA_STRIPPER_1, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.IRON_BLOCK), ELECTRIC_MOTOR, new ItemStack(Material.IRON_BLOCK),
                RECEIVER_2, null, new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.IRON_BLOCK), ELECTRIC_MOTOR, new ItemStack(Material.IRON_BLOCK)
        }, DATA_STRIPPER_1_SETTINGS).register(addon);

        new DataStripper(Groups.MACHINES, DATA_STRIPPER_2, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.IRON_BLOCK), ELECTRIC_MOTOR, new ItemStack(Material.IRON_BLOCK),
                RECEIVER_3, DATA_STRIPPER_1, DIELECTRIC_3,
                new ItemStack(Material.IRON_BLOCK), ELECTRIC_MOTOR, new ItemStack(Material.IRON_BLOCK)
        }, DATA_STRIPPER_2_SETTINGS).register(addon);



        new Launchpad(Groups.MACHINES, LAUNCHPAD, RecipeType.NULL, new ItemStack[]{
                ELECTRO_MAGNET, ELECTRO_MAGNET, ELECTRO_MAGNET,
                RECEIVER_2, new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE), TRANSFORMER_COIL_1,
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_INGOT)
        }, LAUNCHPAD_SETTINGS).register(addon);



        new ItemProjector(Groups.MACHINES, ITEM_PROJECTOR, RecipeType.NULL, new ItemStack[]{
                null, HOLOGRAM_PROJECTOR, null,
                RECEIVER_3, POWER_CRYSTAL, TRANSCEIVER_3,
                FERROSILICON, FERROSILICON, FERROSILICON
        }, ITEM_PROJECTOR_SETTINGS).register(addon);
    }
}
