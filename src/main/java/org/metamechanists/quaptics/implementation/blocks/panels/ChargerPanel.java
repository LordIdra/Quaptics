package org.metamechanists.quaptics.implementation.blocks.panels;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.base.DisplayGroupTickerBlock;
import org.metamechanists.quaptics.implementation.blocks.consumers.Charger;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.panel.PanelBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.Optional;

public class ChargerPanel extends BlockPanel {

    public ChargerPanel(@NotNull final Location location, final ConnectionGroupId groupId) {
        super(location, groupId);
    }

    public ChargerPanel(@NotNull final PanelId panelId, final ConnectionGroupId groupId) {
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
        setPanelHidden(true);

        final Optional<ItemStack> stack = getStack(groupId);
        if (stack.isEmpty()) {
            return;
        }

        final Optional<QuapticChargeableItem> item = QuapticChargeableItem.fromStack(stack.get());
        if (item.isEmpty()) {
            return;
        }

        final double capacity = item.get().getSettings().getCapacity();
        final double charge = QuapticChargeableItem.getCharge(stack.get());
        if ((charge == 0.0) || (capacity == 0.0)) {
            return;
        }

        setPanelHidden(false);

        panel.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }

    private static Optional<ItemStack> getStack(final @NotNull ConnectionGroupId groupId) {
        final Optional<ConnectionGroup> group = groupId.get();
        if (group.isEmpty() || !(group.get().getBlock() instanceof Charger)) {
            return Optional.empty();
        }

        final Optional<Location> location = group.get().getLocation();
        if (location.isEmpty()) {
            return Optional.empty();
        }

        final Optional<Display> display = DisplayGroupTickerBlock.getDisplay(location.get(), "item");
        if (display.isEmpty()) {
            return Optional.empty();
        }

        if (!(display.get() instanceof final ItemDisplay itemDisplay)) {
            return Optional.empty();
        }

        final ItemStack stack = itemDisplay.getItemStack();
        return stack == null || stack.getItemMeta() == null ? Optional.empty() : Optional.of(stack);
    }
}
