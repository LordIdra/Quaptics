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

public class ChargerPanel extends CapacitorPanel {

    public ChargerPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ChargerPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
        super(panelId, groupId);
    }

    @Override
    public void update() {
        final ConnectionGroup group = getGroup();
        if ((group == null) || !(group.getBlock() instanceof Charger)) {
            // How the hell did we get here?
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final ItemStack stack = charger.getItem(location);
        final QuapticChargeableItem item = QuapticChargeableItem.fromStack(stack);
        if (item == null) {
            return;
        }

        final double capacity = item.getSettings().getCapacity();
        final double charge = QuapticChargeableItem.getCharge(stack);

        if ((charge == 0.0) || (capacity == 0.0)) {
            setPanelHidden(false);
            return;
        }

        setPanelHidden(true);

        panel.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }
}
