package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.FireResistanceModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.HungerRefillModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.InvincibilityModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.LuckModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.WirelessChargerModule;
import org.metamechanists.quaptics.items.Groups;

import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.BILLON_INGOT;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.FLASK_OF_KNOWLEDGE;
import static io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems.REINFORCED_ALLOY_INGOT;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_4;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceModule.EXPERIENCE_MODULE_1;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceModule.EXPERIENCE_MODULE_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceModule.EXPERIENCE_MODULE_2;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceModule.EXPERIENCE_MODULE_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.FireResistanceModule.FIRE_RESISTANCE_MODULE_1;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.FireResistanceModule.FIRE_RESISTANCE_MODULE_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.FireResistanceModule.FIRE_RESISTANCE_MODULE_2;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.FireResistanceModule.FIRE_RESISTANCE_MODULE_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.HungerRefillModule.HUNGER_REFILL_MODULE;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.HungerRefillModule.HUNGER_REFILL_MODULE_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.InvincibilityModule.INVINCIBILITY_MODULE;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.InvincibilityModule.INVINCIBILITY_MODULE_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.LuckModule.LUCK_MODULE_1;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.LuckModule.LUCK_MODULE_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.LuckModule.LUCK_MODULE_2;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.LuckModule.LUCK_MODULE_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.WirelessChargerModule.WIRELESS_CHARGER_MODULE;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.WirelessChargerModule.WIRELESS_CHARGER_MODULE_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.reactor.ReactorController.REACTOR_CONTROLLER;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.*;


@UtilityClass
public class BeaconModules {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new LuckModule(Groups.BEACON_MODULES, LUCK_MODULE_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.LIME_DYE), BILLON_INGOT, new ItemStack(Material.LIME_DYE),
                BILLON_INGOT, INFUSED_MODULE_CARD, BILLON_INGOT,
                new ItemStack(Material.LIME_DYE), BILLON_INGOT, new ItemStack(Material.LIME_DYE)
        }, LUCK_MODULE_1_SETTINGS).register(addon);

        new FireResistanceModule(Groups.BEACON_MODULES, FIRE_RESISTANCE_MODULE_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.BLAZE_ROD), BILLON_INGOT, new ItemStack(Material.BLAZE_ROD),
                BILLON_INGOT, INFUSED_MODULE_CARD, BILLON_INGOT,
                new ItemStack(Material.BLAZE_ROD), BILLON_INGOT, new ItemStack(Material.BLAZE_ROD)
        }, FIRE_RESISTANCE_MODULE_1_SETTINGS).register(addon);

        new HungerRefillModule(Groups.BEACON_MODULES, HUNGER_REFILL_MODULE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.HAY_BLOCK), BILLON_INGOT, new ItemStack(Material.HAY_BLOCK),
                BILLON_INGOT, INFUSED_MODULE_CARD, BILLON_INGOT,
                new ItemStack(Material.HAY_BLOCK), BILLON_INGOT, new ItemStack(Material.HAY_BLOCK)
        }, HUNGER_REFILL_MODULE_SETTINGS).register(addon);

        new ExperienceModule(Groups.BEACON_MODULES, EXPERIENCE_MODULE_1, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                FLASK_OF_KNOWLEDGE, BILLON_INGOT, FLASK_OF_KNOWLEDGE,
                BILLON_INGOT, INFUSED_MODULE_CARD, BILLON_INGOT,
                FLASK_OF_KNOWLEDGE, BILLON_INGOT, FLASK_OF_KNOWLEDGE
        }, EXPERIENCE_MODULE_1_SETTINGS).register(addon);



        new LuckModule(Groups.BEACON_MODULES, LUCK_MODULE_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.LIME_DYE), ENTANGLED_MODULE_CARD, new ItemStack(Material.LIME_DYE),
                BILLON_INGOT, LUCK_MODULE_1, BILLON_INGOT,
                new ItemStack(Material.LIME_DYE), ENTANGLED_MODULE_CARD, new ItemStack(Material.LIME_DYE)
        }, LUCK_MODULE_2_SETTINGS).register(addon);

        new FireResistanceModule(Groups.BEACON_MODULES, FIRE_RESISTANCE_MODULE_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                new ItemStack(Material.BLAZE_ROD), ENTANGLED_MODULE_CARD, new ItemStack(Material.BLAZE_ROD),
                BILLON_INGOT, FIRE_RESISTANCE_MODULE_1, BILLON_INGOT,
                new ItemStack(Material.BLAZE_ROD), ENTANGLED_MODULE_CARD, new ItemStack(Material.BLAZE_ROD)
        }, FIRE_RESISTANCE_MODULE_2_SETTINGS).register(addon);

        new ExperienceModule(Groups.BEACON_MODULES, EXPERIENCE_MODULE_2, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                FLASK_OF_KNOWLEDGE, ENTANGLED_MODULE_CARD, FLASK_OF_KNOWLEDGE,
                BILLON_INGOT, EXPERIENCE_MODULE_1, BILLON_INGOT,
                FLASK_OF_KNOWLEDGE, ENTANGLED_MODULE_CARD, FLASK_OF_KNOWLEDGE
        }, EXPERIENCE_MODULE_2_SETTINGS).register(addon);

        new WirelessChargerModule(Groups.BEACON_MODULES, WIRELESS_CHARGER_MODULE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                REINFORCED_ALLOY_INGOT, ENTANGLED_MODULE_CARD, REINFORCED_ALLOY_INGOT,
                BILLON_INGOT, CHARGER_4, BILLON_INGOT,
                REINFORCED_ALLOY_INGOT, ENTANGLED_MODULE_CARD, REINFORCED_ALLOY_INGOT
        }, WIRELESS_CHARGER_MODULE_SETTINGS).register(addon);

        new InvincibilityModule(Groups.BEACON_MODULES, INVINCIBILITY_MODULE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[]{
                TRANSFORMER_COIL_3, ENTANGLED_MODULE_CARD, TRANSFORMER_COIL_3,
                ENTANGLED_MODULE_CARD, REACTOR_CONTROLLER, ENTANGLED_MODULE_CARD,
                TRANSFORMER_COIL_3, ENTANGLED_MODULE_CARD, TRANSFORMER_COIL_3
        }, INVINCIBILITY_MODULE_SETTINGS).register(addon);
    }
}
