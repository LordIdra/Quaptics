package org.metamechanists.quaptics.implementation.blocks.panels;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.Optional;

public abstract class BlockPanel {
    protected static final Vector BLOCK_OFFSET = new Vector(0, 0.7, 0);
    protected static final float SIZE = 0.40F;
    protected final ConnectionGroupId groupId;
    protected final Panel panel;

    protected BlockPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        this.groupId = groupId;
        this.panel = createPanel(location);
        panel.setHidden(false);
    }

    protected BlockPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
        this.groupId = groupId;
        this.panel = panelId.get().get();
    }

    public PanelId getId() {
        return panel.getId();
    }

    protected Optional<ConnectionGroup> getGroup() {
        return groupId.get();
    }

    public void setPanelHidden(final boolean hidden) {
        panel.setHidden(hidden);
    }

    public void remove() {
        panel.remove();
    }

    protected abstract Panel createPanel(@NotNull Location location);
    public abstract void update();
}
