package org.metamechanists.death_lasers.connections.links;

import lombok.Getter;
import org.bukkit.Material;
import org.metamechanists.death_lasers.beams.DeprecatedBeamStorage;
import org.metamechanists.death_lasers.beams.beam.Beam;
import org.metamechanists.death_lasers.beams.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.connections.BlockUpdateScheduler;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;

public class Link {
    @Getter
    private boolean enabled;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;

    @Getter
    private final ConnectionPointOutput output;
    @Getter
    private final ConnectionPointInput input;
    private Beam beam;

    public Link(ConnectionPointInput input, ConnectionPointOutput output) {
        this.input = input;
        this.output = output;
        input.link(this);
        output.link(this);
        BlockUpdateScheduler.scheduleUpdate(output.getGroup());
        update();
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
        input.getGroup().updateInfoDisplays();
        output.getGroup().updateInfoDisplays();
        BlockUpdateScheduler.scheduleUpdate(input.getGroup());
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

        if (ConnectionPointStorage.hasGroup(output.getGroup().getLocation())) {
            output.unlink();
            BlockUpdateScheduler.scheduleUpdate(output.getGroup());
        }

        if (ConnectionPointStorage.hasGroup(input.getGroup().getLocation())) {
            input.unlink();
            BlockUpdateScheduler.scheduleUpdate(input.getGroup());
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
            if (hasBeam()) {
                beam.deprecate();
                beam = null;
            }
            update();
            return;
        }

        if (!hasBeam()) {
            this.beam = new DirectBlockDisplayBeam(
                    new DirectSinglePulseTickerFactory(
                            Material.WHITE_CONCRETE,
                            this.output.getLocation(),
                            this.input.getLocation()));
            update();
        }
    }

    public void setPower(double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;
        update();
    }
}
