package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display.Brightness;
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
import org.metamechanists.quaptics.implementation.blocks.attachments.ConfigPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.config.BlockConfigPanel;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.panels.config.implementation.LaunchpadConfigPanel;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Launchpad extends ConnectedBlock implements ConfigPanelBlock, PowerAnimatedBlock {
    private static final Vector RELATIVE_PANEL_LOCATION = new Vector(0, 0, -0.51);
    private static final Brightness BRIGHTNESS_ON = new Brightness(15, 0);
    private static final Brightness BRIGHTNESS_OFF = new Brightness(4, 0);
    private static final Vector3f mainDisplaySize = new Vector3f(0.8F, 0.1F, 0.8F);
    private static final Vector3f mainDisplayOffset = new Vector3f(0, 0.51F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public Launchpad(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.CYAN_CONCRETE_POWDER.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(mainDisplaySize, mainDisplayOffset))
                .setBrightness(BRIGHTNESS_OFF.getBlockLight())
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
        if (optionalGroup.isEmpty()) {
            return;
        }

        ConfigPanelBlock.setPanelId(location, new LaunchpadConfigPanel(
                formatPointLocation(event.getPlayer(), location, RELATIVE_PANEL_LOCATION),
                optionalGroup.get().getId(),
                (float) Transformations.yawToCardinalDirection(event.getPlayer().getEyeLocation().getYaw()))
                .getId());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        final Optional<ConfigPanelId> panelId = ConfigPanelBlock.getPanelId(location);
        final Optional<ConfigPanelContainer> panel = panelId.isPresent() ? panelId.get().get() : Optional.empty();
        panel.ifPresent(ConfigPanelContainer::remove);
    }

    @Override
    public BlockConfigPanel createPanel(final ConfigPanelId panelId, final ConnectionGroupId groupId) {
        return new LaunchpadConfigPanel(panelId, groupId);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        doBurnoutCheck(group, "input");

        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location.get(), "input");
        onPoweredAnimation(location.get(), inputLink.isPresent() && settings.isOperational(inputLink));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.LIGHT_GRAY_CONCRETE;
    }
    
    public void launch(final Location location, final Player player) {
        final Optional<Link> inputLink = getLink(location, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        player.setVelocity(new Vector(10, 10, 0));
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        getDisplay(location, "main").ifPresent(value -> value.setBrightness(powered ? BRIGHTNESS_ON : BRIGHTNESS_OFF));
    }
}
