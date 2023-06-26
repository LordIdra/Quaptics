package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Material;
import org.metamechanists.metalib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.beams.beam.DirectBeam;
import org.metamechanists.quaptics.beams.ticker.factory.DirectTickerFactory;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.BeamID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.LinkID;

public class Link {
    @Getter
    private final LinkID ID;
    private final ConnectionPointID outputID;
    private final ConnectionPointID inputID;
    private BeamID beamID;
    @Getter
    private boolean enabled;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;
    private double maxPower;

    public Link(ConnectionPointID inputID, ConnectionPointID outputID) {
        this.inputID = inputID;
        this.outputID = outputID;
        this.ID = new LinkID(new DisplayGroup(getInput().getLocation(), 0, 0).getParentUUID());
        this.maxPower = getInput().getGroup().getBlock().maxPower;
        saveData();
        getInput().link(getID());
        getOutput().link(getID());
        BlockUpdateScheduler.scheduleUpdate(getOutput().getGroup().getID());
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
        updatePanels();
    }

    private Link(LinkID ID) {
        final DataTraverser traverser = new DataTraverser(ID);
        final JsonObject mainSection = traverser.getData();
        this.ID = ID;
        this.outputID = new ConnectionPointID(mainSection.get("outputID").getAsString());
        this.inputID = new ConnectionPointID(mainSection.get("inputID").getAsString());
        this.beamID = new BeamID(mainSection.get("beamID").getAsString());
        this.enabled = mainSection.get("enabled").getAsBoolean();
        this.power = mainSection.get("power").getAsDouble();
        this.frequency = mainSection.get("frequency").getAsDouble();
        this.phase = mainSection.get("phase").getAsInt();
        this.frequency = mainSection.get("maxPower").getAsDouble();
    }

    public static Link fromID(LinkID ID) {
        return new Link(ID);
    }

    public void saveData() {
        final DataTraverser traverser = new DataTraverser(getID());
        final JsonObject mainSection = traverser.getData();
        mainSection.add("outputID", new JsonPrimitive(outputID.toString()));
        mainSection.add("inputID", new JsonPrimitive(inputID.toString()));
        mainSection.add("beamID", new JsonPrimitive(beamID.toString()));
        mainSection.add("enabled", new JsonPrimitive(enabled));
        mainSection.add("power", new JsonPrimitive(power));
        mainSection.add("frequency", new JsonPrimitive(frequency));
        mainSection.add("phase", new JsonPrimitive(phase));
        mainSection.add("maxPower", new JsonPrimitive(maxPower));
        traverser.save();
    }

    public ConnectionPointOutput getOutput() {
        return (ConnectionPointOutput) ConnectionPoint.fromID(outputID);
    }

    public ConnectionPointInput getInput() {
        return (ConnectionPointInput) ConnectionPoint.fromID(inputID);
    }

    private boolean hasBeam() {
        return beamID != null;
    }

    private DirectBeam getBeam() {
        return DirectBeam.fromID(beamID);
    }

    public void tick() {
        if (hasBeam()) {
            getBeam().tick();
        }
    }

    public void remove() {
        if (hasBeam()) {
            getBeam().deprecate();
            beamID = null;
        }

        if (getOutput() != null && getOutput().getGroup() != null) {
            getOutput().unlink();
            BlockUpdateScheduler.scheduleUpdate(getOutput().getGroup().getID());
        }

        if (getInput() != null && getInput().getGroup() != null) {
            getInput().unlink();
            BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
        }
    }

    private void updateBeam() {
        if (hasBeam()) {
            getBeam().deprecate();
            beamID = null;
        }

        if (!enabled) {
            return;
        }

        this.beamID = new DirectBeam(new DirectTickerFactory(
                        Material.WHITE_CONCRETE,
                        getOutput().getLocation(),
                        getInput().getLocation(),
                        (float)(power / maxPower) * 0.095F))
                .getID();
        saveData();
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
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
        saveData();
    }

    public void setPower(double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;

        updateBeam();
        updatePanels();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
        saveData();
    }
}