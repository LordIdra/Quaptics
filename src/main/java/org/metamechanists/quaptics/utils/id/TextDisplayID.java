package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class TextDisplayID extends CustomID {
    public TextDisplayID() {
        super();
    }
    public TextDisplayID(String string) {
        super(string);
    }
    public TextDisplayID(UUID uuid) {
        super(uuid);
    }

    public static TextDisplayID deserialize(Map<String, Object> map) {
        return new TextDisplayID(UUID.fromString((String) map.get("UUID")));
    }
}
