package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.jetbrains.annotations.Nullable;

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
    @Override
    public @Nullable DisplayGroup get() {
        return DisplayGroup.fromUUID(getUUID()) != null
                ? DisplayGroup.fromUUID(getUUID())
                : null;
    }
}
