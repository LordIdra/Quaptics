package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public class ConnectionGroupID extends CustomID {
    public ConnectionGroupID() {
        super();
    }
    public ConnectionGroupID(CustomID ID) {
        super(ID);
    }
    public ConnectionGroupID(String string) {
        super(string);
    }
    public ConnectionGroupID(UUID uuid) {
        super(uuid);
    }
}
