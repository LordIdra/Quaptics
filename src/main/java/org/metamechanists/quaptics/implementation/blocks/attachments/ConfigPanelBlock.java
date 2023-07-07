package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.panels.config.ConfigPanel;
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

    default void interact(@NotNull final ConnectionGroup group, final String name, final String type) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ConfigPanelId> panelId = getPanelId(location.get());
        if (panelId.isEmpty()) {
            return;
        }

        final ConfigPanel panel = createPanel(panelId.get(), group.getId());
        panel.interact(location.get(), name, type);
    }

    default void setPanelHidden(@NotNull final ConnectionGroup group, final boolean hidden) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ConfigPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).setPanelHidden(hidden));
    }

    ConfigPanel createPanel(final ConfigPanelId panelId, final ConnectionGroupId groupId);
}
