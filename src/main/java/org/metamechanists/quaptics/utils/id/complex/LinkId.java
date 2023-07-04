package org.metamechanists.quaptics.utils.id.complex;

import org.bukkit.Bukkit;
import org.bukkit.entity.Interaction;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.storage.QuapticCache;
import org.metamechanists.quaptics.utils.id.ComplexCustomId;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class LinkId extends ComplexCustomId {
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
    public boolean isValid() {
        return Bukkit.getEntity(getUUID()) instanceof Interaction;
    }
    @Override
    public Optional<Link> get() {
        return isValid()
                ? QuapticCache.getLink(this)
                : Optional.empty();
    }
}
