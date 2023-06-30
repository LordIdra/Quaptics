package org.metamechanists.quaptics.implementation.panels;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.manipulators.Capacitor;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panel.Panel;
import org.metamechanists.quaptics.panel.PanelBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.PanelID;

public class CapacitorPanel {
    private static final Vector BLOCK_OFFSET = new Vector(0, 0.8, 0);
    private static final float SIZE = 0.40F;
    private final ConnectionGroupID groupID;
    private final Panel panel;

    public CapacitorPanel(@NotNull Location location, ConnectionGroupID groupID) {
        this.groupID = groupID;
        this.panel = new PanelBuilder(location.clone().add(BLOCK_OFFSET), SIZE)
                .addAttribute("chargeText")
                .addAttribute("chargeBar")
                .build();
    }

    public CapacitorPanel(@NotNull PanelID panelID, ConnectionGroupID groupID) {
        this.groupID = groupID;
        this.panel = panelID.get();
    }

    public PanelID getID() {
        return panel.getID();
    }

    private ConnectionGroup getGroup() {
        return groupID.get();
    }

    public void setPanelHidden(boolean hidden) {
        panel.setHidden(hidden);
    }

    // TODO listener to toggle panel
    public void toggleHidden() {
        panel.toggleHidden();
    }

    public void update() {
        final double capacity = getGroup().getBlock().getSettings().getCapacity();
        final double charge = Capacitor.getCharge(getGroup().getLocation());

        if (charge == 0) {
            setPanelHidden(true);
            return;
        }

        setPanelHidden(false);

        panel.setText("chargeText", Lore.chargeBar((int)charge, (int)capacity));
        panel.setText("chargeBar", Lore.chargeValues((int)charge, (int)capacity));
    }

    public void remove() {
        panel.remove();
    }
}
