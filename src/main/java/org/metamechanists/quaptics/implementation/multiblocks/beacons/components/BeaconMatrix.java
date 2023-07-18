package org.metamechanists.quaptics.implementation.multiblocks.beacons.components;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.base.QuapticBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;


public class BeaconMatrix extends QuapticBlock {
    public static final Settings BEACON_MATRIX_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .operatingPowerHidden(true)
            .build();

    public static final SlimefunItemStack BEACON_MATRIX = new SlimefunItemStack(
            "QP_BEACON_MATRIX",
            Material.LIGHT_BLUE_CONCRETE,
            "&dBeacon Matrix",
            Lore.create(BEACON_MATRIX_SETTINGS,
                    Lore.multiblockComponent()));

    public BeaconMatrix(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.LIGHT_BLUE_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(1.2F, 1.2F, 1.2F)
                        .rotation(Math.PI/4))
                .buildAtBlockCenter(location);
    }
}
