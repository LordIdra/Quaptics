package org.metamechanists.quaptics.panels.implementation;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.attachments.ProgressBlock;
import org.metamechanists.quaptics.panels.BlockPanel;
import org.metamechanists.quaptics.panels.PanelBuilder;
import org.metamechanists.quaptics.panels.PanelContainer;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

public class ProgressPanel extends BlockPanel {

    public ProgressPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ProgressPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
        super(panelId, groupId);
    }

    @Override
    protected PanelContainer buildPanelContainer(@NotNull final Location location) {
        return new PanelBuilder(location.clone().toCenterLocation().add(getOffset()), SIZE)
                .addAttribute("progressBar", false)
                .build();
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

        panelContainer.setText("progressBar", ProgressBlock.progressBar(group.get()));
    }
}
