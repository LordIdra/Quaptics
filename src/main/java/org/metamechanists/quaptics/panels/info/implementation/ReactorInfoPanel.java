package org.metamechanists.quaptics.panels.info.implementation;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.multiblocks.entangler.EntanglementMagnet;
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
                .addAttribute("efficiencyBar", false)
                .addAttribute("powerInputText", false)
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

        final double secondsSinceStarted = BlockStorageAPI.getDouble(location.get(), Keys.BS_SECONDS_SINCE_REACTOR_STARTED);
        final double maxSeconds = group.get().getBlock().getSettings().getTimeToMaxEfficiency();

        final double inputPower = ReactorController.getTotalInputPower(location.get());
        final double maxInputPower = ReactorController.RING_LOCATIONS.size() * EntanglementMagnet.ENTANGLEMENT_MAGNET_SETTINGS.getTier().maxPower * 2;

        container.setText("efficiency", Lore.progressBar(secondsSinceStarted, maxSeconds, "&e", "&7", "&a"));
        container.setText("powerInputText", (inputPower >= maxInputPower ? "&a" : "&e") + inputPower + "&7 / " + "&a" + maxInputPower);
        // TODO output power
    }
}
