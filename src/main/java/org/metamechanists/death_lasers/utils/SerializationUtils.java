package org.metamechanists.death_lasers.utils;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SerializationUtils {

    public static <T extends ConfigurationSerializable, U extends ConfigurationSerializable> Map<String, Object> serializeMap(Map<T, U> input, String keyName, String valueName) {
        int i = 0;
        final Map<String, Object> output = new HashMap<>();

        for (Map.Entry<T, U> inputEntry : input.entrySet()) {
            final Map<String, Object> outputEntry = new HashMap<>();

            outputEntry.put(keyName, inputEntry.getKey());
            outputEntry.put(valueName, inputEntry.getValue());

            output.put(Objects.toString(i), outputEntry);
            i++;
        }
        return output;
    }

    public static <T extends ConfigurationSerializable, U extends ConfigurationSerializable> Map<T, U> deserializeMap(
            Map<String, Object> inputMap, String keyName, String valueName) {
        final Map<T, U> outputMap = new HashMap<>();

        for (Map.Entry<String, Object> inputEntry : inputMap.entrySet()) {
            final Map<String, Object> mapEntry = (Map<String, Object>) inputEntry.getValue();
            outputMap.put((T) mapEntry.get(keyName), (U) mapEntry.get(valueName));
        }
        return outputMap;
    }
}
