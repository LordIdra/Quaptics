package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PanelBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.panels.BlockPanel;
import org.metamechanists.quaptics.implementation.blocks.panels.ChargerPanel;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Charger extends ConnectedBlock implements PanelBlock {
    private final Vector3f mainDisplaySize = new Vector3f(0.7F, 0.3F, 0.7F);
    private final Vector3f glassDisplaySize = new Vector3f(0.5F, 0.1F, 0.5F);
    private final Vector3f itemDisplaySize = new Vector3f(0.5F);
    private final Vector3f topOffset = new Vector3f(0, 0.35F, 0);
    private final Vector3f bottomOffset = new Vector3f(0, -0.35F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public Charger(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainTop", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.SMOOTH_STONE_SLAB.createBlockData("[type=top]"))
                .setTransformation(Transformations.adjustedScaleAndOffset(mainDisplaySize, topOffset))
                .build());
        displayGroup.addDisplay("mainBottom", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.SMOOTH_STONE_SLAB.createBlockData("[type=bottom]"))
                .setTransformation(Transformations.adjustedScaleAndOffset(mainDisplaySize, bottomOffset))
                .build());
        displayGroup.addDisplay("glassTop", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedScaleAndOffset(glassDisplaySize, topOffset))
                .build());
        displayGroup.addDisplay("glassBottom", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedScaleAndOffset(glassDisplaySize, bottomOffset))
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(Transformations.unadjustedScale(itemDisplaySize))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    public BlockPanel createPanel(final PanelId panelId, final ConnectionGroupId groupId) {
        return new ChargerPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = getGroup(location);
        optionalGroup.ifPresent(group -> setPanelId(location, new ChargerPanel(location, group.getId()).getId()));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        final Optional<PanelId> panelId = getPanelId(location);
        final Optional<Panel> panel = panelId.isPresent() ? panelId.get().get() : Optional.empty();
        panel.ifPresent(Panel::remove);
    }

    @Override
    protected void onRightClick(final Location location, final Player player) {
        final Optional<ItemDisplay> itemDisplay = getItemDisplay(location);
        if (itemDisplay.isEmpty()) {
            return;
        }

        final ItemStack stack = itemDisplay.get().getItemStack();
        if (stack == null || stack.getType().isEmpty()) {
            addItem(player, itemDisplay.get());
            return;
        }

        removeItem(player, itemDisplay.get());
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<ItemDisplay> itemDisplay = getItemDisplay(group);
        if (itemDisplay.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(group, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        QuapticChargeableItem.chargeItem(inputLink.get(), itemDisplay.get());
        updatePanel(group);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        doBurnoutCheck(group, "input");
    }

    private static Optional<ItemDisplay> getItemDisplay(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return Optional.empty();
        }

        final Optional<Display> display = getDisplay(location.get(), "item");
        if (display.isEmpty()) {
            return Optional.empty();
        }

        if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
            return Optional.empty();
        }

        return Optional.of(itemDisplay);
    }

    private static Optional<ItemDisplay> getItemDisplay(final Location location) {
        final Optional<Display> display = getDisplay(location, "item");
        if (display.isEmpty()) {
            return Optional.empty();
        }

        if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
            return Optional.empty();
        }

        return Optional.of(itemDisplay);
    }

    private static void addItem(@NotNull final Player player, final ItemDisplay display) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
        if (itemStack.getType().isEmpty()) {
            return;
        }

        if (!(SlimefunItem.getByItem(itemStack) instanceof QuapticChargeableItem)) {
            return;
        }

        player.getInventory().setItemInMainHand(null);
        display.setItemStack(itemStack);
    }

    private static void removeItem(final Player player, @NotNull final ItemDisplay display) {
        final ItemStack itemStack = display.getItemStack();
        if (itemStack == null) {
            return;
        }

        display.setItemStack(null);
        QuapticChargeableItem.updateLore(itemStack);
        ItemUtils.addOrDropItemMainHand(player, itemStack);
    }
}
