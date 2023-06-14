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
import org.metamechanists.death_lasers.connections.ConnectionPoint;
import org.metamechanists.death_lasers.connections.ConnectionPointGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
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

    private void setSourceConnectionPoint(Location sourceLocation, ItemStack stack) {
        final ConnectionPointGroup sourceGroup = ConnectionPointStorage.getConnectionGroupFromConnectionPointLocation(sourceLocation);
        final ConnectionPoint sourcePoint = sourceGroup.getConnectionPoint(sourceLocation);
        sourcePoint.select();
        PersistentDataUtils.setLocation(stack, Keys.SOURCE, sourceLocation);
    }

    private void unsetSourceConnectionPoint(ItemStack stack) {
        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(Player player, Location sourceLocation, ItemStack stack) {
        final ConnectionPointGroup sourceGroup = ConnectionPointStorage.getConnectionGroupFromConnectionPointLocation(sourceLocation);
        final ConnectionPoint sourcePoint = sourceGroup.getConnectionPoint(sourceLocation);
        if (!sourcePoint.hasLink()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.not-linked"));
            return;
        }
        sourcePoint.unlink();
    }

    private void createLink(Player player, Location targetLocation, ItemStack stack) {
        final Location sourceLocation = PersistentDataUtils.getLocation(stack, Keys.SOURCE);

        if (sourceLocation.getWorld().getUID() != targetLocation.getWorld().getUID()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.different-worlds"));
            return;
        }

        if (sourceLocation.distance(targetLocation) < 0.001F) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.same-connection-point"));
            return;
        }

        final ConnectionPointGroup sourceGroup = ConnectionPointStorage.getConnectionGroupFromConnectionPointLocation(sourceLocation);
        final ConnectionPoint sourcePoint = sourceGroup.getConnectionPoint(sourceLocation);

        sourcePoint.deselect();
        sourcePoint.link(targetLocation);
    }

    public void use(Player player, Location connectionPointLocation, ItemStack stack) {
        final Location sourceBlockLocation = ConnectionPointStorage.getBlockLocationFromConnectionPointLocation(connectionPointLocation);
        if (!BlockStorage.hasBlockInfo(sourceBlockLocation)
                || !(BlockStorage.check(sourceBlockLocation) instanceof LaserEmitter)
                || !Items.targetingWand.canUse(player, false)
                || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)) {
            return;
        }

        if (player.isSneaking()) {
            removeLink(player, connectionPointLocation, stack);
            unsetSourceConnectionPoint(stack);
        } else if (isSourceSet(stack)) {
            createLink(player, connectionPointLocation, stack);
            unsetSourceConnectionPoint(stack);
        } else {
            setSourceConnectionPoint(connectionPointLocation, stack);
        }
    }
}
