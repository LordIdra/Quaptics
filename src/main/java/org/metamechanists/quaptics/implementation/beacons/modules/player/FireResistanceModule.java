package org.metamechanists.quaptics.implementation.beacons.modules.player;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.beacons.modules.BeaconModule;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;

import java.util.Collection;


public class FireResistanceModule extends BeaconModule implements PlayerModule {
    public static final Settings FIRE_RESISTANCE_MODULE_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .fireResistanceLevel(1)
            .build();
    public static final SlimefunItemStack FIRE_RESISTANCE_MODULE = getBanner(new SlimefunItemStack(
            "QP_FIRE_RESISTANCE_MODULE",
            Material.GRAY_BANNER,
            "&6Fire Resistance Module",
            Lore.create(FIRE_RESISTANCE_MODULE_SETTINGS,
                    "&7● Gives fire resistance to all players in range")));

    public FireResistanceModule(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    private static @NotNull SlimefunItemStack getBanner(final @NotNull SlimefunItemStack stack) {
        final BannerMeta meta = (BannerMeta) stack.getItemMeta();
        meta.addPattern(new Pattern(DyeColor.ORANGE, PatternType.FLOWER));
        meta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
        stack.setItemMeta(meta);
        return stack;
    }

    @Override
    public void apply(final @NotNull Collection<Player> players) {
        players.forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, QuapticTicker.INTERVAL_TICKS_22 + 20, settings.getFireResistanceLevel()-1)));
    }
}
