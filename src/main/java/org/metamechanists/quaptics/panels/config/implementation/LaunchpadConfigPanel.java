package org.metamechanists.quaptics.panels.config.implementation;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.panels.config.BlockConfigPanel;
import org.metamechanists.quaptics.panels.config.ConfigPanelBuilder;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

public class LaunchpadConfigPanel extends BlockConfigPanel {

    public LaunchpadConfigPanel(@NotNull final Location location, final ConnectionGroupId groupId, final float rotationY) {
        super(location, groupId, rotationY);
    }

    public LaunchpadConfigPanel(@NotNull final ConfigPanelId id, final ConnectionGroupId groupId) {
        super(id, groupId);
    }

    @Override
    protected ConfigPanelContainer buildPanelContainer(@NotNull final Location location, final float rotationY) {
        return new ConfigPanelBuilder(location.clone().add(getOffset()), SIZE, rotationY)
                .addAttribute("velocityX", "&fVelocity (x)")
                .addAttribute("velocityY", "&fVelocity (y)")
                .addAttribute("velocityZ", "&fVelocity (z)")
                .build();
    }

    @Override
    public void update() {
        if (isPanelHidden()) {
            return;
        }

        container.setValue("velocityX", "0.35");
        container.setValue("velocityY", "0.00");
        container.setValue("velocityZ", "||||||||||");
    }
}
