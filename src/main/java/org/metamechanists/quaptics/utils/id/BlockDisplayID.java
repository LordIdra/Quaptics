package org.metamechanists.quaptics.utils.id;

import java.util.Map;
import java.util.UUID;

public class BlockDisplayID extends CustomID {
    public BlockDisplayID() {
        super();
    }
    public BlockDisplayID(String string) {
        super(string);
    }
    public BlockDisplayID(UUID uuid) {
        super(uuid);
    }

    public static BlockDisplayID deserialize(Map<String, Object> map) {
        return new BlockDisplayID(UUID.fromString((String) map.get("UUID")));
    }
}
