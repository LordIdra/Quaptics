package org.metamechanists.quaptics.storage.scheduler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.panels.PointPanel;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.HashMap;
import java.util.Map;

public final class PointPanelUpdateScheduler {
    private static final Map<PanelId, ConnectionPointId> panelsToTick = new HashMap<>();

    private PointPanelUpdateScheduler() {}

    public static void scheduleUpdate(final @Nullable PanelId panelId, final @NotNull ConnectionPointId pointId) {
        if (panelId != null) {
            panelsToTick.put(panelId, pointId);
        }
    }

    public static void tick() {
        panelsToTick.forEach((panelId, pointId) -> new PointPanel(panelId, pointId).update());
        panelsToTick.clear();
    }
}
