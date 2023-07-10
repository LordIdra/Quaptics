package org.metamechanists.quaptics.connections;

import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.beams.FrequencyColor;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.schedulers.BlockUpdateScheduler;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.id.complex.DirectBeamId;
import org.metamechanists.quaptics.utils.id.complex.LinkId;

import java.util.Optional;

public class Link {
    private static final int ARBITRARILY_LARGE_NUMBER = 9999999;
    private static final double MAX_POWER_CHANGE_PROPORTION = 0.0001;
    private static final double MAX_FREQUENCY_CHANGE_PROPORTION = 0.0001;
    private static final float MIN_BEAM_SIZE = 0.005F;
    private static final float MAX_BEAM_SIZE = 0.095F;
    @Getter
    private final LinkId id;
    private final ConnectionPointId outputId;
    private final ConnectionPointId inputId;
    private final Location outputLocation;
    private final Location inputLocation;
    private final double maxPower;
    private @Nullable DirectBeamId directBeamId;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Link(final ConnectionPointId inputId, final ConnectionPointId outputId) {
        this.inputId = inputId;
        this.outputId = outputId;

        final ConnectionPoint input = getInput().get();
        final ConnectionPoint output = getOutput().get();

        this.inputLocation = input.getLocation().get();
        this.outputLocation = output.getLocation().get();
        this.id = new LinkId(new InteractionBuilder().height(0).width(0).build(inputLocation).getUniqueId());
        this.maxPower = input.getGroup().get().getBlock().getSettings().getTier().maxPower;

        saveData(); // the points being linked will not be able to get the link from the id without this line
        input.link(id);
        output.link(id);
        updateGroup(input);
        updateGroup(output);
        regenerateBeam();
        saveData();
        getOutput().flatMap(ConnectionPoint::getGroup).ifPresent(ConnectionGroup::updatePanels);
        getInput().flatMap(ConnectionPoint::getGroup).ifPresent(ConnectionGroup::updatePanels);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Link(final LinkId id) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        this.id = id;
        this.outputId = traverser.getConnectionPointId("outputId");
        this.inputId = traverser.getConnectionPointId("inputId");
        this.directBeamId = traverser.getBeamId("beamId");
        this.power = traverser.getDouble("power");
        this.frequency = traverser.getDouble("frequency");
        this.maxPower = traverser.getDouble("maxPower");
        this.inputLocation = getInput().get().getLocation().get();
        this.outputLocation = getOutput().get().getLocation().get();
    }

    private void saveData() {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(id);
        traverser.set("outputId", outputId);
        traverser.set("inputId", inputId);
        traverser.set("beamId", directBeamId);
        traverser.set("power", power);
        traverser.set("frequency", frequency);
        traverser.set("maxPower", maxPower);
    }

    public Optional<ConnectionPoint> getOutput() {
        return outputId.get().isEmpty() || !outputId.get().get().isOutput()
                ? Optional.empty()
                : Optional.of(outputId.get().get());
    }

    public Optional<ConnectionPoint> getInput() {
        return inputId.get().isEmpty() || !inputId.get().get().isInput()
                ? Optional.empty()
                : Optional.of(inputId.get().get());
    }

    private boolean hasBeam() {
        return directBeamId != null;
    }

    private Optional<DirectBeam> getBeam() {
        return Optional.ofNullable(directBeamId).map(DirectBeam::new);
    }

    public void remove() {
        if (hasBeam()) {
            getBeam().ifPresent(DirectBeam::deprecate);
            directBeamId = null;
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
        this.directBeamId = new DirectBeam(
                FrequencyColor.getMaterial(frequency),
                outputLocation,
                inputLocation,
                calculateRadius())
                .getId();
    }

    private void updateBeam() {
        final Optional<DirectBeam> beam = getBeam();
        if (beam.isEmpty()) {
            return;
        }

        beam.get().setMaterial(FrequencyColor.getMaterial(frequency));
        beam.get().setRadius(outputLocation, inputLocation, calculateRadius());
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

    private float calculateRadius() {
        if (power == 0) {
            return 0;
        }
        return Utils.clampToRange((float) (power / maxPower) * MAX_BEAM_SIZE, MIN_BEAM_SIZE, MAX_BEAM_SIZE);
    }

    private static double calculateChange(final double initialValue, final double newValue) {
        if (initialValue == 0) {
            return newValue == 0 ? 0 : ARBITRARILY_LARGE_NUMBER;
        }
        return Math.abs((newValue-initialValue) / initialValue);
    }

    public void setPower(final double power) {
        if (calculateChange(this.power, power) < MAX_POWER_CHANGE_PROPORTION) {
            return;
        }

        this.power = power;

        saveData();
        updateBeam();
        updatePanels();
        getInput().ifPresent(Link::updateGroup);
    }
    private void setFrequency(final double frequency) {
        if (calculateChange(this.frequency, frequency) < MAX_FREQUENCY_CHANGE_PROPORTION) {
            return;
        }

        this.frequency = frequency;

        saveData();
        updateBeam();
        updatePanels();
        getInput().ifPresent(Link::updateGroup);
    }
    private void setPhase(final int phase) {
        int newPhase = phase;
        if (newPhase > 360) {
            newPhase -= 360;
        }

        if (this.phase == newPhase) {
            return;
        }

        this.phase = newPhase;

        saveData();
        updateBeam();
        updatePanels();
        getInput().ifPresent(Link::updateGroup);
    }
    public void setPowerFrequency(final double power, final double frequency) {
        setPower(power);
        setFrequency(frequency);
    }
    public void setPowerFrequencyPhase(final double power, final double frequency, final int phase) {
        setPower(power);
        setFrequency(frequency);
        setPhase(phase);
    }

    public void disable() {
        setPowerFrequencyPhase(0, 0, 0);
    }
}