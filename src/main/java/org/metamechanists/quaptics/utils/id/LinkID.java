package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class LinkID extends CustomID {
    public LinkID() {
        super();
    }
    public LinkID(String string) {
        super(string);
    }
    public LinkID(UUID uuid) {
        super(uuid);
    }

    public static LinkID deserialize(Map<String, Object> map) {
        return new LinkID(UUID.fromString((String) map.get("UUID")));
    }
}
