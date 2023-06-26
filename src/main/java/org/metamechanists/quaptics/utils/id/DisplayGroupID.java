package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

@SuppressWarnings("unused")
public class DisplayGroupID extends CustomID {
    public DisplayGroupID() {
        super();
    }
    public DisplayGroupID(CustomID ID) {
        super(ID);
    }
    public DisplayGroupID(String string) {
        super(string);
    }
    public DisplayGroupID(UUID uuid) {
        super(uuid);
    }
}
