package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class ConnectionGroupID extends CustomID {
    public ConnectionGroupID() {
        super();
    }
    public ConnectionGroupID(String string) {
        super(string);
    }
    public ConnectionGroupID(UUID uuid) {
        super(uuid);
    }

    public static ConnectionGroupID deserialize(Map<String, Object> map) {
        return new ConnectionGroupID(UUID.fromString((String) map.get("UUID")));
    }
}
