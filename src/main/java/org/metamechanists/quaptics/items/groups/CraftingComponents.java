package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.UnplaceableBlock;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.RecipeTypes;
import org.metamechanists.quaptics.utils.Colors;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.*;


@UtilityClass
public class CraftingComponents {
    public final SlimefunItemStack TRANSMITTER_1 = new SlimefunItemStack(
            "QP_TRANSMITTER_1", Material.LIME_STAINED_GLASS_PANE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transmitter &7I");
    public final SlimefunItemStack TRANSMITTER_2 = new SlimefunItemStack(
            "QP_TRANSMITTER_2", Material.LIME_STAINED_GLASS, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transmitter &7II");
    public final SlimefunItemStack TRANSMITTER_3 = new SlimefunItemStack(
            "QP_TRANSMITTER_3", Material.LIME_CONCRETE_POWDER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transmitter &7III");
    public final SlimefunItemStack TRANSMITTER_4 = new SlimefunItemStack(
            "QP_TRANSMITTER_4", Material.LIME_CONCRETE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transmitter &7IV");

    public final SlimefunItemStack RECEIVER_1 = new SlimefunItemStack(
            "QP_RECEIVER_1", Material.RED_STAINED_GLASS_PANE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Receiver &7I");
    public final SlimefunItemStack RECEIVER_2 = new SlimefunItemStack(
            "QP_RECEIVER_2", Material.RED_STAINED_GLASS, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Receiver &7II");
    public final SlimefunItemStack RECEIVER_3 = new SlimefunItemStack(
            "QP_RECEIVER_3", Material.RED_CONCRETE_POWDER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Receiver &7III");
    public final SlimefunItemStack RECEIVER_4 = new SlimefunItemStack(
            "QP_RECEIVER_4", Material.RED_CONCRETE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Receiver &7IV");

    public final SlimefunItemStack TRANSCEIVER_1 = new SlimefunItemStack(
            "QP_TRANSCEIVER_1", Material.WHITE_STAINED_GLASS_PANE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transceiver &7I");
    public final SlimefunItemStack TRANSCEIVER_2 = new SlimefunItemStack(
            "QP_TRANSCEIVER_2", Material.WHITE_STAINED_GLASS, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transceiver &7II");
    public final SlimefunItemStack TRANSCEIVER_3 = new SlimefunItemStack(
            "QP_TRANSCEIVER_3", Material.WHITE_CONCRETE_POWDER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transceiver &7III");
    public final SlimefunItemStack TRANSCEIVER_4 = new SlimefunItemStack(
            "QP_TRANSCEIVER_4", Material.WHITE_CONCRETE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transceiver &7IV");

    public final SlimefunItemStack DIELECTRIC_1 = new SlimefunItemStack(
            "QP_DIELECTRIC_1", Material.LIGHT_BLUE_STAINED_GLASS_PANE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Dielectric &7I");
    public final SlimefunItemStack DIELECTRIC_2 = new SlimefunItemStack(
            "QP_DIELECTRIC_2", Material.LIGHT_BLUE_STAINED_GLASS, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Dielectric &7II");
    public final SlimefunItemStack DIELECTRIC_3 = new SlimefunItemStack(
            "QP_DIELECTRIC_3", Material.LIGHT_BLUE_CONCRETE_POWDER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Dielectric &7III");
    public final SlimefunItemStack DIELECTRIC_4 = new SlimefunItemStack(
            "QP_DIELECTRIC_4", Material.LIGHT_BLUE_CONCRETE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Dielectric &7IV");

    public final SlimefunItemStack TRANSFORMER_COIL_1 = new SlimefunItemStack(
            "QP_TRANSFORMER_COIL_1", Material.GREEN_GLAZED_TERRACOTTA, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transformer Coil &7I");
    public final SlimefunItemStack TRANSFORMER_COIL_2 = new SlimefunItemStack(
            "QP_TRANSFORMER_COIL_2", Material.YELLOW_GLAZED_TERRACOTTA, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transformer Coil &7II");
    public final SlimefunItemStack TRANSFORMER_COIL_3 = new SlimefunItemStack(
            "QP_TRANSFORMER_COIL_3", Material.RED_GLAZED_TERRACOTTA, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Transformer Coil &7III");

    public final SlimefunItemStack ENERGY_CONCENTRATION_ELEMENT_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATION_ELEMENT_1", Material.PEARLESCENT_FROGLIGHT, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Energy Concentration Element &7I");
    public final SlimefunItemStack ENERGY_CONCENTRATION_ELEMENT_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATION_ELEMENT_2", Material.OCHRE_FROGLIGHT, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Energy Concentration Element &7II");
    public final SlimefunItemStack ENERGY_CONCENTRATION_ELEMENT_3 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATION_ELEMENT_3", Material.VERDANT_FROGLIGHT, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Energy Concentration Element &7III");

    public final SlimefunItemStack TURRET_BASE = new SlimefunItemStack(
            "QP_TURRET_BASE", Material.SMOOTH_STONE_SLAB, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Turret Base");
    public final SlimefunItemStack TURRET_BARREL = new SlimefunItemStack(
            "QP_TURRET_BARREL", Material.GRAY_CONCRETE, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Turret Barrel");
    public final SlimefunItemStack TURRET_TARGETING_MECHANISM = new SlimefunItemStack(
            "QP_TURRET_TARGETING_MECHANISM", Material.TRIPWIRE_HOOK, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Turret Targeting Mechanism");

    public final SlimefunItemStack CARBON_STRUCTURE = enchant(new SlimefunItemStack(
            "QP_CARBON_STRUCTURE", Material.COAL_BLOCK, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Carbon Structure"));

    public final SlimefunItemStack BLANK_MODULE_CARD = new SlimefunItemStack(
            "QP_BLANK_MODULE_CARD", Material.BLACK_BANNER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Blank Module Card");
    public final SlimefunItemStack INFUSED_MODULE_CARD = new SlimefunItemStack(
            "QP_INFUSED_MODULE_CARD", Material.GRAY_BANNER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Infused Module Card");
    public final SlimefunItemStack ENTANGLED_MODULE_CARD = enchant(new SlimefunItemStack(
            "QP_ENTANGLED_MODULE_CARD", Material.GRAY_BANNER, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Entangled Module Card"));

    public final SlimefunItemStack INFUSED_FREQUENCY_CRYSTAL = new SlimefunItemStack(
            "QP_INFUSED_FREQUENCY_CRYSTAL", Material.PRISMARINE_CRYSTALS, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Infused Frequency Crystal");
    public final SlimefunItemStack ENTANGLED_FREQUENCY_CRYSTAL = enchant(new SlimefunItemStack(
            "QP_ENTANGLED_FREQUENCY_CRYSTAL", Material.PRISMARINE_CRYSTALS, Colors.CRAFTING_COMPONENTS.getFormattedColor() + "Entangled Frequency Crystal"));

    public final SlimefunItemStack PHASE_CRYSTAL_1 = enchant(new SlimefunItemStack(
            "QP_PHASE_CRYSTAL_1", Material.QUARTZ, "&7Phase Crystal", Lore.phaseChange(1)));
    public final SlimefunItemStack PHASE_CRYSTAL_5 = enchant(new SlimefunItemStack(
            "QP_PHASE_CRYSTAL_5", Material.QUARTZ, "&7Phase Crystal", Lore.phaseChange(5)));
    public final SlimefunItemStack PHASE_CRYSTAL_15 = enchant(new SlimefunItemStack(
            "QP_PHASE_CRYSTAL_15", Material.QUARTZ, "&7Phase Crystal", Lore.phaseChange(15)));
    public final SlimefunItemStack PHASE_CRYSTAL_45 = enchant(new SlimefunItemStack(
            "QP_PHASE_CRYSTAL_45", Material.QUARTZ, "&7Phase Crystal", Lore.phaseChange(45)));
    public final SlimefunItemStack PHASE_CRYSTAL_90 = enchant(new SlimefunItemStack(
            "QP_PHASE_CRYSTAL_90", Material.QUARTZ, "&7Phase Crystal", Lore.phaseChange(90)));
    public final SlimefunItemStack PHASE_CRYSTAL_180 = enchant(new SlimefunItemStack(
            "QP_PHASE_CRYSTAL_180", Material.QUARTZ, "&7Phase Crystal", Lore.phaseChange(180)));

    private @NotNull SlimefunItemStack enchant(final @NotNull SlimefunItemStack itemStack) {
        itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return itemStack;
    }

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSMITTER_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.REDSTONE), null,
                new ItemStack(Material.REDSTONE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.REDSTONE),
                null, new ItemStack(Material.REDSTONE), null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSMITTER_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.REDSTONE_BLOCK), null,
                new ItemStack(Material.REDSTONE_BLOCK), TRANSMITTER_1, new ItemStack(Material.REDSTONE_BLOCK),
                null, new ItemStack(Material.REDSTONE_BLOCK), null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSMITTER_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, CORINTHIAN_BRONZE_INGOT, null,
                CORINTHIAN_BRONZE_INGOT, TRANSMITTER_2, CORINTHIAN_BRONZE_INGOT,
                null, CORINTHIAN_BRONZE_INGOT, null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSMITTER_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, REDSTONE_ALLOY, null,
                REDSTONE_ALLOY, TRANSMITTER_3, REDSTONE_ALLOY,
                null, REDSTONE_ALLOY, null
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, RECEIVER_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.COAL), null,
                new ItemStack(Material.COAL), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.COAL),
                null, new ItemStack(Material.COAL), null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, RECEIVER_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.COAL_BLOCK), null,
                new ItemStack(Material.COAL_BLOCK), RECEIVER_1, new ItemStack(Material.COAL_BLOCK),
                null, new ItemStack(Material.COAL_BLOCK), null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, RECEIVER_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, CORINTHIAN_BRONZE_INGOT, null,
                CORINTHIAN_BRONZE_INGOT, RECEIVER_2, CORINTHIAN_BRONZE_INGOT,
                null, CORINTHIAN_BRONZE_INGOT, null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, RECEIVER_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, REDSTONE_ALLOY, null,
                REDSTONE_ALLOY, RECEIVER_3, REDSTONE_ALLOY,
                null, REDSTONE_ALLOY, null
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSCEIVER_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.COPPER_INGOT), null,
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE),
                null, new ItemStack(Material.COPPER_INGOT), null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSCEIVER_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, new ItemStack(Material.IRON_INGOT), null,
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE),
                null, new ItemStack(Material.IRON_INGOT), null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSCEIVER_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, DAMASCUS_STEEL_INGOT, null,
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE),
                null, DAMASCUS_STEEL_INGOT, null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSCEIVER_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, REINFORCED_ALLOY_INGOT, null,
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE),
                null, REINFORCED_ALLOY_INGOT, null
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, DIELECTRIC_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, ZINC_DUST, null,
                ZINC_DUST, new ItemStack(Material.QUARTZ_BLOCK), ZINC_DUST,
                null, ZINC_DUST, null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, DIELECTRIC_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                DIELECTRIC_1, SYNTHETIC_EMERALD, DIELECTRIC_1,
                null, null, null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, DIELECTRIC_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                DIELECTRIC_2, URANIUM, DIELECTRIC_2,
                null, null, null
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, DIELECTRIC_4, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                DIELECTRIC_3, BLISTERING_INGOT_3, DIELECTRIC_3,
                null, null, null
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSFORMER_COIL_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA),
                new ItemStack(Material.TERRACOTTA), GOLD_24K, new ItemStack(Material.TERRACOTTA),
                new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA), new ItemStack(Material.TERRACOTTA)
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSFORMER_COIL_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.TERRACOTTA), TRANSFORMER_COIL_1, new ItemStack(Material.TERRACOTTA),
                new ItemStack(Material.TERRACOTTA), GOLD_24K_BLOCK, new ItemStack(Material.TERRACOTTA),
                new ItemStack(Material.TERRACOTTA), TRANSFORMER_COIL_1, new ItemStack(Material.TERRACOTTA)
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TRANSFORMER_COIL_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.TERRACOTTA), TRANSFORMER_COIL_2, new ItemStack(Material.TERRACOTTA),
                new ItemStack(Material.TERRACOTTA), CARBONADO, new ItemStack(Material.TERRACOTTA),
                new ItemStack(Material.TERRACOTTA), TRANSFORMER_COIL_2, new ItemStack(Material.TERRACOTTA)
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, ENERGY_CONCENTRATION_ELEMENT_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE),
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.GLASS_PANE),
                new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE), new ItemStack(Material.GLASS_PANE)
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, ENERGY_CONCENTRATION_ELEMENT_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                SYNTHETIC_SAPPHIRE, SYNTHETIC_SAPPHIRE, SYNTHETIC_SAPPHIRE,
                ENERGY_CONCENTRATION_ELEMENT_1, new ItemStack(Material.DIAMOND_BLOCK), ENERGY_CONCENTRATION_ELEMENT_1,
                SYNTHETIC_SAPPHIRE, SYNTHETIC_SAPPHIRE, SYNTHETIC_SAPPHIRE
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, ENERGY_CONCENTRATION_ELEMENT_3, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                HARDENED_METAL_INGOT, HARDENED_METAL_INGOT, HARDENED_METAL_INGOT,
                ENERGY_CONCENTRATION_ELEMENT_2, POWER_CRYSTAL, ENERGY_CONCENTRATION_ELEMENT_2,
                HARDENED_METAL_INGOT, HARDENED_METAL_INGOT, HARDENED_METAL_INGOT
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TURRET_BASE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                null, null, null,
                new ItemStack(Material.IRON_INGOT), new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.IRON_INGOT)
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TURRET_BARREL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                ALUMINUM_INGOT, ALUMINUM_INGOT, ALUMINUM_INGOT,
                null, null, null,
        }).register(addon);

        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, TURRET_TARGETING_MECHANISM, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                null, null, null,
                new ItemStack(Material.GLASS), TRANSCEIVER_1, new ItemStack(Material.GLASS),
                SYNTHETIC_SAPPHIRE, BASIC_CIRCUIT_BOARD, SYNTHETIC_SAPPHIRE,
        }).register(addon);



        new UnplaceableBlock(Groups.CRAFTING_COMPONENTS, CARBON_STRUCTURE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK),
                new ItemStack(Material.COAL_BLOCK), REDSTONE_ALLOY, new ItemStack(Material.COAL_BLOCK),
                new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.COAL_BLOCK),
        }).register(addon);



        new SlimefunItem(Groups.CRAFTING_COMPONENTS, BLANK_MODULE_CARD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.COPPER_BLOCK), TRANSCEIVER_3, new ItemStack(Material.COPPER_BLOCK),
                TRANSCEIVER_3, ENERGY_CONCENTRATION_ELEMENT_3, TRANSCEIVER_3,
                new ItemStack(Material.COPPER_BLOCK), TRANSCEIVER_3, new ItemStack(Material.COPPER_BLOCK)
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, INFUSED_MODULE_CARD, RecipeTypes.RECIPE_INFUSION, new ItemStack[]{
                null, null, null,
                null, BLANK_MODULE_CARD, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, ENTANGLED_MODULE_CARD, RecipeTypes.RECIPE_INFUSION, new ItemStack[]{
                null, null, null,
                null, INFUSED_MODULE_CARD, null,
                null, null, null
        }).register(addon);



        new SlimefunItem(Groups.CRAFTING_COMPONENTS, INFUSED_FREQUENCY_CRYSTAL, RecipeTypes.RECIPE_INFUSION, new ItemStack[]{
                null, null, null,
                null, new ItemStack(Material.QUARTZ), null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, ENTANGLED_FREQUENCY_CRYSTAL, RecipeTypes.RECIPE_ENTANGLEMENT, new ItemStack[]{
                null, null, null,
                null, INFUSED_FREQUENCY_CRYSTAL, null,
                null, null, null
        }).register(addon);



        new SlimefunItem(Groups.CRAFTING_COMPONENTS, PHASE_CRYSTAL_1, RecipeTypes.RECIPE_REFINING, new ItemStack[]{
                null, null, null,
                null, new ItemStack(Material.QUARTZ), null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, PHASE_CRYSTAL_5, RecipeTypes.RECIPE_REFINING, new ItemStack[]{
                null, null, null,
                null, PHASE_CRYSTAL_1, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, PHASE_CRYSTAL_15, RecipeTypes.RECIPE_REFINING, new ItemStack[]{
                null, null, null,
                null, PHASE_CRYSTAL_5, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, PHASE_CRYSTAL_45, RecipeTypes.RECIPE_REFINING, new ItemStack[]{
                null, null, null,
                null, PHASE_CRYSTAL_15, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, PHASE_CRYSTAL_90, RecipeTypes.RECIPE_REFINING, new ItemStack[]{
                null, null, null,
                null, PHASE_CRYSTAL_45, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.CRAFTING_COMPONENTS, PHASE_CRYSTAL_180, RecipeTypes.RECIPE_REFINING, new ItemStack[]{
                null, null, null,
                null, PHASE_CRYSTAL_90, null,
                null, null, null
        }).register(addon);
    }
}
