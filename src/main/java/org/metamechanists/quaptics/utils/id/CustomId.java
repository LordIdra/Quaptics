package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public abstract class CustomId {
    private final UUID uuid;

    protected CustomId() {
        this.uuid = UUID.randomUUID();
    }
    protected CustomId(String string) {
        this.uuid = UUID.fromString(string);
    }
    protected CustomId(CustomId id) {
        this.uuid = id.uuid;
    }
    protected CustomId(UUID uuid) {
        this.uuid = uuid;
    }

    public abstract Object get();

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof CustomId customID)) {
            return false;
        }

        return this.uuid.equals(customID.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
