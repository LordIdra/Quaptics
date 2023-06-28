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
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SolarConcentrator extends ConnectedBlock {
    private final Vector outputLocation = new Vector(0.0F, 0.0F, 0.45F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/2), 0.0F, 0.0F);
    private final Vector3f mainDisplaySize = new Vector3f(0.9F, 0.9F, 0.9F);
    private final Vector3f mainDisplayOffset = new Vector3f(0.45F, -0.45F, 0.45F);
    private final double emissionPower;

    public SolarConcentrator(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                             double emissionPower, double maxPower) {
        super(group, item, recipeType, recipe, 0);
        this.emissionPower = emissionPower;
    }

    private ItemDisplay generateMainBlockDisplay(@NotNull Location from) {
        return new ItemDisplayBuilder(from.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.GLASS_PANE)
                .setTransformation(new Matrix4f()
                        .translate(mainDisplayOffset)
                        .mul(Transformations.rotateAndScale(mainDisplaySize, mainDisplayRotation)))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location));
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
        final ConnectionGroupID ID = new ConnectionGroupID(BlockStorage.getLocationInfo(block.getLocation(), Keys.CONNECTION_GROUP_ID));
        final ConnectionGroup group = ConnectionGroup.fromID(ID);
        final ConnectionPointOutput output = (ConnectionPointOutput) group.getPoint("output");

        if (!output.hasLink()) {
            return;
        }

        if (block.getWorld().isDayTime()) {
            output.getLink().setPower(emissionPower);
            output.getLink().setEnabled(true);
            return;
        }

        output.getLink().setEnabled(false);
    }

    @Override
    public void connect(ConnectionPointID from, ConnectionPointID to) {
        ConnectionPoint.fromID(from).getGroup().changePointLocation(from, calculatePointLocationSquare(from, to));
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }

    private @NotNull Location calculatePointLocationSquare(ConnectionPointID from, ConnectionPointID to) {
        final Vector3f direction = Transformations.getDirection(
                ConnectionPoint.fromID(from).getLocation(),
                ConnectionPoint.fromID(to).getLocation());
        final Vector3f newDirection = new Vector3f();
        if      (abs(direction.x) >= abs(direction.z) && direction.x > 0) { newDirection.x = 0.45F; }
        else if (abs(direction.x) >= abs(direction.z) && direction.x < 0) { newDirection.x = -0.45F; }
        else if (abs(direction.x) <  abs(direction.z) && direction.z > 0) { newDirection.z = 0.45F; }
        else if (abs(direction.x) <  abs(direction.z) && direction.z < 0) { newDirection.z = -0.45F; }
        return ConnectionPoint.fromID(from).getGroup().getLocation().clone().add(0.5, 0.5, 0.5).add(Vector.fromJOML(newDirection));
    }
}
