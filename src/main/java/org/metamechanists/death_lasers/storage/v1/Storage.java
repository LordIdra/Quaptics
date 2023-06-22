package org.metamechanists.death_lasers.storage.v1;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;

import java.util.Map;

public class Storage {
    public static final String FILE_NAME = "data_DO_NOT_EDIT.yml";
    public static final int VERSION = 1;

    public static void write(FileConfiguration data) {
        data.set("groups", SerializationUtils.serializeMap(ConnectionPointStorage.getGroups(),
                "Location", "ConnectionGroup"));
        data.set("groupIdsFromPointLocations", SerializationUtils.serializeMap(ConnectionPointStorage.getGroupIdsFromPointLocations(),
                "GroupLocation", "PointLocation"));
    }

    public static void read(FileConfiguration data) {
        final Map<Location, ConnectionGroup> groups = SerializationUtils.deserializeMap(
                data.getConfigurationSection("groups").getValues(true),
                "Location", "ConnectionGroup");

        final Map<Location, Location> groupIdsFromPointLocations = SerializationUtils.deserializeMap(
                data.getConfigurationSection("groupIdsFromPointLocations").getValues(true),
                "GroupLocation", "PointLocation");

        ConnectionPointStorage.setGroups(groups);
        ConnectionPointStorage.setGroupIdsFromPointLocations(groupIdsFromPointLocations);
    }
}
