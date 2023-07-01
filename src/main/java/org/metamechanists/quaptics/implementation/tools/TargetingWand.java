package org.metamechanists.quaptics.implementation.tools;

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
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.PersistentDataUtils;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

public class TargetingWand extends SlimefunItem {
    public TargetingWand(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private boolean isSourceSet(@NotNull ItemStack stack) {
        return stack.getItemMeta().getPersistentDataContainer().has(Keys.SOURCE);
    }

    private void setSource(Player player, ConnectionPointId sourceId, ItemStack stack) {
        final ConnectionPoint source = sourceId.get();
        if (!(source instanceof ConnectionPointOutput)) {
            Language.sendLanguageMessage(player, "targeting-wand.source-must-be-output");
            return;
        }

        source.select();
        PersistentDataUtils.setString(stack, Keys.SOURCE, sourceId.toString());
    }

    public void unsetSource(ItemStack stack) {
        if (isSourceSet(stack)) {
            final ConnectionPointId sourcePointId = new ConnectionPointId(PersistentDataUtils.getString(stack, Keys.SOURCE));
            final ConnectionPointOutput sourcePoint = (ConnectionPointOutput) sourcePointId.get();

            if (sourcePoint != null) {
                sourcePoint.deselect();
            }
        }

        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private void removeLink(ConnectionPointId pointId) {
        final ConnectionPoint point = pointId.get();

        if (point instanceof ConnectionPointOutput outputPoint && outputPoint.hasLink()) {
            outputPoint.getLink().remove();
            return;
        }

        if (point instanceof ConnectionPointInput inputPoint && inputPoint.hasLink()) {
            inputPoint.getLink().remove();
        }
    }

    private void createLink(Player player, ConnectionPointId inputId, ItemStack stack) {
        final ConnectionPointId outputId = new ConnectionPointId(PersistentDataUtils.getString(stack, Keys.SOURCE));
        final ConnectionPointOutput output = (ConnectionPointOutput) outputId.get();

        if (output == null) {
            return;
        }

        if (!(inputId.get() instanceof ConnectionPointInput input)) {
            Language.sendLanguageMessage(player, "targeting-wand.target-must-be-input");
            return;
        }

        if (output.getLocation().getWorld().getUID() != input.getLocation().getWorld().getUID()) {
            Language.sendLanguageMessage(player, "targeting-wand.different-worlds");
            return;
        }

        if (output.getLocation().distance(input.getLocation()) < 0.0001F) {
            Language.sendLanguageMessage(player, "targeting-wand.same-connection-point");
            return;
        }

        if (input.getGroup().getPoints().containsValue(outputId)) {
            Language.sendLanguageMessage(player, "targeting-wand.same-connection-group");
            return;
        }

        if (input.hasLink() && output.hasLink()) {
            final ConnectionPointInput inputPoint = output.getLink().getInput();
            final ConnectionPointOutput outputPoint = input.getLink().getOutput();
            if (inputPoint != null && input.getId().equals(inputId) && outputPoint != null && outputPoint.getId().equals(outputId)) {
                return;
            }
        }

        final ConnectionGroup outputGroup = output.getGroup();
        final ConnectionGroup inputGroup = input.getGroup();
        if (outputGroup == null || inputGroup == null) {
            return;
        }

        final ConnectedBlock outputBlock = outputGroup.getBlock();
        final ConnectedBlock inputBlock = inputGroup.getBlock();

        if (input.hasLink()) {
            input.getLink().remove();
        }

        if (output.hasLink()) {
            output.getLink().remove();
        }

        outputBlock.connect(outputId, inputId);
        inputBlock.connect(inputId, outputId);

        setSource(player, outputId, stack);

        new Link(inputId, outputId);
    }

    public void use(Player player, ConnectionPointId pointId, ItemStack stack) {
        final ConnectionPoint point = pointId.get();
        if (point == null) {
            return;
        }

        final ConnectionGroup group = point.getGroup();
        if (group == null) {
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        if (!BlockStorage.hasBlockInfo(location)
                || !(BlockStorage.check(location) instanceof ConnectedBlock)
                || !canUse(player, false)
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
