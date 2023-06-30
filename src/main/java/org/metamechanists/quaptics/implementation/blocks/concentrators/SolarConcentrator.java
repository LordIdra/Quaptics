package org.metamechanists.quaptics.implementation.blocks.concentrators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.List;

public class SolarConcentrator extends ConnectedBlock {
    private final float rotationY;
    private final Vector outputLocation = new Vector(0.0F, 0.0F, connectionRadius);
    private final Vector3f mainDisplaySize = new Vector3f(displayRadius*2);
    private final double emissionPower;

    public SolarConcentrator(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                             float radius, float rotationY, double emissionPower, double maxPower) {
        super(group, item, recipeType, recipe, radius, radius, maxPower);
        this.rotationY = rotationY;
        this.emissionPower = emissionPower;
    }

    private ItemDisplay generateMainBlockDisplay(@NotNull Location from) {
        final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/2), 0.0F, rotationY);
        return new ItemDisplayBuilder(from.toCenterLocation())
                .setMaterial(Material.GLASS_PANE)
                .setTransformation(Transformations.unadjustedRotateAndScale(mainDisplaySize, mainDisplayRotation))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, Location location, Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        return List.of(new ConnectionPointOutput(groupID, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onSlimefunTick(@NotNull Block block, SlimefunItem item, Config data) {
        super.onSlimefunTick(block, item, data);
        final ConnectionPointOutput output = getGroup(block.getLocation()).getOutput("output");

        if (!output.hasLink()) {
            return;
        }

        if (block.getWorld().isDayTime()) {
            output.getLink().setAttributes(emissionPower, 0, 0, true);
            return;
        }

        output.getLink().setEnabled(false);
    }
}
