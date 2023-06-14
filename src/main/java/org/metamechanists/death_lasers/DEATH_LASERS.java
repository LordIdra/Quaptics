package org.metamechanists.death_lasers;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.death_lasers.commands.DeathLaserCommand;
import org.metamechanists.death_lasers.storage.beams.BeamStorage;
import org.metamechanists.death_lasers.storage.beams.BeamStorageRunnable;
import org.metamechanists.death_lasers.utils.Language;

public final class DEATH_LASERS extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static DEATH_LASERS instance;

    private void initializeCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.registerCommand(new DeathLaserCommand());
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
        initializeCommands();
        initializeRunnables();
    }

    @Override
    public void onDisable() {
        BeamStorage.hardRemoveAllBeamGroups();
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
