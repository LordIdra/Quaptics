package org.metamechanists.quaptics.implementation.blocks.testing;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupId;

import java.util.List;

public class OscillatingConcentrator extends ConnectedBlock {
    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final Vector3f mainDisplaySize = new Vector3f(settings.getDisplayRadius()*2);

    public OscillatingConcentrator(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe,
                                   final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, final @NotNull Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new ItemDisplayBuilder(location.clone().toCenterLocation())
                .setMaterial(Material.BLACK_STAINED_GLASS_PANE)
                .setTransformation(Transformations.unadjustedRotateAndScale(mainDisplaySize, new Vector3f((float)(Math.PI/2), 0.0F, settings.getRotationY())))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPointOutput(groupId, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        super.onSlimefunTick(block, item, data);
        final Location location = block.getLocation();
        final double power = Bukkit.getCurrentTick() % 2 == 0 // lol
                ? settings.getEmissionPower()
                : 0;
        getLink(location, "output").ifPresent(link -> link.setPower(power));
    }
}
