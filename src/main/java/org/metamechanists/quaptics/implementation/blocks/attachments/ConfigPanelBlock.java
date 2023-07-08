package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.config.ConfigPanel;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

public interface ConfigPanelBlock {
    static Optional<ConfigPanelId> getPanelId(final Location location) {
        return BlockStorageAPI.getConfigPanelId(location, Keys.BS_PANEL_ID);
    }

    static void setPanelId(final Location location, @NotNull final ConfigPanelId id) {
        BlockStorageAPI.set(location, Keys.BS_PANEL_ID, id);
    }

    default void interact(@NotNull final ConnectionGroup group, final String name, final String type) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ConfigPanelId> panelId = getPanelId(location.get());
        if (panelId.isEmpty()) {
            return;
        }

        final ConfigPanel panel = getPanel(panelId.get(), group.getId());
        panel.interact(location.get(), name, type);
    }

    // TODO config panel wand
    default void setPanelHidden(@NotNull final ConnectionGroup group, final boolean hidden) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ConfigPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> getPanel(panelId, group.getId()).setPanelHidden(hidden));
    }

    default void onPlaceConfigPanelBlock(@NotNull final BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> group = ConnectedBlock.getGroup(location);
        group.ifPresent(connectionGroup -> setPanelId(location, createPanel(location, event.getPlayer(), connectionGroup).getId()));
    }

    default void onBreakConfigPanelBlock(@NotNull final Location location) {
        final Optional<ConfigPanelId> panelId = getPanelId(location);
        final Optional<ConfigPanelContainer> panel = panelId.isPresent() ? panelId.get().get() : Optional.empty();
        panel.ifPresent(ConfigPanelContainer::remove);
    }

    ConfigPanel createPanel(final Location location, final Player player, @NotNull final ConnectionGroup group);
    ConfigPanel getPanel(final ConfigPanelId panelId, final ConnectionGroupId groupId);
}
