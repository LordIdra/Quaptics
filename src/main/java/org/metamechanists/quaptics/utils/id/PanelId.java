package org.metamechanists.quaptics.utils.id;

import org.metamechanists.quaptics.panels.Panel;
import org.metamechanists.quaptics.storage.QuapticCache;

import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class PanelId extends CustomId {
    public PanelId() {
        super();
    }
    public PanelId(final CustomId id) {
        super(id);
    }
    public PanelId(final String uuid) {
        super(uuid);
    }
    public PanelId(final UUID uuid) {
        super(uuid);
    }
    @Override
    public Optional<Panel> get() {
        return new InteractionId(this).get().isPresent()
                ? QuapticCache.getPanel(this)
                : Optional.empty();
    }
}
