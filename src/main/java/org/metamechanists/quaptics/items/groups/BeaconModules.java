package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.FireResistanceModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.HungerRefillModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.InvincibilityModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.LuckModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.WirelessChargerModule;
import org.metamechanists.quaptics.items.Groups;

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


@UtilityClass
public class BeaconModules {
    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new LuckModule(Groups.BEACON_MODULES, LUCK_MODULE_1, RecipeType.NULL, new ItemStack[]{

        }, LUCK_MODULE_1_SETTINGS).register(addon);

        new FireResistanceModule(Groups.BEACON_MODULES, FIRE_RESISTANCE_MODULE_1, RecipeType.NULL, new ItemStack[]{

        }, FIRE_RESISTANCE_MODULE_1_SETTINGS).register(addon);

        new HungerRefillModule(Groups.BEACON_MODULES, HUNGER_REFILL_MODULE, RecipeType.NULL, new ItemStack[]{

        }, HUNGER_REFILL_MODULE_SETTINGS).register(addon);

        new ExperienceModule(Groups.BEACON_MODULES, EXPERIENCE_MODULE_1, RecipeType.NULL, new ItemStack[]{

        }, EXPERIENCE_MODULE_1_SETTINGS).register(addon);



        new LuckModule(Groups.BEACON_MODULES, LUCK_MODULE_2, RecipeType.NULL, new ItemStack[]{

        }, LUCK_MODULE_2_SETTINGS).register(addon);

        new FireResistanceModule(Groups.BEACON_MODULES, FIRE_RESISTANCE_MODULE_2, RecipeType.NULL, new ItemStack[]{

        }, FIRE_RESISTANCE_MODULE_2_SETTINGS).register(addon);

        new ExperienceModule(Groups.BEACON_MODULES, EXPERIENCE_MODULE_2, RecipeType.NULL, new ItemStack[]{

        }, EXPERIENCE_MODULE_2_SETTINGS).register(addon);

        new WirelessChargerModule(Groups.BEACON_MODULES, WIRELESS_CHARGER_MODULE, RecipeType.NULL, new ItemStack[]{

        }, WIRELESS_CHARGER_MODULE_SETTINGS).register(addon);

        new InvincibilityModule(Groups.BEACON_MODULES, INVINCIBILITY_MODULE, RecipeType.NULL, new ItemStack[]{

        }, INVINCIBILITY_MODULE_SETTINGS).register(addon);
    }
}
