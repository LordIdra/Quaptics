package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.panels.BlockPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

@FunctionalInterface
public interface PanelBlock {
    default Optional<PanelId> getPanelId(final Location location) {
        return BlockStorageAPI.getPanelId(location, Keys.BS_PANEL_ID);
    }

    default void setPanelId(final Location location, @NotNull final PanelId id) {
        BlockStorageAPI.set(location, Keys.BS_PANEL_ID, id);
    }

    default void updatePanel(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<PanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).update());
    }

    default void setPanelHidden(@NotNull final ConnectionGroup group, final boolean hidden) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<PanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).setPanelHidden(hidden));
    }

    BlockPanel createPanel(final PanelId panelId, final ConnectionGroupId groupId);
}
