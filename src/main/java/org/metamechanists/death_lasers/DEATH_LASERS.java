package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.death_lasers.lasers.storage.BeamStorage;
import org.metamechanists.death_lasers.lasers.storage.BeamStorageRunnable;

public final class DEATH_LASERS extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static DEATH_LASERS instance;

    public static void initializeRunnables() {
        new BeamStorageRunnable().runTaskTimer(instance, 0, 1);
    }

    @Override
    public void onEnable() {
        instance = this;
        Groups.initialize();
        Items.initialize();
        initializeRunnables();
    }

    @Override
    public void onDisable() {
        BeamStorage.hardRemoveAllBeams();
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }
}
