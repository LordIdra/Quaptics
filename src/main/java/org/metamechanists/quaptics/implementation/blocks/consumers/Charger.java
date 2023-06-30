package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.metalib.utils.ItemUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.List;

public class Charger extends ConnectedBlock {
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f itemDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
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
            if (display.getItemStack() == null) {
                addItem(location, player, display);
                return;
            }

            removeItem(player, display);
        };
    }

    protected void addItem(Location location, Player player, ItemDisplay display) {
        final ItemStack itemStack = player.getInventory().getItemInMainHand().clone();
        if (itemStack.getType().isEmpty()) {
            return;
        }

        if (!(SlimefunItem.getByItem(itemStack) instanceof QuapticChargeableItem)) {
            return;
        }

        player.getInventory().setItemInMainHand(null);
        display.setItemStack(itemStack);

        QuapticChargeableItem.chargeItem(getGroup(location), itemStack);
    }

    protected void removeItem(Player player, ItemDisplay display) {
        final ItemStack itemStack = display.getItemStack();
        if (itemStack == null) {
            // This should never be reached
            return;
        }

        QuapticChargeableItem.updateLore(itemStack);
        ItemUtils.addOrDropItem(player, itemStack);
    }

    @Override
    public void onQuapticTick(ConnectionGroup group) {
        final Location location = group.getLocation();
        if (!(getDisplay(location, "item") instanceof ItemDisplay display)) {
            return;
        }

        final ItemStack itemStack = display.getItemStack();
        if (itemStack == null || itemStack.getType().isEmpty()) {
            return;
        }

        QuapticChargeableItem.chargeItem(group, itemStack);
    }

    @Override
    protected void addDisplays(DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_BLUE_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, mainDisplayRotation))
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setItemStack(null)
                .setTransformation(Transformations.adjustedScale(itemDisplaySize))
                .setBillboard(Display.Billboard.VERTICAL)
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        return List.of(new ConnectionPointInput(groupID, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");

        doBurnoutCheck(group, input);
    }
}
