package org.metamechanists.death_lasers.connections.links;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.metamechanists.death_lasers.beams.DeprecatedBeamStorage;
import org.metamechanists.death_lasers.beams.beam.Beam;
import org.metamechanists.death_lasers.beams.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;
import org.metamechanists.death_lasers.connections.BlockUpdateScheduler;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;

public class Link {
    @Getter
    private boolean enabled;
    @Getter
    @Setter
    private double power;
    @Getter
    @Setter
    private double frequency;
    @Getter
    @Setter
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
        BlockUpdateScheduler.scheduleUpdate(output.getGroup());
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

        input.unlink();
        output.unlink();

        update();
    }

    public void setEnabled(boolean enabled) {
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
}
