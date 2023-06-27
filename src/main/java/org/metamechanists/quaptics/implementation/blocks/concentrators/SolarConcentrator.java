package org.metamechanists.quaptics.implementation.blocks.concentrators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
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
import org.metamechanists.quaptics.implementation.tools.TargetingWand;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;
import org.metamechanists.quaptics.utils.id.ConnectionPointID;

import java.util.ArrayList;
import java.util.List;

public class SolarConcentrator extends ConnectedBlock {
    private final Vector outputLocation = new Vector(0.0F, 0.0F, 0.0F);
    private final Vector3f mainDisplaySize = new Vector3f(7.0F, 0.05F, 7.0F);
    private final double emissionPower;

    public SolarConcentrator(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, double emissionPower) {
        super(group, item, recipeType, recipe, 0);
        this.emissionPower = emissionPower;
        addItemHandler(onUse());
    }

    private BlockUseHandler onUse() {
        return event -> {
          event.cancel();

          final Block block = event.getClickedBlock().orElse(null);
          if (block == null) {
              // I have no clue how this would happen but 🤷
              return;
          }

          final ItemStack heldItem = event.getItem();
          if (!(SlimefunItem.getByItem(heldItem) instanceof TargetingWand wand)) {
              return;
          }

          wand.use(event.getPlayer(), getGroup(block.getLocation()).getOutput("output").getID(), heldItem);
        };
    }

    private BlockDisplay generateMainBlockDisplay(@NotNull Location from, Location to) {
        return new BlockDisplayBuilder(from.clone().add(RELATIVE_CENTER))
                .setMaterial(Material.GLASS)
                .setTransformation(Transformations.adjustedScale(mainDisplaySize))
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
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
