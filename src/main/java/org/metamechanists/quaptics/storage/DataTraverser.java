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

    public DataTraverser(CustomID id) {
        this.persistentDataHolder = Bukkit.getEntity(id.get());
        this.data = PersistentDataAPI.getJsonObject(persistentDataHolder, Keys.QUAPTICS_DATA);
    }

    public void save() {
        PersistentDataAPI.setJsonObject(persistentDataHolder, Keys.QUAPTICS_DATA, data);
    }
}
