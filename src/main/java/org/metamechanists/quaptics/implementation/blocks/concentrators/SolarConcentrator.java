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

public class SolarConcentrator extends ConnectedBlock {
    private final Vector outputLocation = new Vector(0.0F, -0.25F, 0.0F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/2), 0.0F, 0.0F);
    private final Vector3f mainDisplaySize = new Vector3f(0.9F, 0.9F, 0.9F);
    private final Vector3f mainDisplayOffset = new Vector3f(0, 0.0F, 0.9F);
    private final double emissionPower;

    public SolarConcentrator(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double emissionPower) {
        super(group, item, recipeType, recipe, 0);
        this.emissionPower = emissionPower;
    }

    private ItemDisplay generateMainBlockDisplay(@NotNull Location from) {
        return new ItemDisplayBuilder(from.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.GLASS_PANE)
                .setTransformation(Transformations.rotateAndScale(mainDisplaySize, mainDisplayRotation)
                        .translate(mainDisplayOffset))
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
        final float angleY = (float) Math.atan2(direction.x, direction.z);
        final Vector3f directionXZ = INITIAL_LINE.clone().toVector3f().rotateY(angleY);
        directionXZ.mul(directionXZ.x/mainDisplaySize.x > directionXZ.z/mainDisplaySize.z
                ? mainDisplaySize.x / directionXZ.x
                : mainDisplaySize.z / directionXZ.z);
        return ConnectionPoint.fromID(from).getLocation().clone().add(Vector.fromJOML(directionXZ));
    }
}
