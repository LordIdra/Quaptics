package org.metamechanists.death_lasers.utils.builders;

import org.bukkit.Location;
import org.bukkit.entity.Interaction;

public class InteractionBuilder {
    private final Location location;
    private Float width;
    private Float height;

    public InteractionBuilder(Location location) {
        this.location = location;
    }
    public Interaction build() {
        return location.getWorld().spawn(location, Interaction.class, interaction -> {
            if (width != null) {
                interaction.setInteractionWidth(width);
            }
            if (height != null) {
                interaction.setInteractionHeight(height);
            }
        });
    }

    public InteractionBuilder setWidth(float width) {
        this.width = width;
        return this;
    }
    public InteractionBuilder setHeight(float height) {
        this.height = height;
        return this;
    }
}
