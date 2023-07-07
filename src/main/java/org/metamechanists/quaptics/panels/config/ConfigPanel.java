package org.metamechanists.quaptics.panels.config;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;

public abstract class ConfigPanel {
    protected final ConfigPanelContainer container;

    protected ConfigPanel(@NotNull final Location location) {
        this.container = buildPanelContainer(location);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected ConfigPanel(@NotNull final ConfigPanelId id) {
        this.container = id.get().get();
    }

    public ConfigPanelId getId() {
        return container.getId();
    }

    public void changeLocation(final @NotNull Location location) {
        container.changeLocation(location.clone().add(getOffset()));
    }

    public boolean isPanelHidden() {
        return container.isHidden();
    }

    public void setPanelHidden(final boolean hidden) {
        container.setHidden(hidden);
        update();
    }

    public void toggleHidden() {
        container.toggleHidden();
        update();
    }

    public void remove() {
        container.remove();
    }

    protected abstract Vector getOffset();

    protected abstract ConfigPanelContainer buildPanelContainer(@NotNull final Location location);

    public abstract void update();
}
