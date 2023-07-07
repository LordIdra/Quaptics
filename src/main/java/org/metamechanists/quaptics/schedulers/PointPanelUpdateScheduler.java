package org.metamechanists.quaptics.schedulers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.panels.info.implementation.PointInfoPanel;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.HashMap;
import java.util.Map;

public final class PointPanelUpdateScheduler {
    private static final Map<InfoPanelId, ConnectionPointId> panelsToTick = new HashMap<>();

    private PointPanelUpdateScheduler() {}

    public static void scheduleUpdate(final @Nullable InfoPanelId panelId, final @NotNull ConnectionPointId pointId) {
        if (panelId != null) {
            panelsToTick.put(panelId, pointId);
        }
    }

    public static void tick() {
        panelsToTick.forEach((panelId, pointId) -> {
            if (panelId.get().isPresent() && pointId.get().isPresent()) {
                new PointInfoPanel(panelId, pointId).update();
            }
        });
        panelsToTick.clear();
    }
}
