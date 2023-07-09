package org.metamechanists.quaptics.implementation.blocks.testing;

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
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.List;
import java.util.Optional;

public class TwoItemDisplayTest extends ConnectedBlock implements ItemHolderBlock {
    public static final Settings DOUBLE_ITEM_TEST_SETTINGS = Settings.builder()
            .tier(Tier.TESTING)
            .build();
    public static final SlimefunItemStack DOUBLE_ITEM_TEST = new SlimefunItemStack(
            "DOUBLE_ITEM_TEST",
            Material.TINTED_GLASS,
            "&dDouble Item Test",
            Lore.create(DOUBLE_ITEM_TEST_SETTINGS,
                    "&7● Has &e2 &7Items :P",
                    "&7● &eFear &7my code Idra"));

    private static final Vector3f BASE_SCALE = new Vector3f(0.90F, 0.30F, 0.90F);
    private static final Vector3f BASE_TRANSLATION = new Vector3f(0, -0.5F, 0);
    private static final Vector3f MAIN_SCALE = new Vector3f(0.70F, 0.15F, 0.70F);
    private static final Vector3f MAIN_ROTATION = new Vector3f(0, (float)(Math.PI/4), 0);
    private static final Vector3f MAIN_TRANSLATION = new Vector3f(0, -0.20F, 0);
    private static final Vector3f ITEM_SCALE = new Vector3f(0.50F);
    private static final Vector3f ITEM_ROTATION = new Vector3f(0, (float)(Math.PI/4), 0);

    public TwoItemDisplayTest(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.60F;
    }

    @Override
    public void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, @NotNull final Player player) {
        displayGroup.addDisplay("base", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.YELLOW_CONCRETE)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(BASE_SCALE)
                        .translate(BASE_TRANSLATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.TINTED_GLASS)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(MAIN_SCALE)
                        .rotate(MAIN_ROTATION)
                        .translate(MAIN_TRANSLATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(ITEM_SCALE)
                        .buildForItemDisplay())
                .build());
        displayGroup.addDisplay("displayItem", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(ITEM_SCALE)
                        .rotate(ITEM_ROTATION)
                        .buildForItemDisplay())
                .build());
    }

    @Override
    protected void onRightClick(@NotNull Location location, @NotNull Player player) {
        itemHolderInteract(location, player);
    }

    @Override
    public boolean onInsert(@NotNull Location location, @NotNull ItemStack stack, @NotNull Player player) {
        getItemDisplay(location, "displayItem").ifPresent(display -> display.setItemStack(stack.clone()));
        return true;
    }

    @Override
    public Optional<ItemStack> onRemove(@NotNull Location location, @NotNull ItemStack stack) {
        getItemDisplay(location, "displayItem").ifPresent(display -> display.setItemStack(new ItemStack(Material.AIR)));
        return Optional.of(stack);
    }

    @Override
    protected List<ConnectionPoint> initConnectionPoints(ConnectionGroupId groupId, Player player, Location location) {
        return null;
    }
}
