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

    public void tick() {
        beam.tick();
    }

    private void update() {
        final ConnectionGroup inputGroup = ConnectionPointStorage.getGroupFromPointLocation(input.getLocation());
        inputGroup.getBlock().onIncomingLinkUpdated(input);
    }

    public void killBeam() {
        if (beam != null) {
            beam.remove();
        }
    }

    public void remove() {
        if (beam != null) {
            DeprecatedBeams.add(beam);
            beam = null;
        }
        input.unlink();
        output.unlink();
        update();
    }

    public void setPowered(boolean powered) {
        this.powered = powered;

        if (!powered && beam != null) {
            beam.deprecate();
            beam = null;
            update();
        }

        else if (powered && beam == null) {
            this.beam = new DirectBlockDisplayBeam(
                    new DirectSinglePulseTickerFactory(
                            Material.WHITE_CONCRETE,
                            this.output.getLocation(),
                            this.input.getLocation()));
            update();
        }
    }
}
