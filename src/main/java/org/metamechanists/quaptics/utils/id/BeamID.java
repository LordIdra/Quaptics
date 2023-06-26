package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class BeamID extends CustomID {
    public BeamID() {
        super();
    }
    public BeamID(String string) {
        super(string);
    }
    public BeamID(UUID uuid) {
        super(uuid);
    }

    public static BeamID deserialize(Map<String, Object> map) {
        return new BeamID(UUID.fromString((String) map.get("UUID")));
    }
}
