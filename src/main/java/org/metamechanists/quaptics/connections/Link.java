package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.metalib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.beams.DirectBeam;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.LinkId;
import org.metamechanists.quaptics.utils.id.TickerId;

public class Link {
    private static final float MAX_BEAM_SIZE = 0.095F;
    @Getter
    private final LinkId linkId;
    private final ConnectionPointId outputId;
    private final ConnectionPointId inputId;
    private final double maxPower;
    private @Nullable TickerId tickerId;
    @Getter
    private boolean enabled;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;

    public Link(final ConnectionPointId inputId, final ConnectionPointId outputId) {
        this.inputId = inputId;
        this.outputId = outputId;
        this.linkId = new LinkId(new DisplayGroup(getInput().getLocation(), 0, 0).getParentUUID());
        this.maxPower = getOutput().getGroup().getBlock().getSettings().getTier().maxPower;
        saveData(); // the points being linked will not be able to get the link from the id without this line
        getInput().link(linkId);
        getOutput().link(linkId);
        updateGroupIfNotNull(getInput());
        updateGroupIfNotNull(getOutput());
        update();
    }

    public Link(final LinkId linkId) {
        final DataTraverser traverser = new DataTraverser(linkId);
        final JsonObject mainSection = traverser.getData();
        this.linkId = linkId;
        this.outputId = new ConnectionPointId(mainSection.get("outputId").getAsString());
        this.inputId = new ConnectionPointId(mainSection.get("inputId").getAsString());
        this.tickerId = mainSection.get("tickerId").getAsString().equals("null")
                ? null
                : new TickerId(mainSection.get("tickerId").getAsString());
        this.enabled = mainSection.get("enabled").getAsBoolean();
        this.power = mainSection.get("power").getAsDouble();
        this.frequency = mainSection.get("frequency").getAsDouble();
        this.phase = mainSection.get("phase").getAsInt();
        this.maxPower = mainSection.get("maxPower").getAsDouble();
    }

    private void saveData() {
        final DataTraverser traverser = new DataTraverser(linkId);
        final JsonObject mainSection = traverser.getData();
        mainSection.add("outputId", new JsonPrimitive(outputId.toString()));
        mainSection.add("inputId", new JsonPrimitive(inputId.toString()));
        mainSection.add("tickerId", new JsonPrimitive(tickerId == null ? "null" : tickerId.toString()));
        mainSection.add("enabled", new JsonPrimitive(enabled));
        mainSection.add("power", new JsonPrimitive(power));
        mainSection.add("frequency", new JsonPrimitive(frequency));
        mainSection.add("phase", new JsonPrimitive(phase));
        mainSection.add("maxPower", new JsonPrimitive(maxPower));
        traverser.save();
    }

    public @Nullable ConnectionPointOutput getOutput() {
        return outputId.get() instanceof final ConnectionPointOutput output
                ? output
                : null;
    }

    public @Nullable ConnectionPointInput getInput() {
        return inputId.get() instanceof final ConnectionPointInput input
                ? input
                : null;
    }

    private boolean hasBeam() {
        return tickerId != null;
    }

    private @Nullable DirectBeam getBeam() {
        return tickerId != null ? new DirectBeam(tickerId) : null;
    }

    public void remove() {
        if (hasBeam()) {
            getBeam().deprecate();
            tickerId = null;
        }

        final ConnectionPointOutput output = getOutput();
        if (output != null) {
            output.unlink();
            updateGroupIfNotNull(getOutput());
        }

        final ConnectionPointInput input = getInput();
        if (input != null) {
            input.unlink();
            updateGroupIfNotNull(getInput());
        }
    }

    private void updateBeam() {
        if (hasBeam()) {
            getBeam().deprecate();
            tickerId = null;
        }

        final ConnectionPointOutput output = getOutput();
        final ConnectionPointInput input = getInput();
        if (!enabled || output == null || input == null) {
            return;
        }

        final Location outputLocation = output.getLocation();
        final Location inputLocation = input.getLocation();
        if (outputLocation == null || inputLocation == null) {
            return;
        }

        this.tickerId = new DirectBeam(new DirectTicker(
                Material.WHITE_CONCRETE,
                outputLocation,
                inputLocation,
                Math.min((float)(power / maxPower) * MAX_BEAM_SIZE, MAX_BEAM_SIZE)))
                .getId();
    }

    private void update() {
        updateBeam();
        saveData();

        final ConnectionPointOutput output = getOutput();
        final ConnectionGroup outputGroup = output != null ? output.getGroup() : null;
        if (output != null && outputGroup != null) {
            outputGroup.updatePanels();
        }

        final ConnectionPointInput input = getInput();
        final ConnectionGroup inputGroup = input != null ? input.getGroup() : null;
        if (input != null && inputGroup != null) {
            inputGroup.updatePanels();
        }
    }

    public void setEnabled(final boolean enabled) {
        if (enabled == this.enabled) {
            return;
        }

        this.enabled = enabled;

        if (!enabled) {
            power = 0;
            frequency = 0;
            phase = 0;
        }

        update();
        updateGroupIfNotNull(getInput());
    }

    private static void updateGroupIfNotNull(final @Nullable ConnectionPoint point) {
        final ConnectionGroup group = point != null ? point.getGroup() : null;
        if (group != null) {
            BlockUpdateScheduler.scheduleUpdate(group.getId());
        }
    }

    private void setPower(final double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;

        update();
        updateGroupIfNotNull(getInput());
    }

    private void setFrequency(final double frequency) {
        // TODO how can we limit the size of frequency changes?
        if (this.frequency == frequency) {
            return;
        }

        this.frequency = frequency;

        update();
        updateGroupIfNotNull(getInput());
    }

    private void setPhase(final int phase) {
        // TODO how can we limit the size of phase changes?
        if (this.phase == phase) {
            return;
        }

        this.phase = phase;

        update();
        updateGroupIfNotNull(getInput());
    }

    public void setAttributes(final double power, final double frequency, final int phase, final boolean enabled) {
        setPower(power);
        setFrequency(frequency);
        setPhase(phase);
        setEnabled(enabled);
    }
}