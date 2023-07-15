package org.metamechanists.quaptics.implementation.beacons.modules;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;


public class TestModule extends BeaconModule {
    public static final Settings TEST_MODULE_SETTINGS = Settings.builder()
            .tier(Tier.TESTING)
            .build();
    public static final SlimefunItemStack TEST_MODULE = new SlimefunItemStack(
            "QP_MODULE_TEST",
            Material.BLUE_BANNER,
            "&6Aaaaaaaaaaaa",
            Lore.create(TEST_MODULE_SETTINGS,
                    "&7● help"));

    public TestModule(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }
}
