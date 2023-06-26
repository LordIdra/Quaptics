package org.metamechanists.quaptics.storage;

import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.Set;
import java.util.stream.Collectors;

public class QuapticStorage {
    public static Set<ConnectionGroupID> groupIDs;

    public static void addGroup(ConnectionGroupID groupID) {
        groupIDs.add(groupID);
    }

    public static Set<ConnectionGroupID> getLoadedGroups() {
        final Set<ConnectionGroupID> loadedGroups = groupIDs.stream()
                .filter(ID -> ConnectionGroup.fromID(ID) != null)
                .collect(Collectors.toSet());
        return loadedGroups;
    }
}
