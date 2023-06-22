package org.metamechanists.death_lasers.implementation.blocks;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
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
import org.metamechanists.death_lasers.implementation.base.ConnectedBlock;
import org.metamechanists.death_lasers.utils.DisplayUtils;
import org.metamechanists.death_lasers.utils.Keys;
import org.metamechanists.death_lasers.utils.id.ConnectionGroupID;

import java.util.ArrayList;
import java.util.List;

public class Emitter extends ConnectedBlock {
    private final double emissionPower;

    public Emitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                   int capacity, int consumption, double emissionPower) {
        super(group, item, recipeType, recipe, 0, capacity, consumption);
        this.emissionPower = emissionPower;
    }

    private BlockDisplay generateMainBlockDisplay(Location from, Location to) {
        return DisplayUtils.spawnBlockDisplay(
                from.clone().add(0.5, 0.5, 0.5),
                Material.PURPLE_CONCRETE,
                DisplayUtils.faceTargetTransformation(from, to, new Vector3f(0.3F, 0.3F, (2*getRadius()))));
    }

    @Override
    protected DisplayGroup generateDisplayGroup(Player player, Location location) {
        // Height/width are zero to prevent the large interaction entity from obstructing the player
        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
        displayGroup.addDisplay("main", generateMainBlockDisplay(location, location.clone().add(accountForPlayerYaw(player, new Vector(0, 0, 1)))));
        return displayGroup;
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointOutput("output", formatRelativeLocation(player, location, new Vector(0.0F, 0.0F, getRadius()))));
        return points;
    }

    @Override
    public void onSlimefunTick(Block block, SlimefunItem item, Config data) {
        final ConnectionGroupID id = new ConnectionGroupID(BlockStorage.getLocationInfo(block.getLocation(), Keys.CONNECTION_GROUP_ID));
        final ConnectionGroup group = ConnectionPointStorage.getGroup(id);
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");
        final int charge = getCharge(block.getLocation(), data);

        if (charge >= consumption) {
            removeCharge(block.getLocation(), consumption);
            if (output.hasLink()) {
                final Link link = output.getLink();
                link.setPower(emissionPower);
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
    public void connect(ConnectionPoint from, ConnectionPoint to) {
        super.connect(from, to);
        final Location fromGroupLocation = from.getGroup().getLocation();
        final Location toGroupLocation = to.getGroup().getLocation();
        final DisplayGroup fromDisplayGroup = getDisplayGroup(fromGroupLocation);

        fromDisplayGroup.removeDisplay("main").remove();
        fromDisplayGroup.addDisplay("main", generateMainBlockDisplay(fromGroupLocation, toGroupLocation));
    }

    @Override
    protected float getRadius() {
        return 0.45F;
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
