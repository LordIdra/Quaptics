package org.metamechanists.quaptics.panels.info;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Optional;

public abstract class BlockInfoPanel extends InfoPanel {
    protected static final float SIZE = 0.40F;
    private final ConnectionGroupId groupId;

    protected BlockInfoPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location);
        this.groupId = groupId;
        setPanelHidden(false);
    }

    protected BlockInfoPanel(@NotNull final InfoPanelId id, final ConnectionGroupId groupId) {
        super(id);
        this.groupId = groupId;
    }

    protected Optional<ConnectionGroup> getGroup() {
        return groupId.get();
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected Vector getOffset() {
        return new Vector(0.0, 0.7, 0.0);
    }
}
