package org.metamechanists.quaptics.panels.implementation;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.BlockPanel;
import org.metamechanists.quaptics.panels.PanelBuilder;
import org.metamechanists.quaptics.panels.PanelContainer;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.PanelId;

import java.util.Optional;

public class ChargerPanel extends BlockPanel {

    public ChargerPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ChargerPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
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

        final Optional<ItemStack> stack = Charger.getStack(group.get());
        if (stack.isEmpty()) {
            return;
        }

        final double capacity = QuapticChargeableItem.getCapacity(stack.get());
        final double charge = QuapticChargeableItem.getCharge(stack.get());

        panelContainer.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panelContainer.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }


}
