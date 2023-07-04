package org.metamechanists.quaptics.implementation.blocks.panels;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.Panel;
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
    protected Panel createPanel(@NotNull final Location location) {
        return new PanelBuilder(location.clone().toCenterLocation().add(BLOCK_OFFSET), SIZE)
                .addAttribute("chargeText", false)
                .addAttribute("chargeBar", false)
                .build();
    }

    @Override
    public void update() {
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

        if (charge == 0) {
            setPanelHidden(true);
            return;
        }

        setPanelHidden(false);

        panel.setText("chargeText", Lore.panelChargeBar((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.panelChargeValues((int)charge, (int)capacity));
    }
}
