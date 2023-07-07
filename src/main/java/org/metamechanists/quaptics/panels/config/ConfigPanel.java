package org.metamechanists.quaptics.panels.config;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

public abstract class ConfigPanel {
    protected static final float SIZE = 0.3F;
    protected final ConfigPanelContainer container;
    private final ConnectionGroupId groupId;

    protected ConfigPanel(final ConnectionGroupId groupId, @NotNull final Location location, final float rotationY) {
        this.groupId = groupId;
        this.container = buildPanelContainer(groupId, location, rotationY);
        setPanelHidden(false);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected ConfigPanel(@NotNull final ConfigPanelId id, final ConnectionGroupId groupId) {
        this.container = id.get().get();
        this.groupId = groupId;
    }

    public ConfigPanelId getId() {
        return container.getId();
    }

    protected Optional<ConnectionGroup> getGroup() {
        return groupId.get();
    }

    public void setPanelHidden(final boolean hidden) {
        container.setHidden(hidden);
        update();
    }

    protected boolean isPanelHidden() {
        return container.isHidden();
    }

    public void toggleHidden() {
        container.toggleHidden();
        update();
    }

    public void remove() {
        container.remove();
    }

    protected abstract Vector getOffset();
    protected abstract ConfigPanelContainer buildPanelContainer(@NotNull final ConnectionGroupId groupId, @NotNull final Location location, final float rotationY);
    public abstract void interact(@NotNull final Location location, final String name, final String type);
    public abstract void update();
}
