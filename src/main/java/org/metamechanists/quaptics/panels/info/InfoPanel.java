package org.metamechanists.quaptics.panels.info;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

public abstract class InfoPanel {
    protected final InfoPanelContainer container;

    protected InfoPanel(@NotNull final Location location) {
        this.container = buildPanelContainer(location);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected InfoPanel(@NotNull final InfoPanelId panelId) {
        this.container = panelId.get().get();
    }

    public InfoPanelId getId() {
        return container.getId();
    }

    public void changeLocation(final @NotNull Location location) {
        container.changeLocation(location.clone().add(getOffset()));
    }

    public boolean isPanelHidden() {
        return container.isHidden();
    }

    public void setPanelHidden(final boolean hidden) {
        if (container.isHidden() != hidden) {
            container.setHidden(hidden);
            update();
        }
    }

    public void togglePanelHidden() {
        container.toggleHidden();
        update();
    }

    public void remove() {
        container.remove();
    }

    protected abstract Vector getOffset();

    protected abstract InfoPanelContainer buildPanelContainer(@NotNull final Location location);

    public abstract void update();
}
