package org.metamechanists.quaptics.implementation.multiblocks.entangler.magnet;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.models.transformations.TransformationMatrixBuilder;


public class EntanglementMagnetTop extends EntanglementMagnet {

    public static final Settings ENTANGLEMENT_MAGNET_TOP_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(7)
            .build();
    public static final SlimefunItemStack ENTANGLEMENT_MAGNET_TOP = new SlimefunItemStack(
            "QP_ENTANGLEMENT_MAGNET_TOP",
            Material.ORANGE_CONCRETE,
            "&6Entanglement Magnet &8(top)",
            Lore.create(ENTANGLEMENT_MAGNET_TOP_SETTINGS,
                    "&7● Multiblock component"));

    private static final Vector3f PLATE_SCALE = new Vector3f(0.50F, 0.10F, 0.50F);
    private static final Vector3f PLATE_OFFSET = new Vector3f(0.0F, -0.20F, 0.0F);
    private static final Vector3f PLATE_ROTATION = new Vector3f(0.0F, (float) (Math.PI / 4), 0.0F);

    public EntanglementMagnetTop(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        super.initDisplays(displayGroup, location, player);
        displayGroup.addDisplay("plate", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.LIGHT_GRAY_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PLATE_SCALE)
                        .rotate(PLATE_ROTATION)
                        .translate(PLATE_OFFSET)
                        .buildForBlockDisplay())
                .build());
    }
}
