package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.groups.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class Items {
    @Getter
    private static final Map<String, ConnectedBlock> blocks = new LinkedHashMap<>();

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        Guide.initialize();
        Tools.initialize();
        Primitive.initialize();
        Basic.initialize();
        Intermediate.initialize();
        Advanced.initialize();

        Slimefun.getRegistry().getAllSlimefunItems().stream()
                .filter(ConnectedBlock.class::isInstance)
                .map(ConnectedBlock.class::cast)
                .forEach(connectedBlock -> blocks.put(connectedBlock.getId(), connectedBlock));
    }
}
