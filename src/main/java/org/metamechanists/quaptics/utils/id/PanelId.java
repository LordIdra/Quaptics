package org.metamechanists.quaptics.utils.id;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.panel.Panel;

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
        final DisplayGroup displayGroup = DisplayGroup.fromUUID(getUUID());
        return displayGroup != null
                ? Optional.of(new Panel(this))
                : Optional.empty();
    }
}
