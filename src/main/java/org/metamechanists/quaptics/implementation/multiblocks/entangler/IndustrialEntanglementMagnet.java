package org.metamechanists.quaptics.implementation.multiblocks.entangler;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;
import org.metamechanists.displaymodellib.models.components.ModelDiamond;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;


public class IndustrialEntanglementMagnet extends EntanglementMagnet {
    public static final Settings INDUSTRIAL_ENTANGLEMENT_MAGNET_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .minPower(1)
            .minFrequency(0)
            .build();
    public static final SlimefunItemStack INDUSTRIAL_ENTANGLEMENT_MAGNET = new SlimefunItemStack(
            "QP_INDUSTRIAL_ENTANGLEMENT_MAGNET",
            Material.MAGENTA_CONCRETE,
            "&6Industrial Entanglement Magnet",
            Lore.create(INDUSTRIAL_ENTANGLEMENT_MAGNET_SETTINGS,
                    Lore.multiblockComponent()));

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public IndustrialEntanglementMagnet(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.6F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("diamond", new ModelDiamond()
                        .material(Material.GRAY_CONCRETE)
                        .size(0.7F))
                .add("panel1", new ModelCuboid()
                        .material(Material.PINK_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.6F, 0.3F, 0.3F)
                        .rotation(ModelDiamond.ROTATION))
                .add("panel2", new ModelCuboid()
                        .material(Material.PINK_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.3F, 0.6F, 0.3F)
                        .rotation(ModelDiamond.ROTATION))
                .add("panel3", new ModelCuboid()
                        .material(Material.PINK_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.3F, 0.3F, 0.6F)
                        .rotation(ModelDiamond.ROTATION))
                .buildAtBlockCenter(location);
    }
}
