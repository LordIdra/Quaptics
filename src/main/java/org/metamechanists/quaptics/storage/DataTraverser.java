package org.metamechanists.quaptics.storage;

import com.google.gson.JsonObject;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.CustomId;

import java.util.Objects;
import java.util.Optional;

public class DataTraverser {
    private final PersistentDataHolder persistentDataHolder;
    @Getter
    private final JsonObject data;

    public DataTraverser(final @NotNull CustomId id) {
        this.persistentDataHolder = Bukkit.getEntity(id.getUUID());

        final JsonObject newData = PersistentDataAPI.getJsonObject(Objects.requireNonNull(persistentDataHolder), Keys.DATA);
        this.data = Optional.ofNullable(newData).orElseGet(JsonObject::new);
    }

    public void save() {
        PersistentDataAPI.setJsonObject(persistentDataHolder, Keys.DATA, data);
    }
}
