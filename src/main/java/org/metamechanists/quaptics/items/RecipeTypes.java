package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;

import static org.metamechanists.quaptics.implementation.multiblocks.entangler.EntanglementContainer.ENTANGLEMENT_CONTAINER;
import static org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionContainer.INFUSION_CONTAINER;

@UtilityClass
public class RecipeTypes {
    public final RecipeInfusion RECIPE_INFUSION = new RecipeInfusion();
    public final RecipeInfusion RECIPE_ENTANGLEMENT = new RecipeInfusion();

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

    private final class RecipeEntanglement extends RecipeType {
        private RecipeEntanglement() {
            super(
                    Keys.RECIPE_ENTANGLER,
                    new CustomItemStack(
                            ENTANGLEMENT_CONTAINER.clone(),
                            Colors.QUAPTICS + "Entanglement",
                            "&7Made in the Entangler multiblock")
            );
        }
    }
}
