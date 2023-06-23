package org.metamechanists.quaptics.storage.versions;

import org.bukkit.configuration.file.FileConfiguration;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.storage.SerializationUtils;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.Map;

public class StorageV1 {
    public static final int VERSION = 1;

    public static void write(FileConfiguration data) {
        data.set("groups", SerializationUtils.serializeMap(ConnectionPointStorage.getGroups(),
                "ConnectionGroupID", "ConnectionGroup"));
        data.set("pointIdsToGroupIds", SerializationUtils.serializeMap(ConnectionPointStorage.getPointIdsToGroupIds(),
                "ConnectionPointID", "ConnectionGroupID"));
    }

    public static void read(FileConfiguration data) {
        final Map<ConnectionGroupID, ConnectionGroup> groups = SerializationUtils.deserializeMap(
                data.getConfigurationSection("groups").getValues(true),
                "ConnectionGroupID", "ConnectionGroup");

        final Map<ConnectionPointID, ConnectionGroupID> groupIdsFromPointLocations = SerializationUtils.deserializeMap(
                data.getConfigurationSection("pointIdsToGroupIds").getValues(true),
                "ConnectionPointID", "ConnectionGroupID");

        ConnectionPointStorage.setGroups(groups);
        ConnectionPointStorage.setPointIdsToGroupIds(groupIdsFromPointLocations);
    }
}
