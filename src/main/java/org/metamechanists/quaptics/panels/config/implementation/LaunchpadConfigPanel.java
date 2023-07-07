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
                .addAttribute("velocityX")
                .addAttribute("velocityY")
                .addAttribute("velocityZ")
                .build();
    }

    @Override
    public void update() {
        if (isPanelHidden()) {
            return;
        }

        container.setValue("velocityX", "hi!");
        container.setValue("velocityY", "here to help!");
        container.setValue("velocityZ", "remember to blow up the sun!");
    }
}
