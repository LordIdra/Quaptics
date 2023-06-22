package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.storage.v1.SerializationUtils;
import org.metamechanists.death_lasers.utils.DisplayUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker, ConfigurationSerializable {
    private final UUID display;

    public DirectSinglePulseTicker(Material material, Location source, Location target, float size) {
        final float scale = size * 0.095F;
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scaleVector = new Vector3f(scale, scale, (float)(source.distance(target)));
        this.display = DisplayUtils.spawnBlockDisplay(midpoint, material, DisplayUtils.faceTargetTransformation(midpoint, target, scaleVector)).getUniqueId();
        getDisplay().setBrightness(new Display.Brightness(15, 15));
    }

    private DirectSinglePulseTicker(UUID display) {
        this.display = display;
    }

    private BlockDisplay getDisplay() {
        return (BlockDisplay) Bukkit.getEntity(display);
    };

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
        map.put("display", SerializationUtils.serializeUUID(getDisplay().getUniqueId()));
        return map;
    }

    public static DirectSinglePulseTicker deserialize(Map<String, Object> map) {
        return new DirectSinglePulseTicker(SerializationUtils.deserializeUUID((Map<String, Object>) map.get("display")));
    }
}

