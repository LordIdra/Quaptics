package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import lombok.experimental.UtilityClass;
import org.metamechanists.quaptics.items.groups.Primitive;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;


@SuppressWarnings("PublicConstructorInNonPublicClass")
@UtilityClass
public class RecipeTypes {
    public final RecipeInfusion RECIPE_INFUSION = new RecipeInfusion();

    private final class RecipeInfusion extends RecipeType {
        public RecipeInfusion() {
            super(
                    Keys.RECIPE_INFUSION_CONTAINER,
                    new CustomItemStack(
                            Primitive.INFUSION_CONTAINER.clone(),
                            Colors.QUAPTICS + "Infusion",
                            "&7Made in the Infuser multiblock")
            );
        }
    }
}
