package org.metamechanists.quaptics.panels.info.implementation;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.multiblocks.reactor.ReactorController;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.InfoPanelBuilder;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Optional;


public class ReactorInfoPanel extends BlockInfoPanel {
    public ReactorInfoPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ReactorInfoPanel(@NotNull final InfoPanelId panelId, final ConnectionGroupId groupId) {
        super(panelId, groupId);
    }

    @Override
    protected InfoPanelContainer buildPanelContainer(@NotNull final Location location) {
        return new InfoPanelBuilder(location.clone().toCenterLocation().add(getOffset()), SIZE)
                .addAttribute("thresholdBar", false)
                .addAttribute("efficiencyBar", false)
                .addAttribute("powerOutputText", false)
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

        final Optional<Location> location = group.get().getLocation();
        if (location.isEmpty()) {
            return;
        }

        final ConnectedBlock block = group.get().getBlock();
        if (!(block instanceof final ReactorController controller)) {
            return;
        }

        final Settings settings = group.get().getBlock().getSettings();

        final double secondsSinceStarted = BlockStorageAPI.getDouble(location.get(), Keys.BS_SECONDS_SINCE_REACTOR_STARTED);
        final double maxSeconds = group.get().getBlock().getSettings().getTimeToMaxEfficiency();

        final double inputPower = ReactorController.getTotalInputPower(location.get());
        final double minInputPower = settings.getPowerThreshold();

        final double outputPower = BlockStorageAPI.getDouble(location.get(), Keys.BS_OUTPUT_POWER);
        final double maxOutputPower = controller.getMaxOutputPower();

        container.setText("thresholdBar", "&7Threshold " + Lore.thresholdBar(inputPower, minInputPower));
        container.setText("efficiencyBar", "&7Efficiency " + Lore.efficiencyBar(secondsSinceStarted, maxSeconds));
        container.setText("powerOutputText", "&7Output " + Lore.powerOutput(outputPower, maxOutputPower));
    }

    @Override
    protected Vector getOffset() {
        return new Vector(0.0, 0.9, 0.0);
    }
}
