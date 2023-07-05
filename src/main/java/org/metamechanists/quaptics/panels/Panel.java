package org.metamechanists.quaptics.panels;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

public abstract class Panel {
    protected final PanelContainer panelContainer;

    protected Panel(@NotNull final Location location) {
        this.panelContainer = buildPanelContainer(location);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    protected Panel(@NotNull final PanelId panelId) {
        this.panelContainer = panelId.get().get();
    }

    public PanelId getId() {
        return panelContainer.getId();
    }

    protected static double roundTo2dp(final double value) {
        return Math.round(value*Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public void changeLocation(final @NotNull Location location) {
        panelContainer.changeLocation(location.clone().add(getOffset()));
    }

    public boolean isPanelHidden() {
        return panelContainer.isHidden();
    }

    public void setPanelHidden(final boolean hidden) {
        panelContainer.setHidden(hidden);
        update();
    }

    public void toggleHidden() {
        panelContainer.toggleHidden();
        update();
    }

    public void remove() {
        panelContainer.remove();
    }

    protected abstract Vector getOffset();

    protected abstract PanelContainer buildPanelContainer(@NotNull final Location location);

    public abstract void update();
}
