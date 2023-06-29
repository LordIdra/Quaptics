package org.metamechanists.quaptics.connections;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.beams.DirectBeam;
import org.metamechanists.quaptics.beams.ticker.DirectTicker;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.storage.DataTraverser;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;
import org.metamechanists.quaptics.utils.id.LinkID;
import org.metamechanists.quaptics.utils.id.TickerID;

public class Link {
    private final static float MAX_BEAM_SIZE = 0.095F;
    @Getter
    private final LinkID ID;
    private final ConnectionPointID outputID;
    private final ConnectionPointID inputID;
    private final double maxPower;
    private TickerID tickerID;
    @Getter
    private boolean enabled;
    @Getter
    private double power;
    @Getter
    private double frequency;
    @Getter
    private int phase;

    public Link(ConnectionPointID inputID, ConnectionPointID outputID) {
        this.inputID = inputID;
        this.outputID = outputID;
        this.ID = new LinkID(new DisplayGroup(getInput().getLocation(), 0, 0).getParentUUID());
        this.maxPower = getOutput().getGroup().getBlock().maxPower;
        saveData(); // the points being linked will not be able to get the link from the ID without this line
        getInput().link(getID());
        getOutput().link(getID());
        BlockUpdateScheduler.scheduleUpdate(getOutput().getGroup().getID());
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
        update();
    }

    public Link(LinkID ID) {
        final DataTraverser traverser = new DataTraverser(ID);
        final JsonObject mainSection = traverser.getData();
        this.ID = ID;
        this.outputID = new ConnectionPointID(mainSection.get("outputID").getAsString());
        this.inputID = new ConnectionPointID(mainSection.get("inputID").getAsString());
        this.tickerID = mainSection.get("tickerID").getAsString().equals("null")
                ? null
                : new TickerID(mainSection.get("tickerID").getAsString());
        this.enabled = mainSection.get("enabled").getAsBoolean();
        this.power = mainSection.get("power").getAsDouble();
        this.frequency = mainSection.get("frequency").getAsDouble();
        this.phase = mainSection.get("phase").getAsInt();
        this.maxPower = mainSection.get("maxPower").getAsDouble();
    }

    public void saveData() {
        final DataTraverser traverser = new DataTraverser(getID());
        final JsonObject mainSection = traverser.getData();
        mainSection.add("outputID", new JsonPrimitive(outputID.toString()));
        mainSection.add("inputID", new JsonPrimitive(inputID.toString()));
        mainSection.add("tickerID", new JsonPrimitive(tickerID == null ? "null" : tickerID.toString()));
        mainSection.add("enabled", new JsonPrimitive(enabled));
        mainSection.add("power", new JsonPrimitive(power));
        mainSection.add("frequency", new JsonPrimitive(frequency));
        mainSection.add("phase", new JsonPrimitive(phase));
        mainSection.add("maxPower", new JsonPrimitive(maxPower));
        traverser.save();
    }

    public ConnectionPointOutput getOutput() {
        return (ConnectionPointOutput) outputID.get();
    }

    public ConnectionPointInput getInput() {
        return (ConnectionPointInput) inputID.get();
    }

    private boolean hasBeam() {
        return tickerID != null;
    }

    @Contract(" -> new")
    private @NotNull DirectBeam getBeam() {
        return new DirectBeam(tickerID);
    }

    public void remove() {
        if (hasBeam()) {
            getBeam().deprecate();
            tickerID = null;
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
            tickerID = null;
        }

        if (!enabled) {
            return;
        }

        this.tickerID = new DirectBeam(new DirectTicker(
                Material.WHITE_CONCRETE,
                getOutput().getLocation(),
                getInput().getLocation(),
                Math.min((float)(power / maxPower) * MAX_BEAM_SIZE, MAX_BEAM_SIZE)))
                .getID();
    }

    private void update() {
        updateBeam();
        saveData();
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

        update();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
    }

    public void setPower(double power) {
        // TODO how can we limit the size of power changes?
        if (this.power == power) {
            return;
        }

        this.power = power;

        update();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
    }

    public void setFrequency(double frequency) {
        // TODO how can we limit the size of frequency changes?
        if (this.frequency == frequency) {
            return;
        }

        this.frequency = frequency;

        update();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
    }

    public void setPhase(int phase) {
        if (this.phase == phase) {
            return;
        }

        this.phase = phase;

        update();
        BlockUpdateScheduler.scheduleUpdate(getInput().getGroup().getID());
    }
}