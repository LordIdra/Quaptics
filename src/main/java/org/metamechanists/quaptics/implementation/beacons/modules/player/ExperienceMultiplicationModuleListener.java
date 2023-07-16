package org.metamechanists.quaptics.implementation.beacons.modules.player;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ExperienceMultiplicationModuleListener implements Listener {
    private static final Map<UUID, Double> affectedPlayers = new HashMap<>();

    @EventHandler
    public static void onExperiencePickup(final @NotNull PlayerPickupExperienceEvent event) {
        final UUID uuid = event.getPlayer().getUniqueId();
        if (affectedPlayers.containsKey(uuid)) {
            final ExperienceOrb experienceOrb = event.getExperienceOrb();
            experienceOrb.setExperience((int) (experienceOrb.getExperience() * affectedPlayers.get(uuid)));
        }
    }

    public static void clearCache() {
        affectedPlayers.clear();
    }
    public static void add(final @NotNull Player player, final double multiplier) {
        affectedPlayers.put(player.getUniqueId(), multiplier);
    }
}
