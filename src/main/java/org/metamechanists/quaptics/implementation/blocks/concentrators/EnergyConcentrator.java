package org.metamechanists.quaptics.implementation.blocks.concentrators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.EnergyConnectedBlock;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

import java.util.List;

public class EnergyConcentrator extends EnergyConnectedBlock {
    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f mainDisplaySize = new Vector3f(settings.getDisplayRadius(), settings.getDisplayRadius(), settings.getConnectionRadius()*2);

    public EnergyConcentrator(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                              final Settings settings, final int capacity, final int consumption) {
        super(group, item, recipeType, recipe, settings, capacity, consumption);
    }

    private BlockDisplay generateMainBlockDisplay(@NotNull final Location from, final Location to) {
        return new BlockDisplayBuilder(from.toCenterLocation())
                .setMaterial(settings.getTier().concreteMaterial)
                .setTransformation(Transformations.lookAlong(mainDisplaySize, Transformations.getDirection(from, to)))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, final @NotNull Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location, location.clone().add(rotateVectorByEyeDirection(player, INITIAL_LINE))));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        super.onSlimefunTick(block, item, data);

        final Location location = block.getLocation();
        final ConnectionGroup group = getGroup(location);
        if (group == null) {
            return;
        }


        final ConnectionPointOutput output = group.getOutput("output");
        if (output == null || !output.hasLink()) {
            return;
        }

        if (isPowered(location)) {
            output.getLink().setAttributes(settings.getEmissionPower(), 0, 0, true);
            return;
        }

        output.getLink().setEnabled(false);
    }

    @Override
    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {
        super.connect(from, to);

        final ConnectionPoint fromPoint = from.get();
        final ConnectionPoint toPoint = to.get();
        if (fromPoint == null || toPoint == null) {
            return;
        }

        final ConnectionGroup fromGroup = fromPoint.getGroup();
        final ConnectionGroup toGroup = toPoint.getGroup();
        if (fromGroup == null || toGroup == null) {
            return;
        }

        final Location fromLocation = fromGroup.getLocation();
        final Location toLocation = toGroup.getLocation();
        if (fromLocation == null || toLocation == null) {
            return;
        }

        final DisplayGroup fromDisplayGroup = getDisplayGroup(fromLocation);
        if (fromDisplayGroup != null) {
            final Display display = fromDisplayGroup.removeDisplay("main");
            if (display != null) {
                display.remove();
            }

            fromDisplayGroup.addDisplay("main", generateMainBlockDisplay(fromLocation, toLocation));
        }
    }
}
