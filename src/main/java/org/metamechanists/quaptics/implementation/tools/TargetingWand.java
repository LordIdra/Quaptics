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
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.base.EnergyConnectedBlock;
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
        ConnectionPoint source = ConnectionPointStorage.getPoint(sourceID);
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
            final ConnectionPointOutput sourcePoint = (ConnectionPointOutput)ConnectionPointStorage.getPoint(sourcePointID);

            if (sourcePoint != null) {
                sourcePoint.deselect();
            }
        }

        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(ConnectionPointID pointID) {
        final ConnectionPoint point = ConnectionPointStorage.getPoint(pointID);

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
        final ConnectionPointOutput output = (ConnectionPointOutput) ConnectionPointStorage.getPoint(outputID);

        if (output == null) {
            return;
        }

        if (!(ConnectionPointStorage.getPoint(inputID) instanceof ConnectionPointInput input)) {
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

        final ConnectedBlock block1 = output.getGroup().getBlock();
        final ConnectedBlock block2 = input.getGroup().getBlock();

        if (input.hasLink()) {
            input.getLink().remove();
        }

        if (output.hasLink()) {
            output.getLink().remove();
        }

        block1.connect(output, input);
        block2.connect(input, output);

        setSource(player, output.getId(), stack);

        new Link(input, output);
    }

    public void use(Player player, ConnectionPointID pointId, ItemStack stack) {
        final ConnectionGroup group = ConnectionPointStorage.getGroup(pointId);
        if (!BlockStorage.hasBlockInfo(group.getLocation())
                || !(BlockStorage.check(group.getLocation()) instanceof EnergyConnectedBlock)
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
