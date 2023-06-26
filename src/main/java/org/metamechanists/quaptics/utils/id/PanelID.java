package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class PanelID extends CustomID {
    public PanelID() {
        super();
    }
    public PanelID(String string) {
        super(string);
    }
    public PanelID(UUID uuid) {
        super(uuid);
    }

    public static PanelID deserialize(Map<String, Object> map) {
        return new PanelID(UUID.fromString((String) map.get("UUID")));
    }
}
