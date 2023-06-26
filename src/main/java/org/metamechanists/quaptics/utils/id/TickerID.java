package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class TickerID extends CustomID {
    public TickerID() {
        super();
    }
    public TickerID(String string) {
        super(string);
    }
    public TickerID(UUID uuid) {
        super(uuid);
    }

    public static TickerID deserialize(Map<String, Object> map) {
        return new TickerID(UUID.fromString((String) map.get("UUID")));
    }
}
