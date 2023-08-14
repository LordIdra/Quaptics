package org.metamechanists.aircraft.utils.id.simple;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.metamechanists.aircraft.utils.id.CustomId;

import java.util.Optional;
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
    public Optional<DisplayGroup> get() {
        return Optional.ofNullable(DisplayGroup.fromUUID(getUUID()));
    }
}
