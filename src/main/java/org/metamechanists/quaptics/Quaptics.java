package org.metamechanists.quaptics;

import co.aikar.commands.PaperCommandManager;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.beams.ticker.ticker.DirectTicker;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.info.ConnectionInfoDisplay;
import org.metamechanists.quaptics.connections.info.PointInformationListener;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.tools.TargetingWandListener;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Items;
import org.metamechanists.quaptics.storage.SaveRunnable;
import org.metamechanists.quaptics.storage.Storage;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.id.BlockDisplayID;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.DisplayGroupID;
import org.metamechanists.quaptics.utils.id.InteractionID;

public final class Quaptics extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static Quaptics instance;

    private void initializeSerializables() {
        ConfigurationSerialization.registerClass(BlockDisplayID.class, "BlockDisplayID");
        ConfigurationSerialization.registerClass(ConnectionGroupID.class, "ConnectionGroupID");
        ConfigurationSerialization.registerClass(ConnectionPointID.class, "ConnectionPointID");
        ConfigurationSerialization.registerClass(DisplayGroupID.class, "DisplayGroupID");
        ConfigurationSerialization.registerClass(InteractionID.class, "InteractionID");


        ConfigurationSerialization.registerClass(ConnectionGroup.class, "ConnectionGroup");
        ConfigurationSerialization.registerClass(ConnectionPointOutput.class, "ConnectionPointOutput");
        ConfigurationSerialization.registerClass(ConnectionPointInput.class, "ConnectionPointInput");
        ConfigurationSerialization.registerClass(ConnectionInfoDisplay.class, "ConnectionInfoDisplay");

        ConfigurationSerialization.registerClass(Link.class, "Link");

        ConfigurationSerialization.registerClass(DirectBeam.class, "DirectBlockDisplayBeam");
        ConfigurationSerialization.registerClass(DirectTicker.class, "DirectSinglePulseTicker");
    }

    private void initializeListeners() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new TargetingWandListener(), this);
        pluginManager.registerEvents(new PointInformationListener(), this);
    }

    public void initializeRunnables() {
        new QuapticTicker().runTaskTimer(instance, 0, QuapticTicker.INTERVAl_TICKS);
        new SaveRunnable().runTaskTimer(instance, SaveRunnable.INTERVAL_TICKS, SaveRunnable.INTERVAL_TICKS);
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
        Storage.load();
        initializeListeners();
        initializeRunnables();
        initializeCommands();
    }

    @Override
    public void onDisable() {
        Storage.save();
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
