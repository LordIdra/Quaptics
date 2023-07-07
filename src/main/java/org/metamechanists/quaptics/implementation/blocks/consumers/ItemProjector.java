package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.panels.info.implementation.ChargerInfoPanel;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class ItemProjector extends ConnectedBlock implements ItemHolderBlock, PowerAnimatedBlock {
    private static final Brightness BRIGHTNESS_ON = new Brightness(15, 0);
    private static final Brightness BRIGHTNESS_OFF = new Brightness(3, 0);
    private final Vector3f mainDisplaySize = new Vector3f(0.6F, 0.6F, 0.6F);
    private final Vector3f itemDisplaySize = new Vector3f(0.5F);
    private final Vector3f itemDisplayInitialOffset = new Vector3f(0, 0.9F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public ItemProjector(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.CYAN_STAINED_GLASS.createBlockData())
                .setTransformation(Transformations.adjustedRotateScale(mainDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .setBrightness(BRIGHTNESS_OFF.getBlockLight())
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(Transformations.unadjustedScaleTranslate(itemDisplaySize, itemDisplayInitialOffset))
                .setViewRange(VIEW_RANGE_OFF)
                .setBrightness(BRIGHTNESS_ON.getBlockLight())
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = getGroup(location);
        optionalGroup.ifPresent(group -> InfoPanelBlock.setPanelId(location, new ChargerInfoPanel(location, group.getId()).getId()));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        final Optional<InfoPanelId> panelId = InfoPanelBlock.getPanelId(location);
        final Optional<InfoPanelContainer> panel = panelId.isPresent() ? panelId.get().get() : Optional.empty();
        panel.ifPresent(InfoPanelContainer::remove);
        ItemHolderBlock.getStack(location).ifPresent(stack -> location.getWorld().dropItem(location, stack));
    }

    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        interact(location, player);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        doBurnoutCheck(group, "input");

        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(group, "input");
        onPoweredAnimation(location.get(), inputLink.isPresent() && settings.isOperational(inputLink));
    }

    @Override
    public boolean onInsert(@NotNull final ItemStack stack, @NotNull final Player player) {
        return true;
    }

    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final ItemStack stack) {
        return Optional.of(stack);
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.POLISHED_DEEPSLATE_SLAB;
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        final Optional<Display> mainDisplay = getDisplay(location, "main");
        if (mainDisplay.isEmpty()) {
            return;
        }

        mainDisplay.get().setBrightness(powered ? BRIGHTNESS_ON : BRIGHTNESS_OFF);

        final Optional<ItemDisplay> itemDisplay = ItemHolderBlock.getItemDisplay(location);
        if (itemDisplay.isEmpty()) {
            return;
        }

        itemDisplay.get().setViewRange(powered ? VIEW_RANGE_ON : VIEW_RANGE_OFF);
    }
}
