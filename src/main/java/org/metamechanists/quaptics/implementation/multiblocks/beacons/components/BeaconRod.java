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
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;


public class BeaconRod extends QuapticBlock {
    public static final Settings BEACON_ROD_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack BEACON_ROD = new SlimefunItemStack(
            "QP_BEACON_ROD",
            Material.GRAY_CONCRETE,
            "&6Beacon Rod",
            Lore.create(BEACON_ROD_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    public BeaconRod(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .size(0.4F, 1.01F, 0.4F))
                .buildAtBlockCenter(location);
    }
    @Override
    @NotNull
    protected Material getBaseMaterial() {
        return Material.NETHER_BRICK_FENCE;
    }
}
