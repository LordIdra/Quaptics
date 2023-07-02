package org.metamechanists.quaptics.connections.points;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display.Brightness;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.panels.PointPanel;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.BlockDisplayId;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.InteractionId;
import org.metamechanists.quaptics.utils.id.LinkId;
import org.metamechanists.quaptics.utils.id.PanelId;

import java.util.Optional;

public abstract class ConnectionPoint {
    private static final float SIZE = 0.1F;
    private static final Vector INTERACTION_OFFSET = new Vector(0, -SIZE/2, 0);
    private static final Color SELECTED_COLOR = Color.fromRGB(0, 255, 0);
    @Getter
    private static final int CONNECTED_BRIGHTNESS = 15;
    @Getter
    private static final int DISCONNECTED_BRIGHTNESS = 3;
    private final ConnectionGroupId groupId;
    @Getter
    private final InteractionId interactionId;
    private final BlockDisplayId blockDisplayId;
    private PanelId panelId;
    private @Nullable LinkId linkId;
    @Getter
    private final String name;

    protected ConnectionPoint(final ConnectionGroupId groupId, final String name, @NotNull final Location location, final Material material) {
        final Interaction interaction = new InteractionBuilder(location.clone().add(INTERACTION_OFFSET))
                .setWidth(SIZE)
                .setHeight(SIZE)
                .build();
        this.groupId = groupId;
        this.interactionId = new InteractionId(interaction.getUniqueId());
        this.blockDisplayId = new BlockDisplayId(new BlockDisplayBuilder(location)
                .setMaterial(material)
                .setTransformation(Transformations.adjustedScale(new Vector3f(SIZE, SIZE, SIZE)))
                .setBrightness(DISCONNECTED_BRIGHTNESS)
                .build()
                .getUniqueId());
        this.panelId = new PointPanel(location, getId()).getId();
        this.name = name;
        saveData();
        getPointPanel().update();
    }

    protected ConnectionPoint(final ConnectionPointId pointId) {
        final DataTraverser traverser = new DataTraverser(pointId);
        final JsonObject mainSection = traverser.getData();
        final String linkIdString = mainSection.get("linkId").getAsString();
        this.groupId = new ConnectionGroupId(mainSection.get("groupId").getAsString());
        this.blockDisplayId = new BlockDisplayId(mainSection.get("blockDisplayId").getAsString());
        this.interactionId = new InteractionId(mainSection.get("interactionId").getAsString());
        this.panelId = new PanelId(mainSection.get("panelId").getAsString());
        this.linkId = linkIdString.equals("null") ? null : new LinkId(linkIdString);
        this.name = mainSection.get("name").getAsString();
    }

    protected void saveData(@NotNull final JsonObject mainSection) {
        mainSection.add("groupId", new JsonPrimitive(groupId.getUUID().toString()));
        mainSection.add("blockDisplayId", new JsonPrimitive(blockDisplayId.getUUID().toString()));
        mainSection.add("interactionId", new JsonPrimitive(interactionId.getUUID().toString()));
        mainSection.add("panelId", new JsonPrimitive(panelId.getUUID().toString()));
        mainSection.add("linkId", new JsonPrimitive((linkId == null) ? "null" : linkId.getUUID().toString()));
        mainSection.add("name", new JsonPrimitive(name));
    }

    protected abstract void saveData();

    public final @NotNull ConnectionPointId getId() {
        return new ConnectionPointId(interactionId);
    }

    public Optional<Link> getLink() {
        return linkId == null ? Optional.empty() : linkId.get();
    }

    public boolean isLinkEnabled() {
        return getLink().isPresent() && getLink().get().isEnabled();
    }

    public Optional<Location> getLocation() {
        return getBlockDisplay().isPresent()
                ? Optional.of(getBlockDisplay().get().getLocation())
                : Optional.empty();
    }

    @Contract(" -> new")
    public @NotNull PointPanel getPointPanel() {
        return new PointPanel(panelId, getId());
    }

    private Optional<BlockDisplay> getBlockDisplay() {
        return blockDisplayId.get();
    }

    private Optional<Interaction> getInteraction() {
        return interactionId.get();
    }

    public Optional<ConnectionGroup> getGroup() {
        return groupId.get();
    }

    public void remove() {
        getLink().ifPresent(Link::remove);
        getPointPanel().remove();
        getBlockDisplay().ifPresent(BlockDisplay::remove);
        getInteraction().ifPresent(Interaction::remove);
    }

    public void changeLocation(@NotNull final Location location) {
        getBlockDisplay().ifPresent(blockDisplay -> blockDisplay.teleport(location));
        getInteraction().ifPresent(interaction -> interaction.teleport(location.clone().add(INTERACTION_OFFSET)));

        final boolean wasHidden = getPointPanel().isPanelHidden();
        getPointPanel().remove();

        this.panelId = new PointPanel(location, getId()).getId();
        getPointPanel().setPanelHidden(wasHidden);
        saveData();
    }

    public void updatePanel() {
        getPointPanel().update();
    }

    public void togglePanelHidden() {
        getPointPanel().toggleHidden();
    }

    public void unlink() {
        this.linkId = null;
        getBlockDisplay().ifPresent(blockDisplay -> blockDisplay.setBrightness(new Brightness(DISCONNECTED_BRIGHTNESS, 0)));
        saveData();
        updatePanel();
    }

    public void link(final LinkId linkId) {
        unlink();
        this.linkId = linkId;
        getBlockDisplay().ifPresent(blockDisplay -> blockDisplay.setBrightness(new Brightness(CONNECTED_BRIGHTNESS, 0)));
        saveData();
        updatePanel();
    }

    public void select() {
        getBlockDisplay().ifPresent(blockDisplay -> {
            blockDisplay.setGlowing(true);
            blockDisplay.setGlowColorOverride(SELECTED_COLOR);
        });
    }

    public void deselect() {
        getBlockDisplay().ifPresent(blockDisplay -> blockDisplay.setGlowing(false));
    }
}
