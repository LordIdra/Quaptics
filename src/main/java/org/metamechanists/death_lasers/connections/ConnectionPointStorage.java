package org.metamechanists.death_lasers.connections;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.utils.id.ConnectionGroupID;
import org.metamechanists.death_lasers.utils.id.ConnectionPointID;

import java.util.HashMap;
import java.util.Map;

public class ConnectionPointStorage {
    @Getter
    @Setter
    private static Map<ConnectionGroupID, ConnectionGroup> groups = new HashMap<>();
    @Getter
    @Setter
    private static Map<ConnectionPointID, ConnectionGroupID> pointIdsToGroupIds = new HashMap<>();


    public static void tick() {
        groups.values().forEach(ConnectionGroup::tick);
    }

    public static void addGroup( ConnectionGroup group) {
        groups.put(group.getId(), group);
        group.getPoints().keySet().forEach(pointID -> pointIdsToGroupIds.put(pointID, group.getId()));
    }
    public static boolean hasGroup(ConnectionGroupID id) {
        return groups.containsKey(id);
    }
    public static ConnectionGroup getGroup(ConnectionGroupID id) {
        return groups.get(id);
    }
    public static ConnectionGroup getGroup(ConnectionPointID id) {
        return groups.get(pointIdsToGroupIds.get(id));
    }

    public static boolean hasPoint(ConnectionPointID id) {
        return pointIdsToGroupIds.containsKey(id);
    }
    public static ConnectionPoint getPoint(ConnectionPointID id) {
        final ConnectionGroupID groupId = pointIdsToGroupIds.get(id);
        final ConnectionGroup group = getGroup(groupId);
        if (group == null) {
            return null;
        }
        return group.getPoint(id);
    }

    public static void removeGroup(ConnectionGroupID id) {
        final ConnectionGroup group = groups.remove(id);
        group.removeAllPoints();
        group.getPoints().forEach(pointIdsToGroupIds::remove);
    }
    public static void updatePointLocation(ConnectionPointID id, Location newLocation) {
        final ConnectionGroupID groupId = pointIdsToGroupIds.get(id);
        final ConnectionGroup group = groups.get(groupId);
        pointIdsToGroupIds.remove(id);
        pointIdsToGroupIds.put(id, groupId);
        group.changePointLocation(id, newLocation);
    }
}
