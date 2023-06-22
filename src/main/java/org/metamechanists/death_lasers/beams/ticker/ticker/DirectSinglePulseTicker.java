package org.metamechanists.death_lasers.beams.ticker.ticker;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.id.BlockDisplayID;

import java.util.HashMap;
import java.util.Map;

public class DirectSinglePulseTicker implements LaserBlockDisplayTicker, ConfigurationSerializable {
    private final BlockDisplayID displayID;

    public DirectSinglePulseTicker(Material material, Location source, Location target, float size) {
        final float scale = size * 0.095F;
        final Location midpoint = source.clone().add(target).multiply(0.5);
        final Vector3f scaleVector = new Vector3f(scale, scale, (float)(source.distance(target)));
        this.displayID = new BlockDisplayID(
                DisplayUtils.spawnBlockDisplay(midpoint, material, DisplayUtils.faceTargetTransformation(midpoint, target, scaleVector)).getUniqueId());
        getDisplay().setBrightness(new Display.Brightness(15, 15));
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

