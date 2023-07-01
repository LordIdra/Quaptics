package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("unused")
public class DisplayGroupId extends CustomId {
    public DisplayGroupId() {
        super();
    }
    public DisplayGroupId(final CustomId id) {
        super(id);
    }
    public DisplayGroupId(final String uuid) {
        super(uuid);
    }
    public DisplayGroupId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public @Nullable DisplayGroup get() {
        return DisplayGroup.fromUUID(getUUID());
    }
}
