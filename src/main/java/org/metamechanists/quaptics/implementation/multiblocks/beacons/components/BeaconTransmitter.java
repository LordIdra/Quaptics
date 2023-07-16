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


public class BeaconTransmitter extends QuapticBlock {
    public static final Settings BEACON_TRANSMITTER_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack BEACON_TRANSMITTER = new SlimefunItemStack(
            "QP_BEACON_TRANSMITTER",
            Material.IRON_BARS,
            "&6Beacon Transmitter",
            Lore.create(BEACON_TRANSMITTER_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    public BeaconTransmitter(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.LIGHT_GRAY_CONCRETE)
                        .size(0.2F, 1.01F, 0.2F))
                .buildAtBlockCenter(location);
    }
    @Override
    @NotNull
    protected Material getBaseMaterial() {
        return Material.IRON_BARS;
    }
}
