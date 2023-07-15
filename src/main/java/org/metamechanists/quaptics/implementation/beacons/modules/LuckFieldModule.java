package org.metamechanists.quaptics.implementation.beacons.modules;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.beacons.modules.types.PlayerModule;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;

import java.util.Collection;


public class LuckFieldModule extends BeaconModule implements PlayerModule {
    public static final Settings LUCK_FIELD_MODULE_SETTINGS = Settings.builder()
            .tier(Tier.TESTING)
            .build();
    public static final SlimefunItemStack LUCK_FIELD_MODULE = new SlimefunItemStack(
            "QP_LUCK_FIELD_MODULE",
            Material.BLUE_BANNER,
            "&6ALuck Field Module",
            Lore.create(LUCK_FIELD_MODULE_SETTINGS,
                    "&7● Increases the luck of all players in range"));

    public LuckFieldModule(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    public void apply(final @NotNull Collection<Player> players) {
        players.forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 2, 1)));
    }
}
