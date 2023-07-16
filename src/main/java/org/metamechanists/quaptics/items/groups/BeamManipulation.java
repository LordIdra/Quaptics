package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
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


@UtilityClass
public class BeamManipulation {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new Lens(Groups.BEAM_MANIPULATION, LENS_1, RecipeType.NULL, new ItemStack[]{

        }, LENS_1_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, LENS_2, RecipeType.NULL, new ItemStack[]{

        }, LENS_2_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, LENS_3, RecipeType.NULL, new ItemStack[]{

        }, LENS_3_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, LENS_4, RecipeType.NULL, new ItemStack[]{

        }, LENS_4_SETTINGS).register(addon);



        new Lens(Groups.BEAM_MANIPULATION, SPLITTER_1, RecipeType.NULL, new ItemStack[]{

        }, SPLITTER_1_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, SPLITTER_2, RecipeType.NULL, new ItemStack[]{

        }, SPLITTER_2_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, SPLITTER_3, RecipeType.NULL, new ItemStack[]{

        }, SPLITTER_3_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, SPLITTER_4, RecipeType.NULL, new ItemStack[]{

        }, SPLITTER_4_SETTINGS).register(addon);



        new Lens(Groups.BEAM_MANIPULATION, COMBINER_1, RecipeType.NULL, new ItemStack[]{

        }, COMBINER_1_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, COMBINER_2, RecipeType.NULL, new ItemStack[]{

        }, COMBINER_2_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, COMBINER_3, RecipeType.NULL, new ItemStack[]{

        }, COMBINER_3_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, COMBINER_4, RecipeType.NULL, new ItemStack[]{

        }, COMBINER_4_SETTINGS).register(addon);



        new Lens(Groups.BEAM_MANIPULATION, CAPACITOR_1, RecipeType.NULL, new ItemStack[]{

        }, CAPACITOR_1_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, CAPACITOR_2, RecipeType.NULL, new ItemStack[]{

        }, CAPACITOR_2_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, CAPACITOR_3, RecipeType.NULL, new ItemStack[]{

        }, CAPACITOR_3_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, CAPACITOR_4, RecipeType.NULL, new ItemStack[]{

        }, CAPACITOR_4_SETTINGS).register(addon);



        new Lens(Groups.BEAM_MANIPULATION, TRANSFORMER_1, RecipeType.NULL, new ItemStack[]{

        }, TRANSFORMER_1_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, TRANSFORMER_2, RecipeType.NULL, new ItemStack[]{

        }, TRANSFORMER_2_SETTINGS).register(addon);

        new Lens(Groups.BEAM_MANIPULATION, TRANSFORMER_3, RecipeType.NULL, new ItemStack[]{

        }, TRANSFORMER_3_SETTINGS).register(addon);
    }
}
