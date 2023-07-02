package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.metalib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.beams.DirectBeam;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.LinkId;
import org.metamechanists.quaptics.utils.id.TickerId;

import java.util.Optional;

public class Link {
    private static final float MAX_BEAM_SIZE = 0.095F;
    @Getter
    private final LinkId linkId;
    private final ConnectionPointId outputId;
    private final ConnectionPointId inputId;
    private final double maxPower;
    private @Nullable TickerId tickerId;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;

    public Link(final ConnectionPointId inputId, final ConnectionPointId outputId) {
        this.inputId = inputId;
        this.outputId = outputId;
        this.linkId = new LinkId(new DisplayGroup(getInput().get().getLocation().get(), 0, 0).getParentUUID());
        this.maxPower = getInput().get().getGroup().get().getBlock().getSettings().getTier().maxPower;
        saveData(); // the points being linked will not be able to get the link from the id without this line
        getInput().get().link(linkId);
        getOutput().get().link(linkId);
        updateGroup(getInput().get());
        updateGroup(getOutput().get());
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
        mainSection.add("power", new JsonPrimitive(power));
        mainSection.add("frequency", new JsonPrimitive(frequency));
        mainSection.add("phase", new JsonPrimitive(phase));
        mainSection.add("maxPower", new JsonPrimitive(maxPower));
        traverser.save();
    }

    public Optional<ConnectionPointOutput> getOutput() {
        if (outputId.get().isEmpty()) {
            return Optional.empty();
        }
        return outputId.get().get() instanceof final ConnectionPointOutput output
                ? Optional.of(output)
                : Optional.empty();
    }

    public Optional<ConnectionPointInput> getInput() {
        if (inputId.get().isEmpty()) {
            return Optional.empty();
        }
        return inputId.get().get() instanceof final ConnectionPointInput input
                ? Optional.of(input)
                : Optional.empty();
    }

    private boolean hasBeam() {
        return tickerId != null;
    }

    public Optional<DirectBeam> getBeam() {
        return Optional.ofNullable(tickerId).map(DirectBeam::new);
    }

    public void remove() {
        if (hasBeam()) {
            getBeam().ifPresent(DirectBeam::deprecate);
            tickerId = null;
        }

        getOutput().ifPresent(output -> {
            output.unlink();
            updateGroup(output);
        });

        getInput().ifPresent(input -> {
            input.unlink();
            updateGroup(input);
        });
    }

    private void updateBeam() {
        getBeam().ifPresent(DirectBeam::deprecate);
        tickerId = null;

        if (getOutput().isEmpty()
                || getInput().isEmpty()
                || getOutput().get().getLocation().isEmpty()
                || getInput().get().getLocation().isEmpty()) {
            return;
        }

        this.tickerId = new DirectBeam(new DirectTicker(
                Colors.firstFrequencyColor(),
                getOutput().get().getLocation().get(),
                getInput().get().getLocation().get(),
                Math.min((float)(power / maxPower) * MAX_BEAM_SIZE, MAX_BEAM_SIZE)))
                .getId().get();
    }

    private void update() {
        updateBeam();
        saveData();
        getOutput().flatMap(ConnectionPoint::getGroup).ifPresent(ConnectionGroup::updatePanels);
        getInput().flatMap(ConnectionPoint::getGroup).ifPresent(ConnectionGroup::updatePanels);
    }

    private static void updateGroup(final @NotNull ConnectionPoint point) {
        point.getGroup().ifPresent(group -> BlockUpdateScheduler.scheduleUpdate(group.getId()));
    }

    public boolean isEnabled() {
        return power != 0;
    }

    public void setPower(final double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;

        update();
        getInput().ifPresent(Link::updateGroup);
    }

    private void setFrequency(final double frequency) {
        // TODO how can we limit the size of frequency changes?
        if (this.frequency == frequency) {
            return;
        }

        this.frequency = frequency;

        update();
        getInput().ifPresent(Link::updateGroup);
    }

    public void setPowerAndFrequency(final double power, final double frequency) {
        setPower(power);
        setFrequency(frequency);
    }
}