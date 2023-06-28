package org.metamechanists.quaptics.utils.interfaces;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import org.bukkit.block.Block;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public interface ConnectionPointBlock {
    ConnectionPointID getPointId(Block block);

    default BlockUseHandler onUse() {
        return event -> {
            final Block block = event.getClickedBlock().orElse(null);
            if (block == null) {
                return;
            }

            final ConnectionPoint connectionPoint = ConnectionPoint.fromID(getPointId(block));
            if (connectionPoint == null) {
                return;
            }

            connectionPoint.togglePanelVisibility();
        };
    }
}
