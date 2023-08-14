package org.metamechanists.aircraft.utils.id;

import java.util.UUID;

public abstract class ComplexCustomId extends CustomId {
    protected ComplexCustomId() {
        super();
    }
    protected ComplexCustomId(final CustomId id) {
        super(id);
    }
    protected ComplexCustomId(final String uuid) {
        super(uuid);
    }
    protected ComplexCustomId(final UUID uuid) {
        super(uuid);
    }

    public abstract boolean isValid();
}
