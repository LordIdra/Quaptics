package org.metamechanists.quaptics.panels.config.implementation;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.consumers.Launchpad;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.config.ConfigPanel;
import org.metamechanists.quaptics.panels.config.ConfigPanelBuilder;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Optional;

public class LaunchpadConfigPanel extends ConfigPanel {

    public LaunchpadConfigPanel(@NotNull final Location location, final ConnectionGroupId groupId, final float rotationY) {
        super(groupId, location, rotationY);
    }

    public LaunchpadConfigPanel(@NotNull final ConfigPanelId id, final ConnectionGroupId groupId) {
        super(id, groupId);
    }

    @Override
    protected ConfigPanelContainer buildPanelContainer(@NotNull final ConnectionGroupId groupId, @NotNull final Location location, final float rotationY) {
        return new ConfigPanelBuilder(groupId, location.clone().add(getOffset()), SIZE, rotationY)
                .addAttribute("velocityX", "&fVelocity (x)")
                .addAttribute("velocityY", "&fVelocity (y)")
                .addAttribute("velocityZ", "&fVelocity (z)")
                .build();
    }

    @Override
    public void interact(@NotNull final Location location, final String name, final String type) {
        final Optional<Vector> velocity = BlockStorageAPI.getVector(location, Keys.BS_VELOCITY);
        if (velocity.isEmpty()) {
            return;
        }

        final int increment = "add".equals(type) ? 1 : -1;

        velocity.get().add(new Vector(
                "velocityX".equals(name) ? increment : 0,
                "velocityY".equals(name) ? increment : 0,
                "velocityZ".equals(name) ? increment : 0));

        Utils.clampToRange(velocity.get(), -Launchpad.MAX_VELOCITY, Launchpad.MAX_VELOCITY);
        BlockStorageAPI.set(location, Keys.BS_VELOCITY, velocity.get());
        update();
    }

    @Override
    public void update() {
        if (isPanelHidden()) {
            return;
        }

        final Optional<ConnectionGroup> group = getGroup();
        if (group.isEmpty()) {
            return;
        }

        final Optional<Location> location = group.get().getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<Vector> velocity = BlockStorageAPI.getVector(location.get(), Keys.BS_VELOCITY);
        if (velocity.isEmpty()) {
            return;
        }

        container.setValue("velocityX", Lore.twoWayProgressBar(velocity.get().getX(), Launchpad.MAX_VELOCITY, "&c", "&a", "&7"));
        container.setValue("velocityY", Lore.twoWayProgressBar(velocity.get().getY(), Launchpad.MAX_VELOCITY, "&c", "&a", "&7"));
        container.setValue("velocityZ", Lore.twoWayProgressBar(velocity.get().getZ(), Launchpad.MAX_VELOCITY, "&c", "&a", "&7"));
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected Vector getOffset() {
        return new Vector(0.0, 0.2, 0.0);
    }
}
