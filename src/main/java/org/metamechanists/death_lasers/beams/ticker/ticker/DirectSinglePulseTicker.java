package org.metamechanists.death_lasers.beams.ticker.ticker;

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

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker, ConfigurationSerializable {
    private final BlockDisplayID displayID;

    public DirectSinglePulseTicker(Material material, Location source, Location target, float size) {
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scale = new Vector3f(size * 0.095F, size * 0.095F, (float)(source.distance(target)));
        this.displayID = new BlockDisplayID(new BlockDisplayBuilder(midpoint)
                        .setMaterial(material)
                        .setTransformation(Transformations.lookAlong(scale, Transformations.getDirection(midpoint, target)))
                        .setBrightness(15)
                        .build()
                        .getUniqueId());
    }

    private DirectSinglePulseTicker(BlockDisplayID displayID) {
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

    public static DirectSinglePulseTicker deserialize(Map<String, Object> map) {
        return new DirectSinglePulseTicker((BlockDisplayID) map.get("display"));
    }
}

