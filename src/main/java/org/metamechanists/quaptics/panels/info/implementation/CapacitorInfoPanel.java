package org.metamechanists.quaptics.panels.info.implementation;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.InfoPanelBuilder;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Optional;

public class CapacitorInfoPanel extends BlockInfoPanel {
    public CapacitorInfoPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public CapacitorInfoPanel(@NotNull final InfoPanelId panelId, final ConnectionGroupId groupId) {
        super(panelId, groupId);
    }

    @Override
    protected InfoPanelContainer buildPanelContainer(@NotNull final Location location) {
        return new InfoPanelBuilder(location.clone().toCenterLocation().add(getOffset()), SIZE)
                .addAttribute("chargeText", false)
                .addAttribute("chargeBar", false)
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

        final double capacity = group.get().getBlock().getSettings().getCapacity();
        final double charge = BlockStorageAPI.getDouble(location.get(), Keys.BS_CHARGE);

        container.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        container.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }
}
