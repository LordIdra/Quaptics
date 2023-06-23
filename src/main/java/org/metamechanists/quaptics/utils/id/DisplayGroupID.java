package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class DisplayGroupID extends CustomID {
    public DisplayGroupID() {
        super();
    }

    public DisplayGroupID(UUID uuid) {
        super(uuid);
    }

    public static DisplayGroupID deserialize(Map<String, Object> map) {
        return new DisplayGroupID(UUID.fromString((String) map.get("UUID")));
    }
}
