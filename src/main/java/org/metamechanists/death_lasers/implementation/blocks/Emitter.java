package org.metamechanists.death_lasers.implementation.blocks;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.connections.ConnectionGroup;
import org.metamechanists.death_lasers.connections.ConnectionPointStorage;
import org.metamechanists.death_lasers.connections.links.Link;
import org.metamechanists.death_lasers.connections.points.ConnectionPoint;
import org.metamechanists.death_lasers.connections.points.ConnectionPointOutput;
import org.metamechanists.death_lasers.implementation.abstracts.ConnectedBlock;
import org.metamechanists.death_lasers.utils.DisplayUtils;

import java.util.HashMap;
import java.util.Map;

public class Emitter extends ConnectedBlock {
    private final double transmissionPower;

    public Emitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                   int capacity, int consumption, double transmissionPower) {
        super(group, item, recipeType, recipe, capacity, consumption);
        this.transmissionPower = transmissionPower;
    }

    private BlockDisplay generateMainBlockDisplay(Location from, Location to) {
        return DisplayUtils.spawnBlockDisplay(
                from.clone().add(0.5, 0.5, 0.5),
                Material.PURPLE_CONCRETE,
                DisplayUtils.faceTargetTransformation(from, to, new Vector3f(0.3F, 0.3F, 0.9F)));
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
        displayGroup.addDisplay("main", generateMainBlockDisplay(location, location.clone().add(accountForPlayerYaw(player, new Vector(0, 0, 1)))));
        return displayGroup;
    }

    @Override
    protected Map<String, ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final Map<String, ConnectionPoint> points = new HashMap<>();
        points.put("output", new ConnectionPointOutput("output", formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, 0.45F))));
        return points;
    }

    @Override
    public void onSlimefunTick(Block block, SlimefunItem item, Config data) {
        final ConnectionGroup group = ConnectionPointStorage.getGroup(block);
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");
        final int charge = getCharge(block.getLocation(), data);

        if (charge >= consumption) {
            removeCharge(block.getLocation(), consumption);
            if (output.hasLink()) {
                final Link link = output.getLink();
                link.setPower(transmissionPower);
                link.setEnabled(true);
            }
        } else {
            if (output.hasLink()) {
                final Link link = output.getLink();
                link.setEnabled(false);
            }
        }
    }

    @Override
    protected Location calculateNewLocation(ConnectionPoint from, ConnectionPoint to) {
        final Location fromGroupLocation = ConnectionPointStorage.getPoint(from.getLocation()).getGroup().getLocation();
        final Location toGroupLocation = ConnectionPointStorage.getPoint(to.getLocation()).getGroup().getLocation();
        final Vector radiusDirection = DisplayUtils.getDirection(fromGroupLocation, toGroupLocation).multiply(0.45F);
        return fromGroupLocation.clone().add(0.5, 0.5, 0.5).add(radiusDirection);
    }

    @Override
    public void connect(ConnectionPoint from, ConnectionPoint to) {
        super.connect(from, to);
        final Location fromGroupLocation = ConnectionPointStorage.getPoint(from.getLocation()).getGroup().getLocation();
        final Location toGroupLocation = ConnectionPointStorage.getPoint(to.getLocation()).getGroup().getLocation();
        final DisplayGroup fromDisplayGroup = getDisplayGroup(fromGroupLocation);

        fromDisplayGroup.removeDisplay("main").remove();
        fromDisplayGroup.addDisplay("main", generateMainBlockDisplay(fromGroupLocation, toGroupLocation));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
