package org.metamechanists.quaptics.implementation.multiblocks.infuser;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;


public class IndustrialInfusionPillar extends InfusionPillar {
    public static final Settings INDUSTRIAL_INFUSION_PILLAR_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .minPower(100)
            .minFrequency(80)
            .build();
    public static final SlimefunItemStack INDUSTRIAL_INFUSION_PILLAR = new SlimefunItemStack(
            "QP_INDUSTRIAL_INFUSION_PILLAR",
            Material.BLUE_CONCRETE,
            "&6Industrial Infusion Pillar",
            Lore.create(INDUSTRIAL_INFUSION_PILLAR_SETTINGS,
                    Lore.multiblockComponent()));

    public IndustrialInfusionPillar(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.40F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("pillar", new ModelCuboid()
                        .material(Material.BLUE_CONCRETE)
                        .facing(player.getFacing())
                        .size(0.4F, 0.8F, 0.4F)
                        .location(0, -0.1F, 0))
                .add("prism", new ModelCuboid()
                        .material(Material.LIGHT_BLUE_STAINED_GLASS)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .facing(player.getFacing())
                        .size(0.3F))
                .buildAtBlockCenter(location);
    }
}
