package org.metamechanists.quaptics.panels.config;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

public abstract class BlockConfigPanel extends ConfigPanel {
    protected static final float SIZE = 0.40F;
    private final ConnectionGroupId groupId;

    protected BlockConfigPanel(@NotNull final Location location, final ConnectionGroupId groupId, final float rotationY) {
        super(location, rotationY);
        this.groupId = groupId;
        setPanelHidden(false);
    }

    protected BlockConfigPanel(@NotNull final ConfigPanelId id, final ConnectionGroupId groupId) {
        super(id);
        this.groupId = groupId;
    }

    protected Optional<ConnectionGroup> getGroup() {
        return groupId.get();
    }

    @Override
    protected Vector getOffset() {
        return new Vector(0.0, 0.0, 0.0);
    }
}
