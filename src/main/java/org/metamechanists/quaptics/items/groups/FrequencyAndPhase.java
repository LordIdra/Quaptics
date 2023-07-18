package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Interferometer;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Polariser;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer;
import org.metamechanists.quaptics.items.Groups;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.FERROSILICON;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.HARDENED_METAL_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.REINFORCED_ALLOY_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.URANIUM;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating.DIFFRACTION_GRATING_1;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating.DIFFRACTION_GRATING_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating.DIFFRACTION_GRATING_2;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating.DIFFRACTION_GRATING_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Interferometer.INTERFEROMETER;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Interferometer.INTERFEROMETER_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Polariser.POLARISER;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Polariser.POLARISER_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_1;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_2;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_3;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_1;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_2;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_3;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_3_SETTINGS;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.*;


@UtilityClass
public class FrequencyAndPhase {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new Repeater(Groups.FREQUENCY_AND_PHASE, REPEATER_1, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                RECEIVER_2, new ItemStack(Material.REPEATER), TRANSMITTER_2,
                FERROSILICON, new ItemStack(Material.REDSTONE_BLOCK), FERROSILICON
        }, REPEATER_1_SETTINGS).register(addon);

        new Repeater(Groups.FREQUENCY_AND_PHASE, REPEATER_2, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                RECEIVER_3, REPEATER_1, TRANSMITTER_3,
                HARDENED_METAL_INGOT, INFUSED_FREQUENCY_CRYSTAL, HARDENED_METAL_INGOT
        }, REPEATER_2_SETTINGS).register(addon);

        new Repeater(Groups.FREQUENCY_AND_PHASE, REPEATER_3, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                RECEIVER_4, REPEATER_2, TRANSMITTER_4,
                URANIUM, ENTANGLED_FREQUENCY_CRYSTAL, URANIUM
        }, REPEATER_3_SETTINGS).register(addon);



        new Scatterer(Groups.FREQUENCY_AND_PHASE, SCATTERER_1, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                RECEIVER_2, new ItemStack(Material.COMPARATOR), TRANSMITTER_2,
                FERROSILICON, new ItemStack(Material.REDSTONE_BLOCK), FERROSILICON
        }, SCATTERER_1_SETTINGS).register(addon);

        new Scatterer(Groups.FREQUENCY_AND_PHASE, SCATTERER_2, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                RECEIVER_3, SCATTERER_1, TRANSMITTER_3,
                HARDENED_METAL_INGOT, INFUSED_FREQUENCY_CRYSTAL, HARDENED_METAL_INGOT
        }, SCATTERER_2_SETTINGS).register(addon);

        new Scatterer(Groups.FREQUENCY_AND_PHASE, SCATTERER_3, RecipeType.NULL, new ItemStack[]{
                new ItemStack(Material.GLASS), new ItemStack(Material.GLASS), new ItemStack(Material.GLASS),
                RECEIVER_4, SCATTERER_2, TRANSMITTER_4,
                URANIUM, ENTANGLED_FREQUENCY_CRYSTAL, URANIUM
        }, SCATTERER_3_SETTINGS).register(addon);



        new Polariser(Groups.FREQUENCY_AND_PHASE, POLARISER, RecipeType.NULL, new ItemStack[]{
                REINFORCED_ALLOY_INGOT, TRANSCEIVER_3, REINFORCED_ALLOY_INGOT,
                RECEIVER_3, null, TRANSMITTER_3,
                REINFORCED_ALLOY_INGOT, TRANSCEIVER_3, REINFORCED_ALLOY_INGOT
        }, POLARISER_SETTINGS).register(addon);



        new Interferometer(Groups.FREQUENCY_AND_PHASE, INTERFEROMETER, RecipeType.NULL, new ItemStack[]{
                REINFORCED_ALLOY_INGOT, POLARISER, REINFORCED_ALLOY_INGOT,
                RECEIVER_3, POLARISER, TRANSMITTER_3,
                REINFORCED_ALLOY_INGOT, POLARISER, REINFORCED_ALLOY_INGOT
        }, INTERFEROMETER_SETTINGS).register(addon);



        new DiffractionGrating(Groups.FREQUENCY_AND_PHASE, DIFFRACTION_GRATING_1, RecipeType.NULL, new ItemStack[]{
                DIELECTRIC_3, PHASE_CRYSTAL_180, DIELECTRIC_3,
                RECEIVER_3, POLARISER, TRANSMITTER_3,
                DIELECTRIC_3, ENTANGLED_FREQUENCY_CRYSTAL, DIELECTRIC_3
        }, DIFFRACTION_GRATING_1_SETTINGS).register(addon);

        new DiffractionGrating(Groups.FREQUENCY_AND_PHASE, DIFFRACTION_GRATING_2, RecipeType.NULL, new ItemStack[]{
                DIELECTRIC_4, PHASE_CRYSTAL_180, DIELECTRIC_4,
                RECEIVER_4, DIFFRACTION_GRATING_1, TRANSMITTER_4,
                DIELECTRIC_4, ENTANGLED_FREQUENCY_CRYSTAL, DIELECTRIC_4
        }, DIFFRACTION_GRATING_2_SETTINGS).register(addon);
    }
}
