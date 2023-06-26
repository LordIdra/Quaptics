package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class PanelAttributeID extends CustomID {
    public PanelAttributeID() {
        super();
    }
    public PanelAttributeID(String string) {
        super(string);
    }
    public PanelAttributeID(UUID uuid) {
        super(uuid);
    }

    public static PanelAttributeID deserialize(Map<String, Object> map) {
        return new PanelAttributeID(UUID.fromString((String) map.get("UUID")));
    }
}
