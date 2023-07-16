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
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;


public class BeaconBattery extends QuapticBlock {
    public static final Settings BEACON_BATTERY_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack BEACON_BATTERY = new SlimefunItemStack(
            "QP_BEACON_BATTERY",
            Material.WHITE_CONCRETE,
            "&6Beacon Battery",
            Lore.create(BEACON_BATTERY_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    public BeaconBattery(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("coil1", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.1F, 0.8F, 1.1F)
                        .location(0.3F, 0, 0))
                .add("coil2", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.1F, 0.8F, 1.1F)
                        .location(-0.3F, 0, 0))
                .add("coil3", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(1.1F, 0.8F, 0.1F)
                        .location(0, 0, 0.3F))
                .add("coil4", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_ON)
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
