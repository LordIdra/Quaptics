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
import org.metamechanists.death_lasers.items.Items;
import org.metamechanists.death_lasers.utils.Keys;
import org.metamechanists.death_lasers.connections.storage.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;
import org.metamechanists.death_lasers.utils.Language;
import org.metamechanists.death_lasers.utils.PersistentDataUtils;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private boolean isSourceSet(ItemStack stack) {
        return stack.getItemMeta().getPersistentDataContainer().has(Keys.SOURCE);
    }

    private void setSourceConnectionPoint(Player player, Location sourcePointLocation, ItemStack stack) {
        final ConnectionPoint sourceOutputPoint = ConnectionPointStorage.getPoint(sourcePointLocation);
        if (!(sourceOutputPoint instanceof ConnectionPointOutput)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.source-must-be-output"));
            return;
        }
        sourceOutputPoint.select();
        PersistentDataUtils.setLocation(stack, Keys.SOURCE, sourcePointLocation);
    }

    public void unsetSourceConnectionPoint(ItemStack stack) {
        if (isSourceSet(stack)) {
            final Location sourcePointLocation = PersistentDataUtils.getLocation(stack, Keys.SOURCE);
            final ConnectionPoint sourcePoint = ConnectionPointStorage.getPoint(sourcePointLocation);
            final ConnectionPointOutput outputSourcePoint = (ConnectionPointOutput)sourcePoint;
            if (outputSourcePoint != null) {
                outputSourcePoint.deselect();
            }
        }

        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(Location pointLocation) {
        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPoint(pointLocation);

        if (sourcePoint instanceof ConnectionPointOutput outputPoint && outputPoint.hasLink()) {
            outputPoint.getLink().remove();
            return;
        }

        if (sourcePoint instanceof ConnectionPointInput inputPoint && inputPoint.hasLink()) {
            inputPoint.getLink().remove();
        }
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

        final ConnectionPoint sourcePoint = ConnectionPointStorage.getPoint(sourcePointLocation);
        final ConnectionPoint targetPoint = ConnectionPointStorage.getPoint(targetPointLocation);

        if (!(targetPoint instanceof ConnectionPointInput inputTargetPoint)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.target-must-be-input"));
            return;
        }

        if (inputTargetPoint.hasLink()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.target-already-linked"));
            return;
        }

        final ConnectionPointOutput outputSourcePoint = (ConnectionPointOutput)sourcePoint;

        if (outputSourcePoint == null) {
            return;
        }

        final ConnectedBlock block1 = ConnectionPointStorage.getPoint(sourcePointLocation).getGroup().getBlock();
        final ConnectedBlock block2 = ConnectionPointStorage.getPoint(targetPointLocation).getGroup().getBlock();

        if (block1.connectionInvalid(sourcePoint, targetPoint) || block2.connectionInvalid(targetPoint, sourcePoint)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.connection-invalid"));
            return;
        }

        block1.connect(sourcePoint, targetPoint);
        block2.connect(targetPoint, sourcePoint);

        setSourceConnectionPoint(player, outputSourcePoint.getLocation(), stack);

        new Link(inputTargetPoint, outputSourcePoint);
    }

    public void use(Player player, Location pointLocation, ItemStack stack) {
        final Location groupLocation = ConnectionPointStorage.getGroupLocation(pointLocation);
        if (!BlockStorage.hasBlockInfo(groupLocation)
                || !(BlockStorage.check(groupLocation) instanceof ConnectedBlock)
                || !Items.targetingWand.canUse(player, false)
                || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)) {
            return;
        }

        if (player.isSneaking()) {
            removeLink(pointLocation);
            unsetSourceConnectionPoint(stack);
        } else if (isSourceSet(stack)) {
            createLink(player, pointLocation, stack);
            unsetSourceConnectionPoint(stack);
        } else {
            setSourceConnectionPoint(player, pointLocation, stack);
        }
    }
}
