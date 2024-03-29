package org.metamechanists.quaptics.implementation.blocks.consumers.launchpad;

import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.utils.BlockStorageAPI;

public class LaunchpadListener implements Listener {

    @EventHandler
    public static void onPlayerMove(@NotNull final PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) {
            return;
        }

        final Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);

        if (block.getType() != Material.LIGHT_GRAY_CONCRETE) {
            return;
        }

        if (!(BlockStorageAPI.check(block) instanceof final Launchpad launchpad)) {
            return;
        }

        if (!Slimefun.getProtectionManager().hasPermission(event.getPlayer(), event.getPlayer().getLocation(), Interaction.INTERACT_BLOCK)) {
            return;
        }

        launchpad.launch(block.getLocation(), event.getPlayer());
    }
}
