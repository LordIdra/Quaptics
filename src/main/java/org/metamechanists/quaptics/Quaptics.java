package org.metamechanists.quaptics;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.panels.PointPanelListener;
import org.metamechanists.quaptics.implementation.tools.TargetingWandListener;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.utils.Language;

public final class Quaptics extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static Quaptics instance;

    private void initializeListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new TargetingWandListener(), this);
        pluginManager.registerEvents(new PointPanelListener(), this);
    }

    public void initializeRunnables() {
        new QuapticTicker().runTaskTimer(instance, 0, QuapticTicker.INTERVAl_TICKS);
    }

    public void initializeCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
    }

    @Override
    public void onEnable() {
        instance = this;
        Language.initialize();
        Groups.initialize();
        Items.initialize();
        initializeListeners();
        initializeRunnables();
        initializeCommands();
    }

    @Override
    public void onDisable() {

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
