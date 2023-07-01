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
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.EnergyConnectedBlock;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.ConnectionPointId;

import java.util.List;
import java.util.Optional;

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
        final Optional<ConnectionGroup> group = getGroup(location);
        if (group.isEmpty()) {
            return;
        }

        final Optional<ConnectionPointOutput> output = group.get().getOutput("output");
        if (output.isEmpty()) {
            return;
        }

        final Optional<Link> outputLink = output.get().getLink();
        if (outputLink.isEmpty()) {
            return;
        }

        if (!isPowered(location)) {
            outputLink.get().setEnabled(false);
            return;
        }

        outputLink.get().setAttributes(settings.getEmissionPower(), 0, 0, true);
    }

    @Override
    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {
        super.connect(from, to);

        final Optional<ConnectionPoint> fromPoint = from.get();
        final Optional<ConnectionPoint> toPoint = to.get();
        if (fromPoint.isEmpty() || toPoint.isEmpty()) {
            return;
        }

        final Optional<ConnectionGroup> fromGroup = fromPoint.get().getGroup();
        final Optional<ConnectionGroup> toGroup = toPoint.get().getGroup();
        if (fromGroup.isEmpty() || toGroup.isEmpty()) {
            return;
        }

        final Optional<Location> fromLocation = fromGroup.get().getLocation();
        final Optional<Location> toLocation = toGroup.get().getLocation();
        if (fromLocation.isEmpty() || toLocation.isEmpty()) {
            return;
        }

        final Optional<DisplayGroup> fromDisplayGroup = getDisplayGroup(fromLocation.get());
        if (fromDisplayGroup.isEmpty()) {
            return;
        }

        final Display display = fromDisplayGroup.get().removeDisplay("main");
        if (display != null) {
            display.remove();
        }

        fromDisplayGroup.get().addDisplay("main", generateMainBlockDisplay(fromLocation.get(), toLocation.get()));
    }
}
