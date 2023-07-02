package org.metamechanists.quaptics.connections;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.metalib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.beams.FrequencyColor;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.id.BeamId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.LinkId;

import java.util.Optional;

public class Link {
    private static final float MAX_BEAM_SIZE = 0.095F;
    @Getter
    private final LinkId linkId;
    private final ConnectionPointId outputId;
    private final ConnectionPointId inputId;
    private final Location outputLocation;
    private final Location inputLocation;
    private final double maxPower;
    private @Nullable BeamId beamId;
    @Getter
    private double power;
    @Getter
    private double frequency;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Link(final ConnectionPointId inputId, final ConnectionPointId outputId) {
        this.inputId = inputId;
        this.outputId = outputId;

        final ConnectionPointInput input = getInput().get();
        final ConnectionPointOutput output = getOutput().get();

        this.inputLocation = input.getLocation().get();
        this.outputLocation = output.getLocation().get();
        this.linkId = new LinkId(new DisplayGroup(inputLocation, 0, 0).getParentUUID());
        this.maxPower = input.getGroup().get().getBlock().getSettings().getTier().maxPower;

        saveData(); // the points being linked will not be able to get the link from the id without this line
        input.link(linkId);
        output.link(linkId);
        updateGroup(input);
        updateGroup(output);
        regenerateBeam();
        saveData();
        getOutput().flatMap(ConnectionPoint::getGroup).ifPresent(ConnectionGroup::updatePanels);
        getInput().flatMap(ConnectionPoint::getGroup).ifPresent(ConnectionGroup::updatePanels);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Link(final LinkId linkId) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(linkId);
        this.linkId = linkId;
        this.outputId = traverser.getConnectionPointId("outputId");
        this.inputId = traverser.getConnectionPointId("inputId");
        this.beamId = traverser.getBeamId("beamId");
        this.power = traverser.getDouble("power");
        this.frequency = traverser.getDouble("frequency");
        this.maxPower = traverser.getDouble("maxPower");
        this.inputLocation = getInput().get().getLocation().get();
        this.outputLocation = getOutput().get().getLocation().get();
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(linkId);
        traverser.set("outputId", outputId.toString());
        traverser.set("inputId", inputId.toString());
        traverser.set("beamId", beamId == null ? "null" : beamId.toString());
        traverser.set("power", power);
        traverser.set("frequency", frequency);
        traverser.set("maxPower", maxPower);
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
        return beamId != null;
    }

    private Optional<DirectBeam> getBeam() {
        return Optional.ofNullable(beamId).map(DirectBeam::new);
    }

    public void remove() {
        if (hasBeam()) {
            getBeam().ifPresent(DirectBeam::deprecate);
            beamId = null;
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

    private void regenerateBeam() {
        getBeam().ifPresent(DirectBeam::deprecate);
        this.beamId = new DirectBeam(
                FrequencyColor.getMaterial(frequency),
                outputLocation,
                inputLocation,
                Math.min((float)(power / maxPower) * MAX_BEAM_SIZE, MAX_BEAM_SIZE))
                .getID();
    }

    private void updateBeam() {
        final Optional<DirectBeam> beam = getBeam();
        if (beam.isEmpty()) {
            return;
        }

        beam.get().setMaterial(FrequencyColor.getMaterial(frequency));
        beam.get().setRadius(outputLocation, inputLocation, Math.min((float)(power / maxPower) * MAX_BEAM_SIZE, MAX_BEAM_SIZE));
    }

    private void updatePanels() {
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

        saveData();
        updateBeam();
        updatePanels();
        getInput().ifPresent(Link::updateGroup);
    }

    private void setFrequency(final double frequency) {
        // TODO how can we limit the size of frequency changes?
        if (this.frequency == frequency) {
            return;
        }

        this.frequency = frequency;

        saveData();
        updateBeam();
        updatePanels();
        getInput().ifPresent(Link::updateGroup);
    }

    public void setPowerAndFrequency(final double power, final double frequency) {
        setPower(power);
        setFrequency(frequency);
    }

    public void disable() {
        setPowerAndFrequency(0, 0);
    }
}