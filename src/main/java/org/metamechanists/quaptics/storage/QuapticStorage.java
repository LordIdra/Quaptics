package org.metamechanists.quaptics.storage;

import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class QuapticStorage {
    public static final Set<ConnectionGroupID> groupIDs = new HashSet<>();

    public static void addGroup(ConnectionGroupID groupID) {
        groupIDs.add(groupID);
    }

    public static Set<ConnectionGroupID> getLoadedGroups() {
        return groupIDs.stream()
                .filter(ID -> ID.get() != null)
                .collect(Collectors.toSet());
    }
}
