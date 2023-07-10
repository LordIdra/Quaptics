package org.metamechanists.quaptics.utils.models;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.models.components.ModelComponent;

import java.util.HashMap;
import java.util.Map;


/**
 * Builder class that allows you to construct a model using components
 * TODO add link to examples
 */
public class ModelBuilder {
    private final Map<String, ModelComponent> components = new HashMap<>();

    public ModelBuilder add(@NotNull final String name, @NotNull final ModelComponent component) {
        components.put(name, component);
        return this;
    }

    /**
     * Creates all the components and adds them to a displaygroup
     * @param location The center of the model
     * @return The display group containing all the components
     */
    public DisplayGroup build(@NotNull final Location location) {
        final DisplayGroup group = new DisplayGroup(location);
        components.forEach((name, component) -> group.addDisplay(name, component.build(location)));
        return group;
    }
}
