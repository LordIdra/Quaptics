package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class InteractionID extends CustomID {
    public InteractionID() {
        super();
    }
    public InteractionID(String string) {
        super(string);
    }
    public InteractionID(UUID uuid) {
        super(uuid);
    }

    public static InteractionID deserialize(Map<String, Object> map) {
        return new InteractionID(UUID.fromString((String) map.get("UUID")));
    }
}
