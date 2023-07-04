package org.metamechanists.quaptics.implementation.blocks.attachments;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.panels.BlockPanel;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

@FunctionalInterface
public interface PanelBlock {
    default Optional<PanelId> getPanelId(final Location location) {
        final String panelId = BlockStorage.getLocationInfo(location, Keys.BS_PANEL_ID);
        return Optional.ofNullable(panelId).map(PanelId::new);
    }

    default void setPanelId(final Location location, @NotNull final PanelId id) {
        BlockStorage.addBlockInfo(location, Keys.BS_PANEL_ID, id.toString());
    }

    default void updatePanel(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<PanelId> id = getPanelId(location.get());
        id.ifPresent(panelId -> createPanel(panelId, group.getId()).update());
    }

    BlockPanel createPanel(final PanelId panelId, final ConnectionGroupId groupId);
}
