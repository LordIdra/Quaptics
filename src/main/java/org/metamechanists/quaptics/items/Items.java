package org.metamechanists.quaptics.items;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.groups.Advanced;
import org.metamechanists.quaptics.items.groups.Basic;
import org.metamechanists.quaptics.items.groups.Guide;
import org.metamechanists.quaptics.items.groups.Intermediate;
import org.metamechanists.quaptics.items.groups.Primitive;
import org.metamechanists.quaptics.items.groups.Tools;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
@UtilityClass
public class Items {
    @Getter
    private final Map<String, ConnectedBlock> blocks = new LinkedHashMap<>();

    public void initialize() {
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
