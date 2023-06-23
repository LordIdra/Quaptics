package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class ConnectionPointID extends CustomID {
    public ConnectionPointID() {
        super();
    }
    public ConnectionPointID(String string) {
        super(string);
    }
    public ConnectionPointID(UUID uuid) {
        super(uuid);
    }

    public static ConnectionPointID deserialize(Map<String, Object> map) {
        return new ConnectionPointID(UUID.fromString((String) map.get("UUID")));
    }
}
