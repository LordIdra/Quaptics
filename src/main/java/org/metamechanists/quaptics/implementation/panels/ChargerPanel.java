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

    public ChargerPanel(@NotNull Location location, ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ChargerPanel(@NotNull PanelId panelID, ConnectionGroupId groupId) {
        super(panelID, groupId);
    }

    @Override
    public void update() {
        final ConnectionGroup group = getGroup();
        if (group == null || !(group.getBlock() instanceof Charger charger)) {
            // How the hell did we get here?
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final ItemStack itemStack = charger.getItem(location);
        if (itemStack == null || itemStack.getItemMeta() == null) {
            return;
        }

        final double capacity = charger.getSettings().getCapacity();
        final double charge = QuapticChargeableItem.getCharge(itemStack);

        if (charge == 0) {
            setPanelHidden(true);
            return;
        }

        setPanelHidden(false);

        panel.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }
}
