package org.metamechanists.quaptics.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LaserPointer extends QuapticChargeableItem implements NotPlaceable {
    public LaserPointer(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ConnectedBlock.Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
        addItemHandler(onUse());
    }

    public ItemUseHandler onUse() {
        return event -> {
            final Player player = event.getPlayer();
            if (player.isSneaking()) {
                changeColor(player);
                return;
            }

            togglePointer(player);
        };
    }

    public static void changeColor(Player player) {
        final LaserPoint point = LaserPointerManager.getPoint(player.getUniqueId());
        if (point == null) {
            return;
        }

        point.setColor(point.getColor().nextColor());
        point.setUpdated(true);
    }

    public static void togglePointer(Player player) {
        final UUID uuid = player.getUniqueId();
        final LaserPoint point = LaserPointerManager.getPoint(uuid);
        if (point == null) {
            LaserPointerManager.addPoint(new LaserPoint(uuid, null, LaserColor.getColors().get(0), false));
            return;
        }

        LaserPointerManager.removePoint(uuid);
    }

    @AllArgsConstructor
    public static class LaserPoint {
        @Getter @Setter private UUID playerId;
        @Getter @Setter private BlockDisplayID displayID;
        @Getter @Setter private LaserColor color;
        @Getter @Setter private boolean updated;
    }

    public static class LaserColor {
        @Getter public static final List<LaserColor> colors = new ArrayList<>();
        static {
            new LaserColor(Color.RED, Material.RED_CONCRETE);
            new LaserColor(Color.ORANGE, Material.ORANGE_CONCRETE);
            new LaserColor(Color.YELLOW, Material.YELLOW_CONCRETE);
            new LaserColor(Color.LIME, Material.LIME_CONCRETE);
            new LaserColor(Color.GREEN, Material.GREEN_CONCRETE);
            new LaserColor(Color.TEAL, Material.CYAN_CONCRETE);
            new LaserColor(Color.NAVY, Material.LIGHT_BLUE_CONCRETE);
            new LaserColor(Color.BLUE, Material.BLUE_CONCRETE);
            new LaserColor(Color.PURPLE, Material.PURPLE_CONCRETE);
            new LaserColor(Color.FUCHSIA, Material.MAGENTA_CONCRETE);
            new LaserColor(Color.FUCHSIA, Material.PINK_CONCRETE);
        }

        @Getter private final Color color;
        @Getter private final Material material;

        public LaserColor(Color color, Material material) {
            this.color = color;
            this.material = material;
            colors.add(this);
        }

        public LaserColor nextColor() {
            return colors.get(colors.indexOf(this));
        }
    }
}
