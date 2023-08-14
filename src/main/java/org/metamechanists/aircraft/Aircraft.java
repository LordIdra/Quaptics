package org.metamechanists.aircraft;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.aircraft.items.Groups;


public final class Aircraft extends JavaPlugin implements SlimefunAddon {
    @Getter
    private static Aircraft instance;

    @Override
    public void onEnable() {
        getLogger().info("brrrrrrrrrrrrrrrrrrrrrrrr");
        instance = this;
        Groups.initialize();
    }
    @Override
    public void onDisable() {

    }

    @Override
    public @NotNull JavaPlugin getJavaPlugin() {
        return this;
    }
    @Override
    public @NotNull String getBugTrackerURL() {
        return "https://github.com/metamechanists/Quaptics/issues";
    }
}
