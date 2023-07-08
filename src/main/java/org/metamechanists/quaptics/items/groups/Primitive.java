package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper;
import org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector;
import org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad;
import org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionContainer;
import org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionPillar;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.RecipeTypes;
import org.metamechanists.quaptics.items.Tier;

import java.util.Set;

@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Primitive {
    public final Settings SOLAR_CONCENTRATOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.45F)
            .connectionRadius(0.45F)
            .emissionPower(1)
            .build();

    public final Settings LENS_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.24F)
            .connectionRadius(0.48F)
            .powerLoss(0.1)
            .build();

    public final Settings COMBINER_1_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();

    public final Settings SPLITTER_1_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.8F)
            .powerLoss(0.2)
            .connections(2)
            .build();

    public final Settings CAPACITOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.3F)
            .connectionRadius(0.6F)
            .chargeCapacity(200)
            .emissionPower(3)
            .powerLoss(0.25)
            .build();

    public final Settings CHARGER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.4F)
            .connectionRadius(0.6F)
            .build();

    public final Settings TURRET_1_HOSTILE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.55F)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(3)
            .targets(Set.of(SpawnCategory.MONSTER))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();

    public final Settings TURRET_1_PASSIVE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .displayRadius(0.55F)
            .connectionRadius(0.55F)
            .minPower(5)
            .range(5)
            .damage(1)
            .projectileSpeed(3)
            .targets(Set.of(SpawnCategory.WATER_UNDERGROUND_CREATURE, SpawnCategory.AMBIENT, SpawnCategory.ANIMAL, SpawnCategory.AXOLOTL,
                    SpawnCategory.WATER_AMBIENT, SpawnCategory.WATER_ANIMAL))
            .projectileMaterial(Material.LIGHT_BLUE_CONCRETE)
            .mainMaterial(Material.POLISHED_ANDESITE)
            .build();

    public final Settings MULTIBLOCK_CLICKER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.35F)
            .useInterval(10)
            .minPower(7)
            .build();

    public final Settings DATA_STRIPPER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.4F)
            .timePerItem(10)
            .minPower(6)
            .build();

    public final Settings LAUNCHPAD_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.8F)
            .minPower(3)
            .build();

    public final Settings ITEM_PROJECTOR_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.6F)
            .build();

    public final Settings INFUSION_CONTAINER_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .timePerItem(10)
            .build();

    public final Settings INFUSION_PILLAR_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(7)
            .connectionRadius(0.3F)
            .build();

    public final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &bI",
            Lore.create(SOLAR_CONCENTRATOR_1_SETTINGS,
                    "&7● Only works during the day",
                    "&7● Concentrates sunlight into a quaptic ray"));

    public final SlimefunItemStack LENS_1 = new SlimefunItemStack(
            "QP_LENS_1",
            Material.GLASS,
            "&9Lens &bI",
            Lore.create(LENS_1_SETTINGS,
                    "&7● Redirects a quaptic ray"));

    public final SlimefunItemStack COMBINER_1_2 = new SlimefunItemStack(
            "QP_COMBINER_1_2",
            Material.GRAY_STAINED_GLASS,
            "&9Combiner &eI &8(2 connections)",
            Lore.create(COMBINER_1_2_SETTINGS,
                    "&7● Combines multiple quaptic rays into one"));

    public final SlimefunItemStack SPLITTER_1_2 = new SlimefunItemStack(
            "QP_SPLITTER_1_2",
            Material.LIGHT_GRAY_STAINED_GLASS,
            "&9Splitter &eI &8(2 connections)",
            Lore.create(SPLITTER_1_2_SETTINGS,
                    "&7● Splits one quaptic ray into multiple"));

    public final SlimefunItemStack CHARGER_1 = new SlimefunItemStack(
            "QP_CHARGER_1",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&bCharger &3I",
            Lore.create(CHARGER_1_SETTINGS,
                    "&7● Charges item with Quaptic Energy Units",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    public final SlimefunItemStack CAPACITOR_1 = new SlimefunItemStack(
            "QP_CAPACITOR_1",
            Material.LIGHT_BLUE_CONCRETE,
            "&3Capacitor &bI",
            Lore.create(CAPACITOR_1_SETTINGS,
                    "&7● Stores charge",
                    "&7● Outputs at a constant power"));

    public final SlimefunItemStack TURRET_1_HOSTILE = new SlimefunItemStack(
            "QP_TURRET_1_HOSTILE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eI &8(targets hostiles)",
            Lore.create(TURRET_1_HOSTILE_SETTINGS,
                    "&7● Modulated projectiles",
                    "&7● Shoots at nearby entities"));

    public final SlimefunItemStack TURRET_1_PASSIVE = new SlimefunItemStack(
            "QP_TURRET_1_PASSIVE",
            Material.SMOOTH_STONE_SLAB,
            "&6Turret &eII &8(targets passives)",
            Lore.create(TURRET_1_PASSIVE_SETTINGS,
                    "&7● Modulated projectiles",
                    "&7● Shoots at nearby entities"));

    public final SlimefunItemStack MULTIBLOCK_CLICKER_1 = new SlimefunItemStack(
            "QP_MULTIBLOCK_CLICKER_1",
            Material.DISPENSER,
            "&6Multiblock Clicker &eI",
            Lore.create(MULTIBLOCK_CLICKER_1_SETTINGS,
                    "&7● &eRight Click &7to enable/disable",
                    "&7● Automatically clicks the attached multiblock",
                    "&7● Place facing the block you'd usually click to use the multiblock"));

    public final SlimefunItemStack DATA_STRIPPER_1 = new SlimefunItemStack(
            "QP_DATA_STRIPPER_1",
            Material.ORANGE_STAINED_GLASS,
            "&6Data Stripper &eI",
            Lore.create(DATA_STRIPPER_1_SETTINGS,
                    "&7● Converts Slimefun heads into placeable vanilla heads",
                    "&7● &eRight Click &7with an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    public final SlimefunItemStack LAUNCHPAD = new SlimefunItemStack(
            "QP_LAUNCHPAD",
            Material.LIGHT_GRAY_CONCRETE,
            "&6Launchpad",
            Lore.create(LAUNCHPAD_SETTINGS,
                    "&7● Launches players",
                    "&7● Launch velocity can be configured",
                    "&7● &eWalk onto the launchpad &7to get launched"));

    public final SlimefunItemStack ITEM_PROJECTOR = new SlimefunItemStack(
            "QP_ITEM_PROJECTOR",
            Material.LIGHT_BLUE_STAINED_GLASS,
            "&6Item Projector",
            Lore.create(ITEM_PROJECTOR_SETTINGS,
                    "&7● Displays a hologram of an inserted item",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    public final SlimefunItemStack INFUSION_CONTAINER = new SlimefunItemStack(
            "QP_INFUSION_CONTAINER",
            Material.GRAY_CONCRETE,
            "&6Infusion Container",
            Lore.create(ITEM_PROJECTOR_SETTINGS,
                    "&c● Explodes if any pillar loses power during an infusion",
                    "&7● Multiblock structure: use the Multiblock Wand to build the structure",
                    "&7● Infuses items",
                    "&7● &eRight Click &7with an item to start infusing"));

    public final SlimefunItemStack INFUSION_PILLAR = new SlimefunItemStack(
            "QP_INFUSION_PILLAR",
            Material.BLUE_CONCRETE,
            "&6Infusion Pillar",
            Lore.create(INFUSION_PILLAR_SETTINGS,
                    "&7● Multiblock component"));

    public final SlimefunItemStack INFUSED_DEAD_BUSH = new SlimefunItemStack(
            "QP_INFUSED_DEAD_BUSH",
            getInfusedDeadBush(),
            "&cInfused Deadbush");

    private @NotNull ItemStack getInfusedDeadBush() {
        final ItemStack itemStack = new ItemStack(Material.DEAD_BUSH);
        itemStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        itemStack.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return itemStack;
    }

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SolarConcentrator(
                Groups.PRIMITIVE,
                SOLAR_CONCENTRATOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                SOLAR_CONCENTRATOR_1_SETTINGS).register(addon);

        new Lens(
                Groups.PRIMITIVE,
                LENS_1,
                RecipeType.NULL,
                new ItemStack[]{},
                LENS_1_SETTINGS).register(addon);

        new Combiner(
                Groups.PRIMITIVE,
                COMBINER_1_2,
                RecipeType.NULL,
                new ItemStack[]{},
                COMBINER_1_2_SETTINGS).register(addon);

        new Splitter(
                Groups.PRIMITIVE,
                SPLITTER_1_2,
                RecipeType.NULL,
                new ItemStack[]{},
                SPLITTER_1_2_SETTINGS).register(addon);

        new Charger(
                Groups.PRIMITIVE,
                CHARGER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                CHARGER_1_SETTINGS).register(addon);

        new Capacitor(
                Groups.PRIMITIVE,
                CAPACITOR_1,
                RecipeType.NULL,
                new ItemStack[]{},
                CAPACITOR_1_SETTINGS).register(addon);

        new ModulatedTurret(
                Groups.PRIMITIVE,
                TURRET_1_HOSTILE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_1_HOSTILE_SETTINGS).register(addon);

        new ModulatedTurret(
                Groups.PRIMITIVE,
                TURRET_1_PASSIVE,
                RecipeType.NULL,
                new ItemStack[]{},
                TURRET_1_PASSIVE_SETTINGS).register(addon);

        new MultiblockClicker(
                Groups.PRIMITIVE,
                MULTIBLOCK_CLICKER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                MULTIBLOCK_CLICKER_1_SETTINGS).register(addon);

        new DataStripper(
                Groups.PRIMITIVE,
                DATA_STRIPPER_1,
                RecipeType.NULL,
                new ItemStack[]{},
                DATA_STRIPPER_1_SETTINGS).register(addon);

        new Launchpad(
                Groups.PRIMITIVE,
                LAUNCHPAD,
                RecipeType.NULL,
                new ItemStack[]{},
                LAUNCHPAD_SETTINGS).register(addon);

        new ItemProjector(
                Groups.PRIMITIVE,
                ITEM_PROJECTOR,
                RecipeType.NULL,
                new ItemStack[]{},
                ITEM_PROJECTOR_SETTINGS).register(addon);

        new InfusionContainer(
                Groups.PRIMITIVE,
                INFUSION_CONTAINER,
                RecipeType.NULL,
                new ItemStack[]{},
                INFUSION_CONTAINER_SETTINGS).register(addon);

        new InfusionPillar(
                Groups.PRIMITIVE,
                INFUSION_PILLAR,
                RecipeType.NULL,
                new ItemStack[]{},
                INFUSION_PILLAR_SETTINGS).register(addon);

        new SlimefunItem(
                Groups.PRIMITIVE,
                INFUSED_DEAD_BUSH,
                RecipeTypes.RECIPE_INFUSION,
                new ItemStack[]{
                        null, null, null,
                        null, new ItemStack(Material.DEAD_BUSH), null,
                        null, null, null
                }).register(addon);
    }
}
