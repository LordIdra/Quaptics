package org.metamechanists.death_lasers.storage.v1;

import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class SerializationUtils {

//    public static <T> Map<String, Object> serializeUUIDKeyMap(Map<UUID, T> input, String keyName, String valueName) {
//        final Map<String, T> output = new HashMap<>();
//        input.forEach((key, value) -> output.put(key.toString(), value));
//        return serializeMap(output, keyName, valueName);
//    }
//
//    public static <T> Map<String, Object> serializeUUIDValueMap(Map<T, UUID> input, String keyName, String valueName) {
//        final Map<T, String> output = new HashMap<>();
//        input.forEach((key, value) -> output.put(key, value.toString()));
//        return serializeMap(output, keyName, valueName);
//    }

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

    public static <T, U> Map<T, U> deserializeMap(Map<String, Object> input, String keyName, String valueName) {
        final Map<T, U> outputMap = new HashMap<>();

        for (Map.Entry<String, Object> inputEntry : input.entrySet()) {
            if (inputEntry.getValue() instanceof MemorySection section) {
                outputMap.put((T) section.get(keyName), (U) section.get(valueName));
            } else if (inputEntry.getValue() instanceof LinkedHashMap map) {
                outputMap.put((T) map.get(keyName), (U) map.get(valueName));
            }
        }

        return outputMap;
    }
}
