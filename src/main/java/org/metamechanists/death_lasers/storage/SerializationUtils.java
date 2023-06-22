package org.metamechanists.death_lasers.storage;

import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SerializationUtils {

    public static <T, U> Map<String, Object> serializeMap(Map<T, U> input, String keyName, String valueName) {
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

    public static <T, U> Map<T, U> deserializeMap(Map<String, Object> inputMap, String keyName, String valueName) {
        final Map<T, U> outputMap = new HashMap<>();

        for (Map.Entry<String, Object> inputEntry : inputMap.entrySet()) {
            final MemorySection mapEntry = (MemorySection) inputEntry.getValue();
            outputMap.put((T) mapEntry.get(keyName), (U) mapEntry.get(valueName));
        }

        return outputMap;
    }

    public static Map<String, Object> serializeUUID(UUID uuid) {
        final Map<String, Object> map = new HashMap<>();
        map.put("mostSignificant", uuid.getMostSignificantBits());
        map.put("leastSignificant", uuid.getLeastSignificantBits());
        return map;
    }

    public static UUID deserializeUUID(Map<String, Object> map) {
        return new UUID((Long) map.get("mostSignificant"), (Long) map.get("leastSignificant"));
    }
}
