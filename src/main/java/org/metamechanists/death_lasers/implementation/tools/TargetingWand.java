package org.metamechanists.death_lasers.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.death_lasers.Items;
import org.metamechanists.death_lasers.Keys;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.emitters.LaserEmitter;
import org.metamechanists.death_lasers.utils.ConnectionGroupLocation;
import org.metamechanists.death_lasers.utils.ConnectionPointLocation;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.PersistentDataUtils;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private boolean isSourceSet(ItemStack stack) {
        return stack.getItemMeta().getPersistentDataContainer().has(Keys.SOURCE);
    }

    private void setSourceConnectionPoint(ConnectionPointLocation sourceLocation, ItemStack stack) {
        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPoint(sourceLocation);
        sourcePoint.select();
        PersistentDataUtils.setLocation(stack, Keys.SOURCE, sourceLocation.location);
    }

    private void unsetSourceConnectionPoint(ItemStack stack) {
        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(Player player, ConnectionPointLocation sourceLocation) {
        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPoint(sourceLocation);
        if (!(sourcePoint instanceof ConnectionPointOutput outputPoint)) {
            return;
        }

        if (!outputPoint.hasLink()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.not-linked"));
            return;
        }

        outputPoint.unlink();
    }

    private void createLink(Player player, ConnectionPointLocation targetLocation, ItemStack stack) {
        final ConnectionPointLocation sourceLocation = new ConnectionPointLocation(PersistentDataUtils.getLocation(stack, Keys.SOURCE));

        if (sourceLocation.location.getWorld().getUID() != targetLocation.location.getWorld().getUID()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.different-worlds"));
            return;
        }

        if (sourceLocation.location.distance(targetLocation.location) < 0.0001F) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.same-connection-point"));
            return;
        }

        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPoint(sourceLocation);
        final ConnectionPoint targetPoint = ConnectionPointStorage.getPoint(targetLocation);

        if (!(sourcePoint instanceof ConnectionPointOutput outputPoint)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.source-must-be-input"));
            return;
        }

        outputPoint.deselect();
        outputPoint.link((ConnectionPointInput) targetPoint);
    }

    public void use(Player player, ConnectionPointLocation pointLocation, ItemStack stack) {
        final ConnectionGroupLocation groupLocation = ConnectionPointStorage.getGroupLocation(pointLocation);
        if (!BlockStorage.hasBlockInfo(groupLocation.location)
                || !(BlockStorage.check(groupLocation.location) instanceof LaserEmitter)
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
