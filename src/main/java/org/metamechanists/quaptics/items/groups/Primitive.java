package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper;
import org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector;
import org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker;
import org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Lens;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter;
import org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionContainer;
import org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionPillar;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.RecipeTypes;

import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_1;
import static org.metamechanists.quaptics.implementation.blocks.concentrators.SolarConcentrator.SOLAR_CONCENTRATOR_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.Charger.CHARGER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector.ITEM_PROJECTOR;
import static org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector.ITEM_PROJECTOR_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad.LAUNCHPAD;
import static org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad.LAUNCHPAD_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_HOSTILE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_HOSTILE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_PASSIVE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_PASSIVE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor.CAPACITOR_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_1_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Combiner.COMBINER_1_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_1;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Lens.LENS_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_1_2;
import static org.metamechanists.quaptics.implementation.blocks.manipulators.Splitter.SPLITTER_1_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionContainer.INFUSION_CONTAINER;
import static org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionContainer.INFUSION_CONTAINER_SETTINGS;
import static org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionPillar.INFUSION_PILLAR;
import static org.metamechanists.quaptics.implementation.multiblocks.infuser.InfusionPillar.INFUSION_PILLAR_SETTINGS;


@SuppressWarnings({"ZeroLengthArrayAllocation", "WeakerAccess"})
@UtilityClass
public class Primitive {
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
