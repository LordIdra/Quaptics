package org.metamechanists.death_lasers.beams.ticker.ticker;

import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.Transformations;
import org.metamechanists.death_lasers.utils.builders.BlockDisplayBuilder;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;

import java.util.HashMap;
import java.util.Map;

@Builder
public class DirectTicker implements DisplayTicker, ConfigurationSerializable {
    private final BlockDisplayID displayID;

    public DirectTicker(Material material, Location source, Location target, float radius) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scale = new Vector3f(radius, radius, (float)(source.distance(target)));
        this.displayID = new BlockDisplayID(new BlockDisplayBuilder(midpoint)
                        .setMaterial(material)
                        .setTransformation(Transformations.lookAlong(scale, Transformations.getDirection(midpoint, target)))
                        .setBrightness(15)
                        .build()
                        .getUniqueId());
    }

    private DirectTicker(BlockDisplayID displayID) {
        this.displayID = displayID;
    }

    private BlockDisplay getDisplay() {
        return (BlockDisplay) Bukkit.getEntity(displayID.get());
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        getDisplay().remove();
    }

    @Override
    public boolean expired() {
        return true;
    }

    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("display", displayID);
        return map;
    }

    public static DirectTicker deserialize(Map<String, Object> map) {
        return new DirectTicker((BlockDisplayID) map.get("display"));
    }
}

