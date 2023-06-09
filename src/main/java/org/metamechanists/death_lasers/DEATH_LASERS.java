package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.death_lasers.implementation.DeathLaserRunnable;
import org.metamechanists.death_lasers.implementation.LaserDisplayStorage;

public final class DEATH_LASERS extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static DEATH_LASERS instance;

    public static void initializeRunnables() {
        new DeathLaserRunnable().runTaskTimer(instance, 0, 1);
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
        LaserDisplayStorage.hardRemoveAllLasers();
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
