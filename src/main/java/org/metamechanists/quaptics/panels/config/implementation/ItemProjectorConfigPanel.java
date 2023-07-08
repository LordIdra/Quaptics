package org.metamechanists.quaptics.panels.config.implementation;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.implementation.blocks.consumers.ItemProjector;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.panels.config.ConfigPanel;
import org.metamechanists.quaptics.panels.config.ConfigPanelBuilder;
import org.metamechanists.quaptics.panels.config.ConfigPanelContainer;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import java.util.Objects;
import java.util.Optional;

public class ItemProjectorConfigPanel extends ConfigPanel {

    public ItemProjectorConfigPanel(@NotNull final Location location, final ConnectionGroupId groupId, final float rotationY) {
        super(groupId, location, rotationY);
    }

    public ItemProjectorConfigPanel(@NotNull final ConfigPanelId id, final ConnectionGroupId groupId) {
        super(id, groupId);
    }

    @Override
    protected ConfigPanelContainer buildPanelContainer(@NotNull final ConnectionGroupId groupId, @NotNull final Location location, final float rotationY) {
        return new ConfigPanelBuilder(groupId, location.clone().add(getOffset()), SIZE, rotationY)
                .addAttribute("height", "&fHeight")
                .addAttribute("size", "&fSize")
                .addAttribute("mode", "&fMode")
                .build();
    }

    @Override
    public void interact(@NotNull final Location location, final String name, final String type) {
        if ("height".equals(name)) {
            double height = BlockStorageAPI.getDouble(location, Keys.BS_HEIGHT);
            height += "add".equals(type) ? 1 : -1;
            height = Utils.clampToRange(height, 0, ItemProjector.MAX_HEIGHT);
            BlockStorageAPI.set(location, Keys.BS_HEIGHT, height);
        }

        if ("size".equals(name)) {
            double size = BlockStorageAPI.getDouble(location, Keys.BS_SIZE);
            size += "add".equals(type) ? 1 : -1;
            size = Utils.clampToRange(size, 0, ItemProjector.MAX_SIZE);
            BlockStorageAPI.set(location, Keys.BS_SIZE, size);
        }

        if ("mode".equals(name)) {
            int size = BlockStorageAPI.getInt(location, Keys.BS_MODE);
            size += "add".equals(type) ? 1 : -1;
            size = Utils.clampToRange(size, 0, ItemProjector.MAX_MODE);
            BlockStorageAPI.set(location, Keys.BS_MODE, size);
        }

        ItemProjector.onConfigUpdated(location);
        update();
    }

    @Override
    protected void update() {
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

        final double height = BlockStorageAPI.getDouble(location.get(), Keys.BS_HEIGHT);
        final double size = BlockStorageAPI.getDouble(location.get(), Keys.BS_SIZE);
        final int mode = BlockStorageAPI.getInt(location.get(), Keys.BS_MODE);

        container.setValue("height", Lore.progressBar(height, ItemProjector.MAX_HEIGHT, "&b", "&7"));
        container.setValue("size", Lore.progressBar(size, ItemProjector.MAX_SIZE, "&b", "&7"));
        container.setValue("mode", Objects.toString(mode));
    }

    @SuppressWarnings("MagicNumber")
    @Override
    protected Vector getOffset() {
        return new Vector(0.0, -0.3, 0.0);
    }
}
