package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public class LinkID extends CustomID {
    public LinkID() {
        super();
    }
    public LinkID(CustomID ID) {
        super(ID);
    }
    public LinkID(String string) {
        super(string);
    }
    public LinkID(UUID uuid) {
        super(uuid);
    }
}
