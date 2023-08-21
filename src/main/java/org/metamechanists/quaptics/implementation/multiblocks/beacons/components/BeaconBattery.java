package org.metamechanists.quaptics.implementation.multiblocks.beacons.components;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.base.QuapticBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;


public class BeaconBattery extends QuapticBlock {
    public static final Settings BEACON_BATTERY_1_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .operatingPowerHidden(true)
            .build();
    public static final Settings BEACON_BATTERY_2_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .operatingPowerHidden(true)
            .build();

    public static final SlimefunItemStack BEACON_BATTERY_1 = new SlimefunItemStack(
            "QP_BEACON_BATTERY_1",
            Material.WHITE_CONCRETE,
            "&dBeacon Battery &5I",
            Lore.create(BEACON_BATTERY_1_SETTINGS,
                    Lore.multiblockComponent()));
    public static final SlimefunItemStack BEACON_BATTERY_2 = new SlimefunItemStack(
            "QP_BEACON_BATTERY_2",
            Material.WHITE_CONCRETE,
            "&dBeacon Battery &5II",
            Lore.create(BEACON_BATTERY_2_SETTINGS,
                    Lore.multiblockComponent()));

    public BeaconBattery(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("coil1", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .size(0.1F, 0.8F, 1.1F)
                        .location(0.3F, 0, 0))
                .add("coil2", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .size(0.1F, 0.8F, 1.1F)
                        .location(-0.3F, 0, 0))
                .add("coil3", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .size(1.1F, 0.8F, 0.1F)
                        .location(0, 0, 0.3F))
                .add("coil4", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .size(1.1F, 0.8F, 0.1F)
                        .location(0, 0, -0.3F))
                .buildAtBlockCenter(location);
    }
    @Override
    @NotNull
    protected Material getBaseMaterial() {
        return Material.WHITE_CONCRETE;
    }
}
