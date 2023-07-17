package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Interferometer;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Polariser;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater;
import org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating.DIFFRACTION_GRATING;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.DiffractionGrating.DIFFRACTION_GRATING_SETTINGS;
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
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_4;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Repeater.REPEATER_4_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_1;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_2;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_3;
import static org.metamechanists.quaptics.implementation.blocks.upgraders.Scatterer.SCATTERER_3_SETTINGS;


@UtilityClass
public class FrequencyAndPhase {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new Repeater(Groups.BEAM_CREATION, REPEATER_1, RecipeType.NULL, new ItemStack[]{

        }, REPEATER_1_SETTINGS).register(addon);

        new Repeater(Groups.BEAM_CREATION, REPEATER_2, RecipeType.NULL, new ItemStack[]{

        }, REPEATER_2_SETTINGS).register(addon);

        new Repeater(Groups.BEAM_CREATION, REPEATER_3, RecipeType.NULL, new ItemStack[]{

        }, REPEATER_3_SETTINGS).register(addon);

        new Repeater(Groups.BEAM_CREATION, REPEATER_4, RecipeType.NULL, new ItemStack[]{

        }, REPEATER_4_SETTINGS).register(addon);



        new Scatterer(Groups.BEAM_CREATION, SCATTERER_1, RecipeType.NULL, new ItemStack[]{

        }, SCATTERER_1_SETTINGS).register(addon);

        new Scatterer(Groups.BEAM_CREATION, SCATTERER_2, RecipeType.NULL, new ItemStack[]{

        }, SCATTERER_2_SETTINGS).register(addon);

        new Scatterer(Groups.BEAM_CREATION, SCATTERER_3, RecipeType.NULL, new ItemStack[]{

        }, SCATTERER_3_SETTINGS).register(addon);



        new Polariser(Groups.BEAM_CREATION, POLARISER, RecipeType.NULL, new ItemStack[]{

        }, POLARISER_SETTINGS).register(addon);



        new Interferometer(Groups.BEAM_CREATION, INTERFEROMETER, RecipeType.NULL, new ItemStack[]{

        }, INTERFEROMETER_SETTINGS).register(addon);



        new DiffractionGrating(Groups.BEAM_CREATION, DIFFRACTION_GRATING, RecipeType.NULL, new ItemStack[]{

        }, DIFFRACTION_GRATING_SETTINGS).register(addon);
    }
}
