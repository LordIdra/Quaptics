package org.metamechanists.quaptics.implementation.tools;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.PersistentDataUtils;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private boolean isSourceSet(ItemStack stack) {
        return stack.getItemMeta().getPersistentDataContainer().has(Keys.SOURCE);
    }

    private void setSource(Player player, ConnectionPointID sourceID, ItemStack stack) {
        final ConnectionPoint source = ConnectionPoint.fromID(sourceID);
        if (!(source instanceof ConnectionPointOutput)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.source-must-be-output"));
            return;
        }

        source.select();
        PersistentDataUtils.setString(stack, Keys.SOURCE, sourceID.toString());
    }

    public void unsetSource(ItemStack stack) {
        if (isSourceSet(stack)) {
            final ConnectionPointID sourcePointID = new ConnectionPointID(PersistentDataUtils.getString(stack, Keys.SOURCE));
            final ConnectionPointOutput sourcePoint = (ConnectionPointOutput) ConnectionPoint.fromID(sourcePointID);

            if (sourcePoint != null) {
                sourcePoint.deselect();
            }
        }

        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(ConnectionPointID pointID) {
        final ConnectionPoint point = ConnectionPoint.fromID(pointID);

        if (point instanceof ConnectionPointOutput outputPoint && outputPoint.hasLink()) {
            outputPoint.getLink().remove();
            return;
        }

        if (point instanceof ConnectionPointInput inputPoint && inputPoint.hasLink()) {
            inputPoint.getLink().remove();
        }
    }

    private void createLink(Player player, ConnectionPointID inputID, ItemStack stack) {
        final ConnectionPointID outputID = new ConnectionPointID(PersistentDataUtils.getString(stack, Keys.SOURCE));
        final ConnectionPointOutput output = (ConnectionPointOutput) ConnectionPoint.fromID(outputID);

        if (output == null) {
            return;
        }

        if (!(ConnectionPoint.fromID(inputID) instanceof ConnectionPointInput input)) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.target-must-be-input"));
            return;
        }

        if (output.getLocation().getWorld().getUID() != input.getLocation().getWorld().getUID()) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.different-worlds"));
            return;
        }

        if (output.getLocation().distance(input.getLocation()) < 0.0001F) {
            player.sendMessage(Language.getLanguageEntry("targeting-wand.same-connection-point"));
            return;
        }

        if (input.hasLink()
                && output.hasLink()
                && input.getLink().getInput().getID().equals(inputID)
                && output.getLink().getOutput().getID().equals(outputID)) {
            return;
        }

        final ConnectedBlock block1 = output.getGroup().getBlock();
        final ConnectedBlock block2 = input.getGroup().getBlock();

        if (input.hasLink()) {
            input.getLink().remove();
        }

        if (output.hasLink()) {
            output.getLink().remove();
        }

        block1.connect(outputID, inputID);
        block2.connect(inputID, outputID);

        setSource(player, outputID, stack);

        new Link(inputID, outputID);
    }

    public void use(Player player, ConnectionPointID pointId, ItemStack stack) {
        final ConnectionGroup group = ConnectionPoint.fromID(pointId).getGroup();
        if (!BlockStorage.hasBlockInfo(group.getLocation())
                || !(BlockStorage.check(group.getLocation()) instanceof ConnectedBlock)
                || !Items.targetingWand.canUse(player, false)
                || !Slimefun.getProtectionManager().hasPermission(player, player.getLocation(), Interaction.INTERACT_BLOCK)) {
            return;
        }

        if (player.isSneaking()) {
            removeLink(pointId);
            unsetSource(stack);
        } else if (isSourceSet(stack)) {
            createLink(player, pointId, stack);
            unsetSource(stack);
        } else {
            setSource(player, pointId, stack);
        }
    }
}
