package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public class PanelID extends CustomID {
    public PanelID() {
        super();
    }
    public PanelID(CustomID ID) {
        super(ID);
    }
    public PanelID(String string) {
        super(string);
    }
    public PanelID(UUID uuid) {
        super(uuid);
    }
}
