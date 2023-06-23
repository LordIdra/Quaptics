package org.metamechanists.quaptics.implementation.blocks;

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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPointStorage;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.EnergyConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.ArrayList;
import java.util.List;

public class Emitter extends EnergyConnectedBlock {
    private final Vector OUTPUT_LOCATION = new Vector(0.0F, 0.0F, getRadius());
    private final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.3F, 0.3F, (2*getRadius()));
    private final double emissionPower;

    public Emitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                   int capacity, int consumption, double emissionPower) {
        super(group, item, recipeType, recipe, 0, capacity, consumption);
        this.emissionPower = emissionPower;
    }

    private BlockDisplay generateMainBlockDisplay(Location from, Location to) {
        return new BlockDisplayBuilder(from.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.PURPLE_CONCRETE)
                .setTransformation(Transformations.lookAlong(MAIN_DISPLAY_SIZE, Transformations.getDirection(from, to)))
                .build();
    }

    @Override
    protected void addDisplays(DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location, location.clone().add(rotateVectorByEyeDirection(player, INITIAL_LINE))));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointOutput("output", formatPointLocation(player, location, OUTPUT_LOCATION)));
        return points;
    }

    @Override
    public void onSlimefunTick(Block block, SlimefunItem item, Config data) {
        super.onSlimefunTick(block, item, data);
        final ConnectionGroupID id = new ConnectionGroupID(BlockStorage.getLocationInfo(block.getLocation(), Keys.CONNECTION_GROUP_ID));
        final ConnectionGroup group = ConnectionPointStorage.getGroup(id);
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        if (!output.hasLink()) {
            return;
        }

        if (powered) {
            output.getLink().setPower(emissionPower);
            output.getLink().setEnabled(true);
            return;
        }

        output.getLink().setEnabled(false);
    }

    @Override
    public void connect(ConnectionPoint from, ConnectionPoint to) {
        super.connect(from, to);
        final DisplayGroup fromDisplayGroup = getDisplayGroup(from.getGroup().getLocation());
        fromDisplayGroup.removeDisplay("main").remove();
        fromDisplayGroup.addDisplay("main", generateMainBlockDisplay(from.getGroup().getLocation(), to.getGroup().getLocation()));
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
