package org.metamechanists.death_lasers.connections.links;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.beams.DeprecatedBeamStorage;
import org.metamechanists.death_lasers.beams.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.connections.BlockUpdateScheduler;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;

import java.util.HashMap;
import java.util.Map;

public class Link implements ConfigurationSerializable {
    @Getter
    private boolean enabled;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;
    private final Location outputLocation;
    private final Location inputLocation;
    private DirectBlockDisplayBeam beam;


    public Link(ConnectionPointInput input, ConnectionPointOutput output) {
        this.inputLocation = input.getLocation();
        this.outputLocation = output.getLocation();
        input.link(this);
        output.link(this);
        BlockUpdateScheduler.scheduleUpdate(output.getGroup());
        update();
    }

    private Link(boolean enabled, double power, double frequency, int phase,
                 Location outputLocation, Location inputLocation, DirectBlockDisplayBeam beam) {
        this.enabled = enabled;
        this.power = power;
        this.frequency = frequency;
        this.phase = phase;
        this.outputLocation = outputLocation;
        this.inputLocation = inputLocation;
        this.beam = beam;
    }

    public ConnectionPointOutput getOutput() {
        return (ConnectionPointOutput) ConnectionPointStorage.getPoint(outputLocation);
    }

    public ConnectionPointInput getInput() {
        return (ConnectionPointInput) ConnectionPointStorage.getPoint(inputLocation);
    }

    private void updateBeam() {
        if (hasBeam()) {
            beam.deprecate();
            beam = null;
        }

        if (!enabled) {
            return;
        }

        // TODO dynamically update beam max size
        this.beam = new DirectBlockDisplayBeam(
                new DirectSinglePulseTickerFactory(
                        Material.WHITE_CONCRETE,
                        outputLocation,
                        inputLocation,
                        (float) (power / 40.0F)));
    }

    private boolean hasBeam() {
        return beam != null;
    }

    public void tick() {
        if (hasBeam()) {
            beam.tick();
        }
    }

    private void update() {
        getInput().getGroup().updateInfoDisplays();
        getOutput().getGroup().updateInfoDisplays();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup());
    }

    public void killBeam() {
        if (hasBeam()) {
            beam.remove();
        }
    }

    public void remove() {
        if (hasBeam()) {
            DeprecatedBeamStorage.add(beam);
            beam = null;
        }

        if (getOutput() != null && ConnectionPointStorage.hasGroup(getOutput().getGroup().getLocation())) {
            getOutput().unlink();
            BlockUpdateScheduler.scheduleUpdate(getOutput().getGroup());
        }

        if (getInput() != null && ConnectionPointStorage.hasGroup(getInput().getGroup().getLocation())) {
            getInput().unlink();
            BlockUpdateScheduler.scheduleUpdate(getInput().getGroup());
        }
    }

    public void setEnabled(boolean enabled) {
        if (enabled == this.enabled) {
            return;
        }

        this.enabled = enabled;

        if (!enabled) {
            power = 0;
            frequency = 0;
            phase = 0;
        }

        updateBeam();
        update();
    }

    public void setPower(double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;

        updateBeam();
        update();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("enabled", enabled);
        map.put("power", power);
        map.put("frequency", frequency);
        map.put("phase", phase);
        map.put("outputLocation", outputLocation);
        map.put("inputLocation", inputLocation);
        map.put("beam", beam);
        return map;
    }

    public static Link deserialize(Map<String, Object> map) {
        return new Link(
                (boolean) map.get("enabled"),
                (double) map.get("power"),
                (double) map.get("frequency"),
                (int) map.get("phase"),
                (Location) map.get("outputLocation"),
                (Location) map.get("inputLocation"),
                (DirectBlockDisplayBeam) map.get("beam"));
    }
}
