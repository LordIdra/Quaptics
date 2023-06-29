package org.metamechanists.quaptics.implementation.blocks.concentrators;

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
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.EnergyConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.ArrayList;
import java.util.List;

public class EnergyConcentrator extends EnergyConnectedBlock {
    private final Material material;
    private final Vector outputLocation = new Vector(0.0F, 0.0F, connectionRadius);
    private final Vector3f mainDisplaySize = new Vector3f(displayRadius, displayRadius, connectionRadius*2);
    private final double emissionPower;

    public EnergyConcentrator(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                              Material material, float width, float length,
                              int capacity, int consumption, double emissionPower, double maxPower) {
        super(group, item, recipeType, recipe, width/2, length/2, maxPower, capacity, consumption);
        this.material = material;
        this.emissionPower = emissionPower;
    }

    private BlockDisplay generateMainBlockDisplay(@NotNull Location from, Location to) {
        return new BlockDisplayBuilder(from.toCenterLocation())
                .setMaterial(material)
                .setTransformation(Transformations.lookAlong(mainDisplaySize, Transformations.getDirection(from, to)))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location, location.clone().add(rotateVectorByEyeDirection(player, INITIAL_LINE))));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointOutput(groupID, "output", formatPointLocation(player, location, outputLocation)));
        return points;
    }

    @Override
    public void onSlimefunTick(@NotNull Block block, SlimefunItem item, Config data) {
        super.onSlimefunTick(block, item, data);
        final ConnectionGroupID groupID = new ConnectionGroupID(BlockStorage.getLocationInfo(block.getLocation(), Keys.CONNECTION_GROUP_ID));
        final ConnectionGroup group = groupID.get();
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        if (!output.hasLink()) {
            return;
        }

        if (isPowered(block.getLocation())) {
            output.getLink().setPower(emissionPower);
            output.getLink().setEnabled(true);
            return;
        }

        output.getLink().setEnabled(false);
    }

    @Override
    public void connect(ConnectionPointID from, ConnectionPointID to) {
        super.connect(from, to);
        final DisplayGroup fromDisplayGroup = getDisplayGroup(from.get().getGroup().getLocation());
        fromDisplayGroup.removeDisplay("main").remove();
        fromDisplayGroup.addDisplay("main", generateMainBlockDisplay(
                from.get().getGroup().getLocation(),
                to.get().getGroup().getLocation()));
    }
}
