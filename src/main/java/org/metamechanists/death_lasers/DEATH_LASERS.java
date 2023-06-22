package org.metamechanists.death_lasers;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.death_lasers.beams.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.beams.ticker.ticker.DirectSinglePulseTicker;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.info.ConnectionInfoDisplay;
import org.metamechanists.death_lasers.connections.info.PointInformationListener;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.tools.TargetingWandListener;
import org.metamechanists.death_lasers.items.Groups;
import org.metamechanists.death_lasers.items.Items;
import org.metamechanists.death_lasers.storage.StorageLoad;
import org.metamechanists.death_lasers.storage.StorageSave;
import org.metamechanists.death_lasers.utils.Language;

public final class DEATH_LASERS extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static DEATH_LASERS instance;

    private void initializeSerializables() {
        ConfigurationSerialization.registerClass(ConnectionGroup.class, "ConnectionGroup");
        ConfigurationSerialization.registerClass(ConnectionPointOutput.class, "ConnectionPointOutput");
        ConfigurationSerialization.registerClass(ConnectionPointInput.class, "ConnectionPointInput");
        ConfigurationSerialization.registerClass(ConnectionInfoDisplay.class, "ConnectionInfoDisplay");
        ConfigurationSerialization.registerClass(Link.class, "Link");
        ConfigurationSerialization.registerClass(DirectBlockDisplayBeam.class, "DirectBlockDisplayBeam");
        ConfigurationSerialization.registerClass(DirectSinglePulseTicker.class, "DirectSinglePulseTicker");
    }

    private void initializeListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new TargetingWandListener(), this);
        pluginManager.registerEvents(new PointInformationListener(), this);
    }

    public void initializeRunnables() {
        new LaserTicker().runTaskTimer(instance, 0, LaserTicker.INTERVAl_TICKS);
        new StorageSave().runTaskTimer(instance, StorageSave.INTERVAl_TICKS, StorageSave.INTERVAl_TICKS);
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
        initializeSerializables();
        StorageLoad.load();
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
