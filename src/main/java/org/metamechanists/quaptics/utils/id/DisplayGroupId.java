package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class DisplayGroupId extends CustomId {
    public DisplayGroupId() {
        super();
    }
    public DisplayGroupId(CustomId id) {
        super(id);
    }
    public DisplayGroupId(String string) {
        super(string);
    }
    public DisplayGroupId(UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable DisplayGroup get() {
        return DisplayGroup.fromUUID(getUUID());
    }
}
