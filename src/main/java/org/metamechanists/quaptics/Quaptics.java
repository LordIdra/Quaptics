package org.metamechanists.quaptics;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.metalib.bstats.bukkit.Metrics;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.ModuleClickListener;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.player.ExperienceMultiplicationModuleListener;
import org.metamechanists.quaptics.implementation.burnout.BurnoutManager;
import org.metamechanists.quaptics.implementation.blocks.consumers.launchpad.LaunchpadListener;
import org.metamechanists.quaptics.implementation.tools.multiblockwand.MultiblockWandListener;
import org.metamechanists.quaptics.implementation.tools.targetingwand.TargetingWandListener;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.panels.config.ConfigPanelListener;
import org.metamechanists.quaptics.panels.info.implementation.PointInfoPanelListener;
import org.metamechanists.quaptics.storage.CacheGarbageCollector;
import org.metamechanists.quaptics.storage.QuapticStorage;
import org.metamechanists.quaptics.storage.QuapticTicker;


public final class Quaptics extends JavaPlugin implements SlimefunAddon {
    private static final int BSTATS_ID = 18956;
    @Getter
    private static Quaptics instance;

    private void initializeListeners() {
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new TargetingWandListener(), this);
        pluginManager.registerEvents(new PointInfoPanelListener(), this);
        pluginManager.registerEvents(new LaunchpadListener(), this);
        pluginManager.registerEvents(new ConfigPanelListener(), this);
        pluginManager.registerEvents(new BurnoutManager(), this);
        pluginManager.registerEvents(new MultiblockWandListener(), this);
        pluginManager.registerEvents(new ModuleClickListener(), this);
        pluginManager.registerEvents(new QuapticStorage(), this);
        pluginManager.registerEvents(new ExperienceMultiplicationModuleListener(), this);
    }
    private void initializeRunnables() {
        new QuapticTicker().runTaskTimer(this, 0, QuapticTicker.INTERVAL_TICKS);
        new CacheGarbageCollector().runTaskTimer(this, CacheGarbageCollector.INTERVAL_TICKS, CacheGarbageCollector.INTERVAL_TICKS);
    }
    private void initializeCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
    }

    @Override
    public void onEnable() {
        instance = this;
        Groups.initialize();
        Items.initialize();

        initializeListeners();
        initializeRunnables();
        initializeCommands();

        new Metrics(this, BSTATS_ID);
    }
    @Override
    public void onDisable() {
        BurnoutManager.stopBurnouts();
    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }
    @Override
    public @Nullable String getBugTrackerURL() {
        return null;
    }
}
