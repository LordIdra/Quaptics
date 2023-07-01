package org.metamechanists.quaptics.implementation.panels;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.Optional;

public class ChargerPanel extends CapacitorPanel {

    public ChargerPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ChargerPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
        super(panelId, groupId);
    }

    @Override
    public void update() {
        final Optional<ConnectionGroup> group = getGroup();
        if (group.isEmpty() || !(group.get().getBlock() instanceof Charger)) {
            // How the hell did we get here?
            return;
        }

        final Optional<Location> location = group.get().getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<ItemStack> stack = Charger.getItem(location.get());
        if (stack.isEmpty()) {
            setPanelHidden(true);
            return;
        }

        final Optional<QuapticChargeableItem> item = QuapticChargeableItem.fromStack(stack.get());
        if (item.isEmpty()) {
            return;
        }

        final double capacity = item.get().getSettings().getCapacity();
        final double charge = QuapticChargeableItem.getCharge(stack.get());
        if ((charge == 0.0) || (capacity == 0.0)) {
            setPanelHidden(true);
            return;
        }

        setPanelHidden(false);

        panel.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }
}
