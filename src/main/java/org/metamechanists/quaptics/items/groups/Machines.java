package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper;
import org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker;
import org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret;
import org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret;
import org.metamechanists.quaptics.items.Groups;

import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_2;
import static org.metamechanists.quaptics.implementation.blocks.consumers.DataStripper.DATA_STRIPPER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_1;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_1_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_2;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_2_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_3;
import static org.metamechanists.quaptics.implementation.blocks.consumers.MultiblockClicker.MULTIBLOCK_CLICKER_3_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad.LAUNCHPAD;
import static org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.Launchpad.LAUNCHPAD_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_3_HOSTILE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_3_HOSTILE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_3_PASSIVE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_3_PASSIVE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_4_HOSTILE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_4_HOSTILE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_4_PASSIVE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.DirectTurret.TURRET_4_PASSIVE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_HOSTILE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_HOSTILE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_PASSIVE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_1_PASSIVE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_2_HOSTILE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_2_HOSTILE_SETTINGS;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_2_PASSIVE;
import static org.metamechanists.quaptics.implementation.blocks.consumers.turrets.ModulatedTurret.TURRET_2_PASSIVE_SETTINGS;


@UtilityClass
public class Machines {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new ModulatedTurret(Groups.MACHINES, TURRET_1_HOSTILE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_1_HOSTILE_SETTINGS).register(addon);

        new ModulatedTurret(Groups.MACHINES, TURRET_1_PASSIVE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_1_PASSIVE_SETTINGS).register(addon);

        new ModulatedTurret(Groups.MACHINES, TURRET_2_HOSTILE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_2_HOSTILE_SETTINGS).register(addon);

        new ModulatedTurret(Groups.MACHINES, TURRET_2_PASSIVE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_2_PASSIVE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_3_HOSTILE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_3_HOSTILE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_3_PASSIVE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_3_PASSIVE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_4_HOSTILE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_4_HOSTILE_SETTINGS).register(addon);

        new DirectTurret(Groups.MACHINES, TURRET_4_PASSIVE, RecipeType.NULL, new ItemStack[]{

        }, TURRET_4_PASSIVE_SETTINGS).register(addon);



        new MultiblockClicker(Groups.MACHINES, MULTIBLOCK_CLICKER_1, RecipeType.NULL, new ItemStack[]{

        }, MULTIBLOCK_CLICKER_1_SETTINGS).register(addon);

        new MultiblockClicker(Groups.MACHINES, MULTIBLOCK_CLICKER_2, RecipeType.NULL, new ItemStack[]{

        }, MULTIBLOCK_CLICKER_2_SETTINGS).register(addon);

        new MultiblockClicker(Groups.MACHINES, MULTIBLOCK_CLICKER_3, RecipeType.NULL, new ItemStack[]{

        }, MULTIBLOCK_CLICKER_3_SETTINGS).register(addon);



        new Launchpad(Groups.MACHINES, LAUNCHPAD, RecipeType.NULL, new ItemStack[]{

        }, LAUNCHPAD_SETTINGS).register(addon);



        new DataStripper(Groups.MACHINES, DATA_STRIPPER_1, RecipeType.NULL, new ItemStack[]{

        }, DATA_STRIPPER_1_SETTINGS).register(addon);

        new DataStripper(Groups.MACHINES, DATA_STRIPPER_2, RecipeType.NULL, new ItemStack[]{

        }, DATA_STRIPPER_2_SETTINGS).register(addon);
    }
}
