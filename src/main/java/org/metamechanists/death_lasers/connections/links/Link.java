package org.metamechanists.death_lasers.connections.links;

import lombok.Getter;
import org.bukkit.Material;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.points.ConnectionPointInput;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.lasers.DeprecatedBeams;
import org.metamechanists.death_lasers.lasers.beam.Beam;
import org.metamechanists.death_lasers.lasers.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.lasers.ticker.factory.DirectSinglePulseTickerFactory;

public class Link {
    @Getter
    private boolean powered;

    @Getter
    private final ConnectionPointOutput output;
    @Getter
    private final ConnectionPointInput input;
    private Beam beam;

    public Link(ConnectionPointInput input, ConnectionPointOutput output) {
        this.input = input;
        this.output = output;
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
        inputGroup.getBlock().onLinkUpdated(inputGroup);
        outputGroup.getBlock().onLinkUpdated(outputGroup);
    }

    public void killBeam() {
        if (hasBeam()) {
            beam.remove();
        }
    }

    public void remove() {
        if (hasBeam()) {
            DeprecatedBeams.add(beam);
            beam = null;
        }
        input.unlink();
        output.unlink();
        update();
    }

    public void setPowered(boolean powered) {
        this.powered = powered;

        if (!powered && hasBeam()) {
            beam.deprecate();
            beam = null;
            update();
        }

        else if (powered && !hasBeam()) {
            this.beam = new DirectBlockDisplayBeam(
                    new DirectSinglePulseTickerFactory(
                            Material.WHITE_CONCRETE,
                            this.output.getLocation(),
                            this.input.getLocation()));
            update();
        }
    }
}
