package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;

import static org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionContainer.INFUSION_CONTAINER;

@UtilityClass
public class RecipeTypes {
    public final RecipeInfusion RECIPE_INFUSION = new RecipeInfusion();

    private final class RecipeInfusion extends RecipeType {
        private RecipeInfusion() {
            super(
                    Keys.RECIPE_INFUSION_CONTAINER,
                    new CustomItemStack(
                            INFUSION_CONTAINER.clone(),
                            Colors.QUAPTICS + "Infusion",
                            "&7Made in the Infuser multiblock")
            );
        }
    }
}
