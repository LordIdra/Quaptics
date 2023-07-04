package org.metamechanists.quaptics.utils.id;

import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.storage.QuapticCache;

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
        return new InteractionId(getUUID()).get().isPresent()
                ? QuapticCache.getLink(this)
                : Optional.empty();
    }
}
