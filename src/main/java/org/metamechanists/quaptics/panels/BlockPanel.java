package org.metamechanists.quaptics.panels;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

public abstract class BlockPanel extends Panel {
    protected static final float SIZE = 0.40F;
    private final ConnectionGroupId groupId;

    protected BlockPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location);
        this.groupId = groupId;
        setPanelHidden(false);
    }

    protected BlockPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
        super(panelId);
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
