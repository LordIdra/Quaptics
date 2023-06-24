package org.metamechanists.quaptics.connections;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.beams.ticker.factory.DirectTickerFactory;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

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
    private double maxPower;
    private final ConnectionPointID outputID;
    private final ConnectionPointID inputID;
    private DirectBeam beam;


    public Link(ConnectionPointInput input, ConnectionPointOutput output) {
        this.inputID = input.getId();
        this.outputID = output.getId();
        this.maxPower = input.getGroup().getBlock().maxPower;
        input.link(this);
        output.link(this);
        BlockUpdateScheduler.scheduleUpdate(output.getGroup().getId());
        BlockUpdateScheduler.scheduleUpdate(input.getGroup().getId());
        updatePanels();
    }

    private Link(boolean enabled, double power, double frequency, int phase, ConnectionPointID outputID, ConnectionPointID inputID, DirectBeam beam) {
        this.enabled = enabled;
        this.power = power;
        this.frequency = frequency;
        this.phase = phase;
        this.outputID = outputID;
        this.inputID = inputID;
        this.beam = beam;
    }

    public ConnectionPointOutput getOutput() {
        return (ConnectionPointOutput) ConnectionPointStorage.getPoint(outputID);
    }

    public ConnectionPointInput getInput() {
        return (ConnectionPointInput) ConnectionPointStorage.getPoint(inputID);
    }

    private boolean hasBeam() {
        return beam != null;
    }

    public void tick() {
        if (hasBeam()) {
            beam.tick();
        }
    }

    public void remove() {
        if (hasBeam()) {
            beam.deprecate();
            beam = null;
        }

        if (getOutput() != null && ConnectionPointStorage.hasGroup(getOutput().getGroup().getId())) {
            getOutput().unlink();
            BlockUpdateScheduler.scheduleUpdate(getOutput().getGroup().getId());
        }

        if (getInput() != null && ConnectionPointStorage.hasGroup(getInput().getGroup().getId())) {
            getInput().unlink();
            BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getId());
        }
    }

    private void updateBeam() {
        if (hasBeam()) {
            beam.deprecate();
            beam = null;
        }

        if (!enabled) {
            return;
        }

        this.beam = new DirectBeam(new DirectTickerFactory(
                        Material.WHITE_CONCRETE,
                        getOutput().getLocation(),
                        getInput().getLocation(),
                        (float)(power / maxPower) * 0.095F));
    }

    private void updatePanels() {
        getInput().getGroup().updatePanels();
        getOutput().getGroup().updatePanels();
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
        updatePanels();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getId());
    }

    public void setPower(double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;

        updateBeam();
        updatePanels();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getId());
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("enabled", enabled);
        map.put("power", power);
        map.put("frequency", frequency);
        map.put("phase", phase);
        map.put("outputID", outputID);
        map.put("inputID", inputID);
        map.put("beam", beam);
        return map;
    }

    public static Link deserialize(Map<String, Object> map) {
        return new Link(
                (boolean) map.get("enabled"),
                (double) map.get("power"),
                (double) map.get("frequency"),
                (int) map.get("phase"),
                (ConnectionPointID) map.get("outputID"),
                (ConnectionPointID) map.get("inputID"),
                (DirectBeam) map.get("beam"));
    }
}
