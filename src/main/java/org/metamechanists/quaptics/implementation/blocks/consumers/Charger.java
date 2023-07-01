package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.implementation.panels.ChargerPanel;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Charger extends ConnectedBlock {
    private final Vector3f mainDisplaySize = new Vector3f(0.7F, 0.3F, 0.7F);
    private final Vector3f glassDisplaySize = new Vector3f(0.5F, 0.1F, 0.5F);
    private final Vector3f itemDisplaySize = new Vector3f(0.5F);
    private final Vector3f topOffset = new Vector3f(0, 0.35F, 0);
    private final Vector3f bottomOffset = new Vector3f(0, -0.35F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public Charger(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(group, item, recipeType, recipe, settings);
        addItemHandler(onUseQuapticCharger());
    }

    @NotNull
    private static BlockUseHandler onUseQuapticCharger() {
        return event -> {
            final Block block = event.getClickedBlock().orElse(null);
            if (block == null) {
                return;
            }

            final Location location = block.getLocation();
            final Optional<Display> display = getDisplay(location, "item");
            if (display.isEmpty()) {
                return;
            }

            if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
                return;
            }

            final Player player = event.getPlayer();
            if (itemDisplay.getItemStack() == null || itemDisplay.getItemStack().getType().isEmpty()) {
                addItem(player, itemDisplay);
                return;
            }

            removeItem(player, itemDisplay);
        };
    }

    public static Optional<ItemStack> getItem(final Location location) {
        final Optional<Display> display = getDisplay(location, "item");
        if (display.isEmpty()) {
            return Optional.empty();
        }

        if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
            return Optional.empty();
        }

        final ItemStack stack = itemDisplay.getItemStack();
        return stack == null || stack.getItemMeta() == null ? Optional.empty() : Optional.of(stack);
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
            // This should never be reached
            return;
        }

        display.setItemStack(null);
        QuapticChargeableItem.updateLore(itemStack);
        ItemUtils.addOrDropItem(player, itemStack);
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<Display> display = getDisplay(location.get(), "item");
        if (display.isEmpty()) {
            return;
        }

        if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
            return;
        }

        QuapticChargeableItem.chargeItem(group, itemDisplay);
        updatePanel(group);
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
        return List.of(new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    private static Optional<PanelId> getPanelId(final Location location) {
        final String panelId = BlockStorage.getLocationInfo(location, Keys.BS_PANEL_ID);
        return panelId == null ? Optional.empty() : Optional.of(new PanelId(panelId));
    }

    private static void setPanelId(final Location location, @NotNull final PanelId id) {
        BlockStorage.addBlockInfo(location, Keys.BS_PANEL_ID, id.toString());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final ConnectionGroup group = getGroup(location);
        if (group == null) {
            return;
        }

        setPanelId(location, new ChargerPanel(location, group.getId()).getId());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final BlockBreakEvent event) {
        super.onBreak(event);
        final Location location = event.getBlock().getLocation();
        final PanelId panelId = getPanelId(location);
        final Panel panel = panelId != null ? panelId.get() : null;
        if (panel != null) {
            panel.remove();
        }
    }

    private static void updatePanel(@NotNull final ConnectionGroup group) {
        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final Optional<PanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> new ChargerPanel(panelId, group.getId()).update());
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        final Optional<ConnectionPoint> input = group.getPoint("input");
        input.ifPresent(point -> doBurnoutCheck(group, point));
    }
}
