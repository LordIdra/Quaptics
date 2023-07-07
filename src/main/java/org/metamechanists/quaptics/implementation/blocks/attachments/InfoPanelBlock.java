package org.metamechanists.quaptics.implementation.blocks.attachments;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Optional;

@FunctionalInterface
public interface InfoPanelBlock {
    static Optional<InfoPanelId> getPanelId(final Location location) {
        return BlockStorageAPI.getPanelId(location, Keys.BS_PANEL_ID);
    }

    static void setPanelId(final Location location, @NotNull final InfoPanelId id) {
        BlockStorageAPI.set(location, Keys.BS_PANEL_ID, id);
    }

    default void updatePanel(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<InfoPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).update());
    }

    default void setPanelHidden(@NotNull final ConnectionGroup group, final boolean hidden) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<InfoPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).setPanelHidden(hidden));
    }

    BlockInfoPanel createPanel(final InfoPanelId panelId, final ConnectionGroupId groupId);
}
