package org.metamechanists.quaptics.utils.id;

import java.util.UUID;

public abstract class CustomID {
    private final UUID uuid;

    public CustomID() {
        this.uuid = UUID.randomUUID();
    }
    public CustomID(String string) {
        this.uuid = UUID.fromString(string);
    }
    protected CustomID(CustomID ID) {
        this.uuid = ID.uuid;
    }
    protected CustomID(UUID uuid) {
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

        if (!(other instanceof CustomID customID)) {
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
