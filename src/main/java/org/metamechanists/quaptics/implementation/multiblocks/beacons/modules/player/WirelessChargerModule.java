package org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.BeaconModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.PlayerModule;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;

import java.util.Collection;


public class WirelessChargerModule extends BeaconModule implements PlayerModule {
    public static final Settings WIRELESS_CHARGER_MODULE_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .operatingPowerHidden(true)
            .powerEfficiency(0.1)
            .build();

    public static final SlimefunItemStack WIRELESS_CHARGER_MODULE = getBanner(new SlimefunItemStack(
            "QP_WIRELESS_CHARGER_MODULE",
            Material.ORANGE_BANNER,
            Colors.BEACONS.getFormattedColor() + "Wireless Charger Module",
            Lore.create(WIRELESS_CHARGER_MODULE_SETTINGS,
                    "&7● Wirelessly charges held items",
                    "&7● The more power the beacon has, the higher the charge rate")));

    public WirelessChargerModule(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    private static @NotNull SlimefunItemStack getBanner(final @NotNull SlimefunItemStack stack) {
        final BannerMeta meta = (BannerMeta) stack.getItemMeta();
        meta.addPattern(new Pattern(DyeColor.LIGHT_BLUE, PatternType.CIRCLE_MIDDLE));
        meta.addPattern(new Pattern(DyeColor.CYAN, PatternType.FLOWER));
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void apply(final @NotNull BeaconController controller, final @NotNull Location controllerLocation, final @NotNull Collection<Player> players) {
        final double chargeRate = BlockStorageAPI.getDouble(controllerLocation.clone().add(controller.getPowerSupplyLocation()), Keys.BS_INPUT_POWER);
        players.forEach(player -> {
            final ItemStack mainHandItem = player.getInventory().getItem(EquipmentSlot.HAND);
            final ItemStack offHandItem = player.getInventory().getItem(EquipmentSlot.OFF_HAND);

            if (SlimefunItem.getByItem(mainHandItem) instanceof final QuapticChargeableItem chargeableItem) {
                chargeableItem.chargeItem(chargeRate, mainHandItem, QuapticTicker.INTERVAL_TICKS_22);
                QuapticChargeableItem.updateLore(mainHandItem);
            }

            if (SlimefunItem.getByItem(offHandItem) instanceof final QuapticChargeableItem chargeableItem) {
                chargeableItem.chargeItem(chargeRate, offHandItem, QuapticTicker.INTERVAL_TICKS_22);
                QuapticChargeableItem.updateLore(offHandItem);
            }
        });
    }
}
