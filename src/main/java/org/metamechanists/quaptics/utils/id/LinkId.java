package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.connections.Link;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class LinkId extends CustomId {
    public LinkId() {
        super();
    }
    public LinkId(final CustomId id) {
        super(id);
    }
    public LinkId(final String uuid) {
        super(uuid);
    }
    public LinkId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<Link> get() {
        return DisplayGroup.fromUUID(getUUID()) != null
                ? Optional.of(new Link(this))
                : Optional.empty();
    }
}
