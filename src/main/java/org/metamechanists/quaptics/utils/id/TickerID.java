package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

@SuppressWarnings("unused")
public class TickerID extends CustomID {
    public TickerID() {
        super();
    }
    public TickerID(CustomID ID) {
        super(ID);
    }
    public TickerID(String string) {
        super(string);
    }
    public TickerID(UUID uuid) {
        super(uuid);
    }
}
