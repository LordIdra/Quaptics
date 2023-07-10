package org.metamechanists.quaptics.utils.models.models.components;

import org.bukkit.entity.Display;


@FunctionalInterface
public interface ModelComponent {
    Display create();
}
