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
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.ArrayList;
import java.util.List;

public class Collector extends ConnectedBlock {
    private final Vector OUTPUT_LOCATION = new Vector(0.0F, 0.0F, 0.0F);
    private final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.3F, 0.3F, (2*getRadius()));
    private final double emissionPower;

    public Collector(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double emissionPower) {
        super(group, item, recipeType, recipe, 0);
        this.emissionPower = emissionPower;
    }

    private BlockDisplay generateMainBlockDisplay(@NotNull Location from, Location to) {
        return new BlockDisplayBuilder(from.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.GLASS_PANE)
                .setTransformation(Transformations.lookAlong(MAIN_DISPLAY_SIZE, Transformations.getDirection(from, to)))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location, location.clone().add(rotateVectorByEyeDirection(player, INITIAL_LINE))));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();
        points.add(new ConnectionPointOutput(groupID, "output", formatPointLocation(player, location, OUTPUT_LOCATION)));
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
        super.connect(from, to);
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
