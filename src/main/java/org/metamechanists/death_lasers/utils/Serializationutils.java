package org.metamechanists.death_lasers.utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Serializationutils {

    public static <T extends ConfigurationSerializable, U extends ConfigurationSerializable> void serializeMap(
            ConfigurationSection mapSection, String keyName, String valueName, Map<T, U> map) {
        int i = 0;
        for (Map.Entry<T, U> entry : map.entrySet()) {
            ConfigurationSection section = mapSection.createSection(Objects.toString(i));
            section.set(keyName, entry.getKey());
            section.set(valueName, entry.getValue());
            i++;
        }
    }

    public static <T extends ConfigurationSerializable, U extends ConfigurationSerializable> Map<T, U> deserializeMap(
            ConfigurationSection mapSection, String keyName, String valueName) {
        Map<T, U> map = new HashMap<>();
        for (String sectionName : mapSection.getKeys(false)) {
            ConfigurationSection section = mapSection.getConfigurationSection(sectionName);
            map.put((T) section.get(keyName), (U) section.get(valueName));
        }
        return map;
    }
}
