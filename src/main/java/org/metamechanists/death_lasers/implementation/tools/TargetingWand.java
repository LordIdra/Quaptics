package org.metamechanists.death_lasers.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.Items;
import org.metamechanists.death_lasers.Keys;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.emitters.LaserEmitter;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.PersistentDataUtils;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private boolean isSourceSet(ItemStack stack) {
        return stack.getItemMeta().getPersistentDataContainer().has(Keys.SOURCE);
    }

    private void setSourceConnectionPoint(Location sourcePointLocation, ItemStack stack) {
        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPointFromPointLocation(sourcePointLocation);
        sourcePoint.select();
        PersistentDataUtils.setLocation(stack, Keys.SOURCE, sourcePointLocation);
    }

    private void unsetSourceConnectionPoint(ItemStack stack) {
        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(Player player, Location sourcePointLocation) {
        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPointFromPointLocation(sourcePointLocation);
        if (!(sourcePoint instanceof ConnectionPointOutput outputPoint)) {
            return;
        }

        if (!outputPoint.hasLink()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.not-linked"));
            return;
        }

        outputPoint.unlink();
    }

    private void createLink(Player player, Location targetPointLocation, ItemStack stack) {
        final Location sourcePointLocation = PersistentDataUtils.getLocation(stack, Keys.SOURCE);

        if (sourcePointLocation.getWorld().getUID() != targetPointLocation.getWorld().getUID()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.different-worlds"));
            return;
        }

        if (sourcePointLocation.distance(targetPointLocation) < 0.0001F) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.same-connection-point"));
            return;
        }

        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPointFromPointLocation(sourcePointLocation);
        final ConnectionPoint targetPoint = ConnectionPointStorage.getPointFromPointLocation(targetPointLocation);

        if (!(sourcePoint instanceof ConnectionPointOutput outputPoint)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.source-must-be-input"));
            return;
        }

        outputPoint.deselect();
        outputPoint.link((ConnectionPointInput) targetPoint);
    }

    public void use(Player player, Location pointLocation, ItemStack stack) {
        final Location groupLocation = ConnectionPointStorage.getGroupLocationFromPointLocation(pointLocation);
        if (!BlockStorage.hasBlockInfo(groupLocation)
                || !(BlockStorage.check(groupLocation) instanceof LaserEmitter)
                || !Items.targetingWand.canUse(player, false)
                || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)) {
            return;
        }

        if (player.isSneaking()) {
            removeLink(player, pointLocation);
            unsetSourceConnectionPoint(stack);
        } else if (isSourceSet(stack)) {
            createLink(player, pointLocation, stack);
            unsetSourceConnectionPoint(stack);
        } else {
            setSourceConnectionPoint(pointLocation, stack);
        }
    }
}
