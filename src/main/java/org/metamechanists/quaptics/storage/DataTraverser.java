package org.metamechanists.quaptics.storage;

import com.google.gson.JsonObject;
import io.github.bakedlibs.dough.data.persistent.PersistentDataAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataHolder;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.CustomID;

public class DataTraverser {
    private final PersistentDataHolder persistentDataHolder;
    @Getter
    private final JsonObject data;

    public DataTraverser(CustomID ID) {
        this.persistentDataHolder = Bukkit.getEntity(ID.getUUID());
        final JsonObject data = PersistentDataAPI.getJsonObject(persistentDataHolder, Keys.DATA);
        this.data = data == null
                ? new JsonObject()
                : data;
    }

    public void save() {
        PersistentDataAPI.setJsonObject(persistentDataHolder, Keys.DATA, data);
    }
}
