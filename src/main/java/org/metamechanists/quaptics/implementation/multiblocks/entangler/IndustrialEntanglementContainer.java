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
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.displaymodellib.models.components.ModelLine;
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

import static org.metamechanists.quaptics.implementation.multiblocks.entangler.EntanglementMagnet.ENTANGLEMENT_MAGNET;


public class IndustrialEntanglementContainer extends EntanglementContainer {
    public static final Settings INDUSTRIAL_ENTANGLEMENT_CONTAINER_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .timePerItem(15)
            .build();
    public static final SlimefunItemStack INDUSTRIAL_ENTANGLEMENT_CONTAINER = new SlimefunItemStack(
            "QP_INDUSTRIAL_ENTANGLEMENT_CONTAINER",
            Material.CYAN_CONCRETE,
            "&6Industrial Entanglement Container",
            Lore.create(INDUSTRIAL_ENTANGLEMENT_CONTAINER_SETTINGS,
                    Lore.multiblock(),
                    "&7● Entangles items",
                    "&7● Can entangle up to 16 items at once",
                    "&7● &eRight Click &7with an item to start the entanglement process"));

    // I hate this stupid language jesus christ
    private static final Map<Vector, ItemStack> MAGNETS = Map.ofEntries(
            Map.entry(new Vector(3, 0, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(-3, 0, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, 3, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, -3, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, 0, 3), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, 0, -3), ENTANGLEMENT_MAGNET),

            Map.entry(new Vector(2, 2, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(2, -2, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(-2, 2, 0), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(2, 2, 0), ENTANGLEMENT_MAGNET),

            Map.entry(new Vector(2, 0, 2), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(2, 0, -2), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(-2, 0, 2), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(-2, 0, -2), ENTANGLEMENT_MAGNET),

            Map.entry(new Vector(0, 2, 2), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, 2, -2), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, -2, 2), ENTANGLEMENT_MAGNET),
            Map.entry(new Vector(0, -2, -2), ENTANGLEMENT_MAGNET));

    public IndustrialEntanglementContainer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("frame1a", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, 0.6F, 0)
                        .to(0.6F, 0, 0)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1b", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, 0.6F, 0)
                        .to(-0.6F, 0, 0)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1c", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, 0.6F, 0)
                        .to(0, 0, 0.6F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1d", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, 0.6F, 0)
                        .to(0, 0, -0.6F)
                        .thickness(0.1F)
                        .extraLength(0.1F))

                .add("frame2a", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, -0.6F, 0)
                        .to(0.6F, 0, 0)
                        .thickness(0.1F))
                .add("frame2b", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, -0.6F, 0)
                        .to(-0.6F, 0, 0)
                        .thickness(0.1F))
                .add("frame2c", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, -0.6F, 0)
                        .to(0, 0, 0.6F)
                        .thickness(0.1F))
                .add("frame2d", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0, -0.6F, 0)
                        .to(0, 0, -0.6F)
                        .thickness(0.1F))

                .add("frame3a", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0.6F, 0, 0)
                        .to(0, 0, 0.6F)
                        .thickness(0.1F))
                .add("frame3b", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0.6F, 0, 0)
                        .to(0, 0, -0.6F)
                        .thickness(0.1F))
                .add("frame3c", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.6F, 0, 0)
                        .to(0, 0, 0.6F)
                        .thickness(0.1F))
                .add("frame3d", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.6F, 0, 0)
                        .to(0, 0, -0.6F)
                        .thickness(0.1F))
                
                .add("item", new ModelItem()
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.5F))
                .buildAtBlockCenter(location);
    }

    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        final Map<ItemStack, ItemStack> recipes = new HashMap<>();
        for (final Entry<ItemStack, ItemStack> recipe : super.getRecipes().entrySet()) {
            for (int i = 0; i < 16; i++) {
                final ItemStack input = recipe.getKey().clone();
                final ItemStack output = recipe.getValue().clone();
                input.add(i);
                output.add(i);
                recipes.put(input, output);
            }
        }
        return recipes;
    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        return MAGNETS;
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
        ItemHolderBlock.insertItem(location, name, newItemStack);
        BlockStorageAPI.set(location, Keys.BS_IS_HOLDING_ITEM, true);
    }
}
