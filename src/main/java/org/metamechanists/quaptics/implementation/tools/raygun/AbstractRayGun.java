package org.metamechanists.quaptics.implementation.tools.raygun;

import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.metamechanists.metalib.utils.LocationUtils;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;

public abstract class AbstractRayGun extends QuapticChargeableItem {
    protected AbstractRayGun(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    public void onUseItem(PlayerRightClickEvent event) {
        final ItemStack itemStack = event.getItem();
        final double charge = getCharge(itemStack);

        // Don't have enough charge to use
        if (charge < settings.getEmissionPower()) {
            return;
        }

        final Player player = event.getPlayer();
        final boolean leftHand =
                (player.getMainHand() == MainHand.LEFT && event.getHand() == EquipmentSlot.HAND)
                        || (player.getMainHand() == MainHand.RIGHT && event.getHand() == EquipmentSlot.OFF_HAND);

        final Location eyeLocation = player.getEyeLocation();
        final Location source = LocationUtils.getHandLocation(player, leftHand);
        final Location target = eyeLocation.add(eyeLocation.getDirection().multiply(64));

        fireRayGun(player, source, target);
        setCharge(itemStack, stepCharge(settings, charge, -settings.getEmissionPower()));
        updateLore(itemStack);
    }

    public abstract void fireRayGun(Player player, Location source, Location target);
}
