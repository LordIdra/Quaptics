package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public class TextDisplayID extends CustomID {
    public TextDisplayID() {
        super();
    }
    public TextDisplayID(CustomID ID) {
        super(ID);
    }
    public TextDisplayID(String string) {
        super(string);
    }
    public TextDisplayID(UUID uuid) {
        super(uuid);
    }
}
