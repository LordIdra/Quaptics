package org.metamechanists.quaptics.utils.interfaces;

import org.bukkit.block.Block;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

public interface ConnectionPointBlock {
    ConnectionPointID getPointId(Block block);
}
