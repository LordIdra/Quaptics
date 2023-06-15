package org.metamechanists.death_lasers;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.death_lasers.lasers.BeamStorage;
import org.metamechanists.death_lasers.lasers.BeamStorageRunnable;
import org.metamechanists.death_lasers.connections.ConnectionPointListener;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.utils.Language;

public final class DEATH_LASERS extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static DEATH_LASERS instance;

    private void initializeListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ConnectionPointListener(), this);
    }

    public static void initializeRunnables() {
        new BeamStorageRunnable().runTaskTimer(instance, 0, 1);
    }

    @Override
    public void onEnable() {
        instance = this;
        Language.initialize();
        Groups.initialize();
        Items.initialize();
        initializeListeners();
        initializeRunnables();
    }

    @Override
    public void onDisable() {
        BeamStorage.hardRemoveAllBeamGroups();
        ConnectionPointStorage.removeAllConnectionPoints();
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
