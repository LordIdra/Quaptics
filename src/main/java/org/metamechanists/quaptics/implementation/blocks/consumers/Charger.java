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

public class Charger extends ConnectedBlock {
    private final Vector3f mainDisplaySize = new Vector3f(0.7F, 0.3F, 0.7F);
    private final Vector3f glassDisplaySize = new Vector3f(0.5F, 0.1F, 0.5F);
    private final Vector3f itemDisplaySize = new Vector3f(0.5F);
    private final Vector3f topOffset = new Vector3f(0, 0.35F, 0);
    private final Vector3f bottomOffset = new Vector3f(0, -0.35F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public Charger(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, Settings settings) {
        super(group, item, recipeType, recipe, settings);
        addItemHandler(onUseQuapticCharger());
    }

    public BlockUseHandler onUseQuapticCharger() {
        return event -> {
            final Block block = event.getClickedBlock().orElse(null);
            if (block == null) {
                return;
            }

            final Location location = block.getLocation();
            if (!(getDisplay(location, "item") instanceof ItemDisplay display)) {
                return;
            }

            final Player player = event.getPlayer();
            if (display.getItemStack() == null || display.getItemStack().getType().isEmpty()) {
                addItem(player, display);
                return;
            }

            removeItem(player, display);
        };
    }

    public @Nullable ItemStack getItem(Location location) {
        if (!(getDisplay(location, "item") instanceof ItemDisplay display)) {
            return null;
        }
        return display.getItemStack();
    }

    protected void addItem(@NotNull Player player, ItemDisplay display) {
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

    protected void removeItem(Player player, @NotNull ItemDisplay display) {
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
    public void onQuapticTick(@NotNull ConnectionGroup group) {
        final Location location = group.getLocation();
        if (!(getDisplay(location, "item") instanceof ItemDisplay display)) {
            return;
        }

        QuapticChargeableItem.chargeItem(group, display);
        updatePanel(group);
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
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
                .setItemStack(null)
                .setTransformation(Transformations.unadjustedScale(itemDisplaySize))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupId groupId, Player player, Location location) {
        return List.of(new ConnectionPointInput(groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    public @Nullable PanelId getPanelId(Location location) {
        final String stringId = BlockStorage.getLocationInfo(location, Keys.BS_PANEL_ID);
        return stringId == null ? null : new PanelId(stringId);
    }

    private void setPanelId(Location location, @NotNull PanelId id) {
        BlockStorage.addBlockInfo(location, Keys.BS_PANEL_ID, id.toString());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull BlockPlaceEvent event) {
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
    protected void onBreak(@NotNull BlockBreakEvent event) {
        super.onBreak(event);
        final Location location = event.getBlock().getLocation();
        final PanelId panelId = getPanelId(location);
        final Panel panel = panelId != null ? panelId.get() : null;
        if (panel != null) {
            panel.remove();
        }
    }

    protected void updatePanel(@NotNull ConnectionGroup group) {
        final PanelId id = getPanelId(group.getLocation());
        if (id != null) {
            final ChargerPanel panel = new ChargerPanel(id, group.getId());
            panel.update();
        }
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final ConnectionPoint input = group.getPoint("input");
        if (input != null) {
            doBurnoutCheck(group, input);
        }
    }
}
