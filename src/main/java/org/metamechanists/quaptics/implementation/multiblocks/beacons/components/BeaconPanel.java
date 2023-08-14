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


public class BeaconPanel extends QuapticBlock {
    public static final Settings BEACON_PANEL_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .operatingPowerHidden(true)
            .build();

    public static final SlimefunItemStack BEACON_PANEL = new SlimefunItemStack(
            "QP_BEACON_PANEL",
            Material.BLACK_STAINED_GLASS_PANE,
            "&dBeacon Panel",
            Lore.create(BEACON_PANEL_SETTINGS,
                    Lore.multiblockComponent()));

    public BeaconPanel(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .buildAtBlockCenter(location);
    }
    @Override
    @NotNull
    protected Material getBaseMaterial() {
        return Material.BLACK_STAINED_GLASS_PANE;
    }
}
