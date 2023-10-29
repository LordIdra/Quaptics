package org.metamechanists.quaptics.implementation.multiblocks.infuser;

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
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static org.metamechanists.quaptics.implementation.multiblocks.infuser.IndustrialInfusionPillar.INDUSTRIAL_INFUSION_PILLAR;


public class IndustrialInfusionContainer extends InfusionContainer {
    public static final Settings INDUSTRIAL_INFUSION_CONTAINER_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .operatingPowerHidden(true)
            .timePerItem(30)
            .build();
    public static final SlimefunItemStack INDUSTRIAL_INFUSION_CONTAINER = new SlimefunItemStack(
            "QP_INDUSTRIAL_INFUSION_CONTAINER",
            Material.GRAY_CONCRETE,
            "&6Industrial Infusion Container",
            Lore.create(INDUSTRIAL_INFUSION_CONTAINER_SETTINGS,
                    Lore.multiblock(),
                    "&7● Infuses items",
                    "&7● Can infuse up to 16 items at once",
                    "&7● &eRight Click &7with an item to start infusing"));

    private static final Map<Vector, ItemStack> PILLARS = Map.of(
            new Vector(3, 0, 0), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(2, 0, 2), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(0, 0, 3), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(-2, 0, 2), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(-3, 0, 0), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(-2, 0, -2), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(0, 0, -3), INDUSTRIAL_INFUSION_PILLAR,
            new Vector(2, 0, -2), INDUSTRIAL_INFUSION_PILLAR);

    public IndustrialInfusionContainer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("base", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .facing(player.getFacing())
                        .size(1.3F, 0.4F, 1.3F)
                        .location(0, -0.3F, 0))
                .add("pillar1", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .facing(player.getFacing())
                        .location(-0.6F, -0.1F, -0.6F)
                        .size(0.25F, 0.8F, 0.25F))
                .add("pillar2", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .facing(player.getFacing())
                        .location(-0.6F, -0.1F, 0.6F)
                        .size(0.25F, 0.8F, 0.25F))
                .add("pillar3", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .facing(player.getFacing())
                        .location(0.6F, -0.1F, -0.6F)
                        .size(0.25F, 0.8F, 0.25F))
                .add("pillar4", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .facing(player.getFacing())
                        .location(0.6F, -0.1F, 0.6F)
                        .size(0.25F, 0.8F, 0.25F))
                .add("plate", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(player.getFacing())
                        .location(0, -0.1F, 0)
                        .size(0.6F, 0.1F, 0.6F))
                .add("item", new ModelItem()
                        .brightness(Utils.BRIGHTNESS_ON)
                        .location(0, 0.4F, 0)
                        .size(0.8F))
                .buildAtBlockCenter(location);
    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        return PILLARS;
    }

    @Override
    public void itemHolderInteract(@NotNull final Location location, @NotNull final String name, @NotNull final Player player) {
        final Optional<ItemStack> currentStack = removeItem(location, name);
        BlockStorageAPI.set(location, Keys.BS_IS_HOLDING_ITEM, false);
        if (currentStack.isPresent() && !isEmptyItemStack(currentStack.get())) {
            onRemove(location, name, currentStack.get()).ifPresent(itemStack -> ItemUtils.addOrDropItemMainHand(player, itemStack));
            return;
        }

        final ItemStack itemStack = player.getInventory().getItemInMainHand();
        final int amount = Math.min(itemStack.getAmount(), 16);
        final ItemStack newItemStack = itemStack.clone();
        newItemStack.setAmount(amount);
        if (newItemStack.getType().isEmpty() || !onInsert(location, name, newItemStack, player)) {
            return;
        }

        itemStack.subtract(amount);
        ItemHolderBlock.insertItem(location, name, itemStack);
        BlockStorageAPI.set(location, Keys.BS_IS_HOLDING_ITEM, true);
    }

    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        final Map<ItemStack, ItemStack> recipes = new HashMap<>();
        for (final Entry<ItemStack, ItemStack> recipe : super.getRecipes().entrySet()) {
            for (int i = 0; i < 8; i++) {
                final ItemStack input = recipe.getKey().clone();
                final ItemStack output = recipe.getValue().clone();
                input.add(i);
                output.add(i);
                recipes.put(input, output);
            }
        }
        return recipes;
    }
}