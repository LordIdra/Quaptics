package org.metamechanists.quaptics.implementation.panels;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.panel.PanelBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.PanelId;

public class CapacitorPanel {
    private static final Vector BLOCK_OFFSET = new Vector(0, 0.7, 0);
    private static final float SIZE = 0.40F;
    private final ConnectionGroupId groupId;
    protected final Panel panel;

    public CapacitorPanel(@NotNull Location location, ConnectionGroupId groupId) {
        this.groupId = groupId;
        this.panel = new PanelBuilder(location.clone().toCenterLocation().add(BLOCK_OFFSET), SIZE)
                .addAttribute("chargeText")
                .addAttribute("chargeBar")
                .build();
        this.panel.setAttributeHidden("chargeText", false);
        this.panel.setAttributeHidden("chargeBar", false);
        this.panel.setHidden(false);
    }

    public CapacitorPanel(@NotNull PanelId panelId, ConnectionGroupId groupId) {
        this.groupId = groupId;
        this.panel = panelId.get();
    }

    public PanelId getId() {
        return panel.getId();
    }

    protected @Nullable ConnectionGroup getGroup() {
        return groupId.get();
    }

    public void setPanelHidden(boolean hidden) {
        panel.setHidden(hidden);
    }

    public void toggleHidden() {
        panel.toggleHidden();
    }

    public void update() {
        final ConnectionGroup group = getGroup();
        if (group == null) {
            return;
        }

        final Location location = group.getLocation();
        if (location == null) {
            return;
        }

        final double capacity = group.getBlock().getSettings().getCapacity();
        final double charge = Capacitor.getCharge(location);

        if (charge == 0) {
            setPanelHidden(true);
            return;
        }

        setPanelHidden(false);

        panel.setText("chargeText", Lore.chargeBarRaw((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.chargeValuesRaw((int)charge, (int)capacity));
    }

    public void remove() {
        panel.remove();
    }
}
