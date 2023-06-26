package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

@SuppressWarnings("unused")
public class InteractionID extends CustomID {
    public InteractionID() {
        super();
    }
    public InteractionID(CustomID ID) {
        super(ID);
    }
    public InteractionID(String string) {
        super(string);
    }
    public InteractionID(UUID uuid) {
        super(uuid);
    }
}
