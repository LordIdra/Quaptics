package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.panel.Panel;

import java.util.UUID;

@SuppressWarnings("unused")
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
    @Override
    public @Nullable Panel get() {
        return DisplayGroup.fromUUID(getUUID()) != null
                ? new Panel(this)
                : null;
    }
}
