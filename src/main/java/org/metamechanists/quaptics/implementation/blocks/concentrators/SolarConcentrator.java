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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.base.Settings;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.List;
import java.util.Optional;

public class SolarConcentrator extends ConnectedBlock {
    private final float rotationY;
    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f mainDisplaySize = new Vector3f(settings.getDisplayRadius()*2);

    public SolarConcentrator(final ItemGroup group, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                             final Settings settings, final float rotationY) {
        super(group, item, recipeType, recipe, settings);
        this.rotationY = rotationY;
    }

    private ItemDisplay generateMainBlockDisplay(@NotNull final Location from) {
        final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/2), 0.0F, rotationY);
        return new ItemDisplayBuilder(from.toCenterLocation())
                .setMaterial(Material.GLASS_PANE)
                .setTransformation(Transformations.unadjustedRotateAndScale(mainDisplaySize, mainDisplayRotation))
                .build();
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, final @NotNull Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", generateMainBlockDisplay(location));
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        super.onSlimefunTick(block, item, data);

        final Optional<ConnectionGroup> group = getGroup(block.getLocation());
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

        if (block.getWorld().isDayTime()) {
            outputLink.get().setAttributes(settings.getEmissionPower(), 0, 0, true);
            return;
        }

        outputLink.get().setEnabled(false);
    }
}
