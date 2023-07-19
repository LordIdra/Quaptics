package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBattery;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconComputer;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconMatrix;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPanel;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPowerSupply;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconRod;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconTransmitter;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController1;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController2;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController3;
import org.metamechanists.quaptics.items.Groups;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.ADVANCED_CIRCUIT_BOARD;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.FERROSILICON;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.HARDENED_METAL_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.POWER_CRYSTAL;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBattery.BEACON_BATTERY_1;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBattery.BEACON_BATTERY_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBattery.BEACON_BATTERY_2;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBattery.BEACON_BATTERY_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBeam.BEACON_BEAM;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBeam.BEACON_BEAM_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconComputer.BEACON_COMPUTER;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconComputer.BEACON_COMPUTER_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconMatrix.BEACON_MATRIX;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconMatrix.BEACON_MATRIX_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPanel.BEACON_PANEL;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPanel.BEACON_PANEL_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPowerSupply.BEACON_POWER_SUPPLY_1;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPowerSupply.BEACON_POWER_SUPPLY_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPowerSupply.BEACON_POWER_SUPPLY_2;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPowerSupply.BEACON_POWER_SUPPLY_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconRod.BEACON_ROD;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconRod.BEACON_ROD_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconTransmitter.BEACON_TRANSMITTER;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconTransmitter.BEACON_TRANSMITTER_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController1.BEACON_CONTROLLER_1;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController1.BEACON_CONTROLLER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController2.BEACON_CONTROLLER_2;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController2.BEACON_CONTROLLER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController3.BEACON_CONTROLLER_3;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController3.BEACON_CONTROLLER_3_SETTINGS;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.CARBON_STRUCTURE;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.ENTANGLED_FREQUENCY_CRYSTAL;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.INFUSED_FREQUENCY_CRYSTAL;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.RECEIVER_3;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.RECEIVER_4;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSCEIVER_3;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSFORMER_COIL_2;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.TRANSFORMER_COIL_3;


@UtilityClass
public class BeaconComponents {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new BeaconController1(Groups.BEACON_COMPONENTS, BEACON_CONTROLLER_1, RecipeType.NULL, new ItemStack[]{

        }, BEACON_CONTROLLER_1_SETTINGS).register(addon);

        new BeaconController2(Groups.BEACON_COMPONENTS, BEACON_CONTROLLER_2, RecipeType.NULL, new ItemStack[]{

        }, BEACON_CONTROLLER_2_SETTINGS).register(addon);

        new BeaconController3(Groups.BEACON_COMPONENTS, BEACON_CONTROLLER_3, RecipeType.NULL, new ItemStack[]{

        }, BEACON_CONTROLLER_3_SETTINGS).register(addon);



        new BeaconPowerSupply(Groups.BEACON_COMPONENTS, BEACON_POWER_SUPPLY_1, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK),
                RECEIVER_3, TRANSCEIVER_3, TRANSCEIVER_3,
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK)
        }, BEACON_POWER_SUPPLY_1_SETTINGS).register(addon);

        new BeaconPowerSupply(Groups.BEACON_COMPONENTS, BEACON_POWER_SUPPLY_2, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK),
                RECEIVER_4, BEACON_POWER_SUPPLY_1, BEACON_POWER_SUPPLY_1,
                new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK), new ItemStack(Material.COPPER_BLOCK)
        }, BEACON_POWER_SUPPLY_2_SETTINGS).register(addon);



        new BeaconBattery(Groups.BEACON_COMPONENTS, BEACON_BATTERY_1, RecipeType.NULL, new ItemStack[]{
                POWER_CRYSTAL, TRANSFORMER_COIL_2, POWER_CRYSTAL,
                CARBON_STRUCTURE, CARBON_STRUCTURE, CARBON_STRUCTURE,
                POWER_CRYSTAL, TRANSFORMER_COIL_2, POWER_CRYSTAL,
        }, BEACON_BATTERY_1_SETTINGS).register(addon);

        new BeaconBattery(Groups.BEACON_COMPONENTS, BEACON_BATTERY_2, RecipeType.NULL, new ItemStack[]{
                CARBON_STRUCTURE, TRANSFORMER_COIL_3, CARBON_STRUCTURE,
                BEACON_BATTERY_1, CARBON_STRUCTURE, BEACON_BATTERY_1,
                CARBON_STRUCTURE, TRANSFORMER_COIL_3, CARBON_STRUCTURE,
        }, BEACON_BATTERY_2_SETTINGS).register(addon);



        new BeaconComputer(Groups.BEACON_COMPONENTS, BEACON_COMPUTER, RecipeType.NULL, new ItemStack[]{
                FERROSILICON, FERROSILICON, FERROSILICON,
                INFUSED_FREQUENCY_CRYSTAL, ADVANCED_CIRCUIT_BOARD, INFUSED_FREQUENCY_CRYSTAL,
                FERROSILICON, FERROSILICON, FERROSILICON
        }, BEACON_COMPUTER_SETTINGS).register(addon);



        new BeaconMatrix(Groups.BEACON_COMPONENTS, BEACON_MATRIX, RecipeType.NULL, new ItemStack[]{
                HARDENED_METAL_INGOT, HARDENED_METAL_INGOT, HARDENED_METAL_INGOT,
                ENTANGLED_FREQUENCY_CRYSTAL, BEACON_COMPUTER, ENTANGLED_FREQUENCY_CRYSTAL,
                HARDENED_METAL_INGOT, FERROSILICON, HARDENED_METAL_INGOT
        }, BEACON_MATRIX_SETTINGS).register(addon);



        new BeaconRod(Groups.BEACON_COMPONENTS, BEACON_BEAM, RecipeType.NULL, new ItemStack[]{
                null, CARBON_STRUCTURE, null,
                null, CARBON_STRUCTURE, null,
                null, CARBON_STRUCTURE, null
        }, BEACON_BEAM_SETTINGS).register(addon);



        new BeaconRod(Groups.BEACON_COMPONENTS, BEACON_ROD, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                CARBON_STRUCTURE, CARBON_STRUCTURE, CARBON_STRUCTURE,
                null, null, null
        }, BEACON_ROD_SETTINGS).register(addon);



        new BeaconPanel(Groups.BEACON_COMPONENTS, BEACON_PANEL, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                CARBON_STRUCTURE, CARBON_STRUCTURE, null,
                CARBON_STRUCTURE, CARBON_STRUCTURE, null,
        }, BEACON_PANEL_SETTINGS).register(addon);



        new BeaconTransmitter(Groups.BEACON_COMPONENTS, BEACON_TRANSMITTER, RecipeType.NULL, new ItemStack[]{
                null, null, null,
                null, TRANSCEIVER_3, null,
                null, CARBON_STRUCTURE, null,
        }, BEACON_TRANSMITTER_SETTINGS).register(addon);
    }
}