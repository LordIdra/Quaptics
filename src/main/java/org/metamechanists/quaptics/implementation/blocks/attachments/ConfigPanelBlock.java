package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.panels.config.BlockConfigPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

@FunctionalInterface
public interface ConfigPanelBlock {
    static Optional<ConfigPanelId> getPanelId(final Location location) {
        return BlockStorageAPI.getConfigPanelId(location, Keys.BS_PANEL_ID);
    }

    static void setPanelId(final Location location, @NotNull final ConfigPanelId id) {
        BlockStorageAPI.set(location, Keys.BS_PANEL_ID, id);
    }

    default void updatePanel(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ConfigPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).update());
    }

    default void setPanelHidden(@NotNull final ConnectionGroup group, final boolean hidden) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ConfigPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).setPanelHidden(hidden));
    }

    BlockConfigPanel createPanel(final ConfigPanelId panelId, final ConnectionGroupId groupId);
}
