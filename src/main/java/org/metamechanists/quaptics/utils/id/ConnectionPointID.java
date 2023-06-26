package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public class ConnectionPointID extends CustomID {
    public ConnectionPointID() {
        super();
    }
    public ConnectionPointID(CustomID ID) {
        super(ID);
    }
    public ConnectionPointID(String string) {
        super(string);
    }
    public ConnectionPointID(UUID uuid) {
        super(uuid);
    }
}
