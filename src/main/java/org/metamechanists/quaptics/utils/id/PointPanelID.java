package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class PointPanelID extends CustomID {
    public PointPanelID() {
        super();
    }
    public PointPanelID(String string) {
        super(string);
    }
    public PointPanelID(UUID uuid) {
        super(uuid);
    }

    public static PointPanelID deserialize(Map<String, Object> map) {
        return new PointPanelID(UUID.fromString((String) map.get("UUID")));
    }
}
