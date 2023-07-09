package org.metamechanists.quaptics.implementation.tools.targetingwand;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.PersistentDataUtils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;

import java.util.Optional;

public class TargetingWand extends SlimefunItem {
    public static final SlimefunItemStack TARGETING_WAND = new SlimefunItemStack(
            "QP_TARGETING_WAND",
            Material.BLAZE_ROD,
            "&6Targeting Wand",
            "&7● &eRight Click &7to select a source",
            "&7● &eRight Click &7again to create a link",
            "&7● &eShift Right Click &7to remove a link");

    private static final float MIN_POINT_SEPARATION = 0.0001F;

    public TargetingWand(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    private static boolean isSourceSet(@NotNull final ItemStack stack) {
        return stack.getItemMeta().getPersistentDataContainer().has(Keys.SOURCE);
    }
    private static void setSource(final Player player, @NotNull final ConnectionPointId sourceId, final ItemStack stack) {
        final Optional<ConnectionPoint> source = sourceId.get();
        if (source.isEmpty()) {
            return;
        }

        if (!(source.get().isOutput())) {
            Language.sendLanguageMessage(player, "targeting-wand.source-must-be-output");
            return;
        }

        source.get().select();
        PersistentDataUtils.setString(stack, Keys.SOURCE, sourceId.toString());
    }
    public static void unsetSource(final ItemStack stack) {
        if (isSourceSet(stack)) {
            final ConnectionPointId sourcePointId = new ConnectionPointId(PersistentDataUtils.getString(stack, Keys.SOURCE));
            final Optional<ConnectionPoint> sourcePoint = sourcePointId.get();
            sourcePoint.ifPresent(ConnectionPoint::deselect);
        }

        PersistentDataUtils.clear(stack, Keys.SOURCE);
    }

    private static void removeLink(@NotNull final ConnectionPointId pointId) {
        final Optional<ConnectionPoint> point = pointId.get();
        if (point.isEmpty()) {
            return;
        }

        point.get().getLink().ifPresent(Link::remove);
    }
    private static void createLink(final Player player, final @NotNull ConnectionPointId inputId, final ItemStack stack) {
        final ConnectionPointId outputId = new ConnectionPointId(PersistentDataUtils.getString(stack, Keys.SOURCE));
        final Optional<ConnectionPoint> output = outputId.get();
        final Optional<ConnectionPoint> input = inputId.get();
        if (output.isEmpty() || input.isEmpty()) {
            return;
        }

        if (!(input.get().isInput())) {
            Language.sendLanguageMessage(player, "targeting-wand.target-must-be-input");
            return;
        }

        final Optional<Location> inputLocation = input.get().getLocation();
        final Optional<Location> outputLocation = output.get().getLocation();
        if (inputLocation.isEmpty() || outputLocation.isEmpty()) {
            return;
        }

        final Optional<ConnectionGroup> outputGroup = output.get().getGroup();
        final Optional<ConnectionGroup> inputGroup = input.get().getGroup();
        if (outputGroup.isEmpty() || inputGroup.isEmpty()) {
            return;
        }

        if (!outputLocation.get().getWorld().getUID().equals(inputLocation.get().getWorld().getUID())) {
            Language.sendLanguageMessage(player, "targeting-wand.different-worlds");
            return;
        }

        if (outputLocation.get().distance(inputLocation.get()) < MIN_POINT_SEPARATION) {
            Language.sendLanguageMessage(player, "targeting-wand.same-connection-point");
            return;
        }

        if (inputGroup.get().getPoints().containsValue(outputId)) {
            Language.sendLanguageMessage(player, "targeting-wand.same-connection-group");
            return;
        }

        if (input.get().getLink().isPresent() && output.get().getLink().isPresent()) {
            final Optional<ConnectionPoint> inputPoint = output.get().getLink().get().getInput();
            final Optional<ConnectionPoint> outputPoint = input.get().getLink().get().getOutput();

            if (inputPoint.isPresent()
                    && inputPoint.get().getId().equals(inputId)
                    && outputPoint.isPresent()
                    && outputPoint.get().getId().equals(outputId)) {
                return;
            }
        }

        final ConnectedBlock outputBlock = outputGroup.get().getBlock();
        final ConnectedBlock inputBlock = inputGroup.get().getBlock();

        if (input.get().getLink().isPresent()) {
            input.get().getLink().get().remove();
        }

        if (output.get().getLink().isPresent()) {
            output.get().getLink().get().remove();
        }

        outputBlock.connect(outputId, inputId);
        inputBlock.connect(inputId, outputId);

        setSource(player, outputId, stack);

        new Link(inputId, outputId);
    }

    public void use(final Player player, @NotNull final ConnectionPointId pointId, final ItemStack stack) {
        final Optional<ConnectionPoint> point = pointId.get();
        if (point.isEmpty()) {
            return;
        }

        final Optional<ConnectionGroup> group = point.get().getGroup();
        if (group.isEmpty()) {
            return;
        }

        final Optional<Location> location = group.get().getLocation();
        if (location.isEmpty()) {
            return;
        }

        if (!BlockStorageAPI.hasData(location.get())
                || !(BlockStorageAPI.check(location.get()) instanceof ConnectedBlock)
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
