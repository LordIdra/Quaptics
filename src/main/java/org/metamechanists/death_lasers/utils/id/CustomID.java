package org.metamechanists.death_lasers.utils.id;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

public abstract class CustomID implements ConfigurationSerializable {
    private final UUID uuid;

    public CustomID() {
        this.uuid = UUID.randomUUID();
    }

    public CustomID(String string) {
        this.uuid = UUID.fromString(string);
    }

    protected CustomID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID get() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of("UUID", uuid.toString());
    }
}
