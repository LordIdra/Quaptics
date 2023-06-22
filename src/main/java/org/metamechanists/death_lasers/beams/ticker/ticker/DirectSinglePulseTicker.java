package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.DEATH_LASERS;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.SerializationUtils;

import java.util.HashMap;
import java.util.Map;

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker, ConfigurationSerializable {
    private final BlockDisplay display;

    public DirectSinglePulseTicker(Material material, Location source, Location target, float size) {
        final float scale = size * 0.095F;
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scaleVector = new Vector3f(scale, scale, (float)(source.distance(target)));
        this.display = DisplayUtils.spawnBlockDisplay(midpoint, material, DisplayUtils.faceTargetTransformation(midpoint, target, scaleVector));
        display.setBrightness(new Display.Brightness(15, 15));
    }

    private DirectSinglePulseTicker(BlockDisplay display) {
        this.display = display;
    }

    @Override
    public void tick() {}

    @Override
    public void remove() {
        display.remove();
    }

    @Override
    public boolean expired() {
        return true;
    }

    public @NotNull Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("world", SerializationUtils.serializeUUID(display.getWorld().getUID()));
        map.put("display", SerializationUtils.serializeUUID(display.getUniqueId()));
        return map;
    }

    public static DirectSinglePulseTicker deserialize(Map<String, Object> map) {
        final World world = DEATH_LASERS.getInstance().getServer().getWorld(SerializationUtils.deserializeUUID((Map<String, Object>) map.get("world")));
        final BlockDisplay display = (BlockDisplay) world.getEntity(SerializationUtils.deserializeUUID((Map<String, Object>) map.get("display")));
        return new DirectSinglePulseTicker(display);
    }
}

