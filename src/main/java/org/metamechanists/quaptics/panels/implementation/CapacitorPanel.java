package org.metamechanists.quaptics.panels.implementation;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.BlockPanel;
import org.metamechanists.quaptics.panels.PanelContainer;
import org.metamechanists.quaptics.panels.PanelBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

public class CapacitorPanel extends BlockPanel {
    public CapacitorPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public CapacitorPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
        super(panelId, groupId);
    }

    @Override
    protected PanelContainer buildPanelContainer(@NotNull final Location location) {
        return new PanelBuilder(location.clone().toCenterLocation().add(getOffset()), SIZE)
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
        final double charge = Capacitor.getCharge(location.get());

        panelContainer.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panelContainer.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }
}
