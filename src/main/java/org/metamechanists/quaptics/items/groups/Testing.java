package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.testing.OscillatingConcentrator;
import org.metamechanists.quaptics.implementation.blocks.testing.TwoItemDisplayTest;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.testing.OscillatingConcentrator.OSCILLATING_CONCENTRATOR;
import static org.metamechanists.quaptics.implementation.blocks.testing.OscillatingConcentrator.OSCILLATING_CONCENTRATOR_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.testing.TwoItemDisplayTest.DOUBLE_ITEM_TEST;
import static org.metamechanists.quaptics.implementation.blocks.testing.TwoItemDisplayTest.DOUBLE_ITEM_TEST_SETTINGS;


@UtilityClass
public class Testing {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new OscillatingConcentrator(
                Groups.TESTING,
                OSCILLATING_CONCENTRATOR,
                RecipeType.NULL,
                new ItemStack[]{},
                OSCILLATING_CONCENTRATOR_SETTINGS).register(addon);

        new TwoItemDisplayTest(
                Groups.TESTING,
                DOUBLE_ITEM_TEST,
                RecipeType.NULL,
                new ItemStack[]{},
                DOUBLE_ITEM_TEST_SETTINGS).register(addon);
    }
}
