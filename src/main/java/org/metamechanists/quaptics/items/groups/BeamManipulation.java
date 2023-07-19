package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_3;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Transformer.TRANSFORMER_3_SETTINGS;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.*;


@UtilityClass
public class BeamManipulation {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new Lens(Groups.BEAM_MANIPULATION, LENS_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                RECEIVER_1, TRANSCEIVER_1, TRANSMITTER_1,
                null, null, null
        }, LENS_1_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, LENS_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                RECEIVER_2, TRANSCEIVER_2, TRANSMITTER_2,
                null, null, null
        }, LENS_2_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, LENS_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                RECEIVER_3, TRANSCEIVER_3, TRANSMITTER_3,
                null, null, null
        }, LENS_3_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, LENS_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                RECEIVER_4, TRANSCEIVER_4, TRANSMITTER_4,
                null, null, null
        }, LENS_4_SETTINGS).register(addon);



        new Splitter(Groups.BEAM_MANIPULATION, SPLITTER_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, TRANSMITTER_1,
                RECEIVER_1, TRANSCEIVER_1, null,
                null, null, TRANSMITTER_1
        }, SPLITTER_1_SETTINGS).register(addon);

        new Splitter(Groups.BEAM_MANIPULATION, SPLITTER_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, TRANSMITTER_2,
                RECEIVER_2, TRANSCEIVER_2, TRANSMITTER_2,
                null, null, TRANSMITTER_2
        }, SPLITTER_2_SETTINGS).register(addon);

        new Splitter(Groups.BEAM_MANIPULATION, SPLITTER_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, TRANSMITTER_3, TRANSMITTER_3,
                RECEIVER_3, TRANSCEIVER_3, null,
                null, TRANSMITTER_3, TRANSMITTER_3
        }, SPLITTER_3_SETTINGS).register(addon);

        new Splitter(Groups.BEAM_MANIPULATION, SPLITTER_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, TRANSMITTER_4, TRANSMITTER_4,
                RECEIVER_4, TRANSCEIVER_4, TRANSMITTER_4,
                null, TRANSMITTER_4, TRANSMITTER_4
        }, SPLITTER_4_SETTINGS).register(addon);



        new Combiner(Groups.BEAM_MANIPULATION, COMBINER_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                RECEIVER_1, null, null,
                null, TRANSCEIVER_1, TRANSMITTER_1,
                RECEIVER_1, null, null
        }, COMBINER_1_SETTINGS).register(addon);

        new Combiner(Groups.BEAM_MANIPULATION, COMBINER_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                RECEIVER_2, null, null,
                RECEIVER_2, TRANSCEIVER_2, TRANSMITTER_2,
                RECEIVER_2, null, null
        }, COMBINER_2_SETTINGS).register(addon);

        new Combiner(Groups.BEAM_MANIPULATION, COMBINER_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                RECEIVER_3, RECEIVER_3, null,
                null, TRANSCEIVER_3, TRANSMITTER_3,
                RECEIVER_3, RECEIVER_3, null
        }, COMBINER_3_SETTINGS).register(addon);

        new Combiner(Groups.BEAM_MANIPULATION, COMBINER_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                RECEIVER_4, RECEIVER_4, null,
                RECEIVER_4, TRANSCEIVER_4, TRANSMITTER_4,
                RECEIVER_4, RECEIVER_4, null
        }, COMBINER_4_SETTINGS).register(addon);



        new Capacitor(Groups.BEAM_MANIPULATION, CAPACITOR_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, DIELECTRIC_1, null,
                RECEIVER_1, TRANSCEIVER_1, TRANSMITTER_1,
                null, DIELECTRIC_1, null
        }, CAPACITOR_1_SETTINGS).register(addon);

        new Capacitor(Groups.BEAM_MANIPULATION, CAPACITOR_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, DIELECTRIC_2, null,
                RECEIVER_2, TRANSCEIVER_2, TRANSMITTER_2,
                null, DIELECTRIC_2, null
        }, CAPACITOR_2_SETTINGS).register(addon);

        new Capacitor(Groups.BEAM_MANIPULATION, CAPACITOR_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, DIELECTRIC_3, null,
                RECEIVER_3, TRANSCEIVER_3, TRANSMITTER_3,
                null, DIELECTRIC_3, null
        }, CAPACITOR_3_SETTINGS).register(addon);

        new Capacitor(Groups.BEAM_MANIPULATION, CAPACITOR_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, DIELECTRIC_4, null,
                RECEIVER_4, TRANSCEIVER_4, TRANSMITTER_4,
                null, DIELECTRIC_4, null
        }, CAPACITOR_4_SETTINGS).register(addon);



        new Transformer(Groups.BEAM_MANIPULATION, TRANSFORMER_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                TRANSFORMER_COIL_1, null, TRANSFORMER_COIL_1,
                RECEIVER_1, TRANSCEIVER_2, TRANSMITTER_1,
                TRANSFORMER_COIL_1, null, TRANSFORMER_COIL_1
        }, TRANSFORMER_1_SETTINGS).register(addon);

        new Transformer(Groups.BEAM_MANIPULATION, TRANSFORMER_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                TRANSFORMER_COIL_2, null, TRANSFORMER_COIL_2,
                RECEIVER_2, TRANSCEIVER_2, TRANSMITTER_2,
                TRANSFORMER_COIL_2, null, TRANSFORMER_COIL_2
        }, TRANSFORMER_2_SETTINGS).register(addon);

        new Transformer(Groups.BEAM_MANIPULATION, TRANSFORMER_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                TRANSFORMER_COIL_3, null, TRANSFORMER_COIL_3,
                RECEIVER_3, TRANSCEIVER_3, TRANSMITTER_3,
                TRANSFORMER_COIL_3, null, TRANSFORMER_COIL_3
        }, TRANSFORMER_3_SETTINGS).register(addon);
    }
}
