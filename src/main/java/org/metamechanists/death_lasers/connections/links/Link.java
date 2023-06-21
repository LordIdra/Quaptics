package org.metamechanists.death_lasers.connections.links;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.metamechanists.death_lasers.connections.BlockUpdateScheduler;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.beams.DeprecatedBeamStorage;
import org.metamechanists.death_lasers.beams.beam.Beam;
import org.metamechanists.death_lasers.beams.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.beams.ticker.factory.DirectSinglePulseTickerFactory;

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
        final ConnectionGroup inputGroup = ConnectionPointStorage.getGroupFromPointLocation(input.getLocation());
        final ConnectionGroup outputGroup = ConnectionPointStorage.getGroupFromPointLocation(output.getLocation());
        BlockUpdateScheduler.scheduleUpdate(inputGroup);
        BlockUpdateScheduler.scheduleUpdate(outputGroup);
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

        final ConnectionGroup inputGroup = ConnectionPointStorage.getGroupFromPointLocation(input.getLocation());
        final ConnectionGroup outputGroup = ConnectionPointStorage.getGroupFromPointLocation(output.getLocation());

        input.unlink();
        output.unlink();

        BlockUpdateScheduler.scheduleUpdate(inputGroup);
        BlockUpdateScheduler.scheduleUpdate(outputGroup);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled && hasBeam()) {
            beam.deprecate();
            beam = null;
            update();
        }

        else if (enabled && !hasBeam()) {
            this.beam = new DirectBlockDisplayBeam(
                    new DirectSinglePulseTickerFactory(
                            Material.WHITE_CONCRETE,
                            this.output.getLocation(),
                            this.input.getLocation()));
            update();
        }
    }
}
