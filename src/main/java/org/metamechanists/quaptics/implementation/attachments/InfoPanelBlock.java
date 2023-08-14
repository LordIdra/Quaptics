package org.metamechanists.quaptics.implementation.attachments;

import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Optional;

public interface InfoPanelBlock {
    static Optional<InfoPanelId> getPanelId(final Location location) {
        return BlockStorageAPI.getInfoPanelId(location, Keys.BS_PANEL_ID);
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
        id.ifPresent(panelId -> getPanel(panelId, group.getId()).update());
    }

    default void setPanelHidden(@NotNull final ConnectionGroup group, final boolean hidden) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<InfoPanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> getPanel(panelId, group.getId()).setPanelHidden(hidden));
    }

    default void onPlaceInfoPanelBlock(@NotNull final BlockPlaceEvent event) {
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = ConnectedBlock.getGroup(location);
        optionalGroup.ifPresent(group -> setPanelId(location, createPanel(location, optionalGroup.get()).getId()));
    }
    default void onBreakInfoPanelBlock(@NotNull final Location location) {
        getPanelId(location)
                .flatMap(InfoPanelId::get)
                .ifPresent(InfoPanelContainer::remove);
    }

    BlockInfoPanel createPanel(final Location location, final @NotNull ConnectionGroup group);
    BlockInfoPanel getPanel(final InfoPanelId panelId, final ConnectionGroupId groupId);
}
