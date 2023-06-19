package org.metamechanists.death_lasers.connections.points;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.lasers.DeprecatedBeams;
import org.metamechanists.death_lasers.lasers.beam.Beam;
import org.metamechanists.death_lasers.lasers.beam.DirectBlockDisplayBeam;
import org.metamechanists.death_lasers.lasers.ticker.factory.DirectSinglePulseTickerFactory;

public class ConnectionPointOutput extends ConnectionPoint {
    private ConnectionPointInput target;
    private Beam beam;

    public ConnectionPointOutput(String name, Location location) {
        super(name, location,
                Material.LIME_STAINED_GLASS,
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
        if (target != null) {
            target.unlink();
        }
        if (beam != null) {
            DeprecatedBeams.add(beam);
            beam = null;
        }
    }

    public void killBeam() {
        if (beam != null) {
            beam.remove();
        }
    }

    public void setPowered(boolean powered) {
        this.beam.setPowered(powered);
        final ConnectionGroup targetGroup = ConnectionPointStorage.getGroupFromPointLocation(target.location);
        targetGroup.getBlock().onInputUpdated(target);
    }

    public boolean isPowered() {
        if (beam == null) {
            return false;
        }
        return beam.isPowered();
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
        beam = new DirectBlockDisplayBeam(
                new DirectSinglePulseTickerFactory(
                        Material.WHITE_CONCRETE,
                        this.location,
                        target.location));
    }

    public void unlink() {
        target.unlink();
        target = null;
        blockDisplay.setBrightness(disconnectedBrightness);
        DeprecatedBeams.add(beam);
        beam = null;
    }
}
