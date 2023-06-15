package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.lasers.DeprecatedBeams;
import org.metamechanists.death_lasers.lasers.Lasers;
import org.metamechanists.death_lasers.lasers.beam.Beam;
import org.metamechanists.death_lasers.lasers.beam.IntervalBlockDisplayBeam;
import org.metamechanists.death_lasers.lasers.ticker.factory.interval.IntervalLinearTimeTickerFactory;

public class ConnectionPointOutput extends ConnectionPoint {
    private ConnectionPointInput target;
    private Beam beam;

    public ConnectionPointOutput(Location location) {
        super(location,
                Material.LIME_STAINED_GLASS.createBlockData(),
                new Display.Brightness(15, 15),
                new Display.Brightness(3, 3));
    }

    @Override
    public void tick() {
        if (beam != null) {
            beam.tick();
        }
    }

    @Override
    public void remove() {
        blockDisplay.remove();
        interaction.remove();
        if (beam != null) {
            DeprecatedBeams.add(beam);
        }
    }

    @Override
    public void kill() {
        blockDisplay.remove();
        interaction.remove();
        if (beam != null) {
            beam.remove();
        }
    }

    public boolean hasLink() {
        return target != null;
    }

    public void link(ConnectionPointInput target) {
        if (this.target != null) {
            unlink();
        }
        this.target = target;
        this.target.link(this.location);
        blockDisplay.setBrightness(connectedBrightness);
        beam = new IntervalBlockDisplayBeam(
                new IntervalLinearTimeTickerFactory(
                        Lasers.testDisplay(),
                        this.location,
                        target.location,
                        100),
                Lasers.testTimer());
        beam.setPowered(true);
    }

    public void unlink() {
        target.unlink();
        target = null;
        blockDisplay.setBrightness(disconnectedBrightness);
        DeprecatedBeams.add(beam);
        beam = null;
    }
}
