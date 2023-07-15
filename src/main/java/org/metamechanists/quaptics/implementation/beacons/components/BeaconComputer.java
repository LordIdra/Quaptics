package org.metamechanists.quaptics.implementation.beacons.components;

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
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;


public class BeaconComputer extends QuapticBlock {
    public static final Settings BEACON_COMPUTER_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack BEACON_COMPUTER = new SlimefunItemStack(
            "QP_BEACON_COMPUTER",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&6Beacon Computer",
            Lore.create(BEACON_COMPUTER_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    public BeaconComputer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.LIGHT_BLUE_STAINED_GLASS)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.8F, 1.0F, 0.8F))
                .buildAtBlockCenter(location);
    }
}
