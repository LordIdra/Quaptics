package org.metamechanists.quaptics.implementation.base;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.id.DisplayGroupID;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static dev.sefiraat.sefilib.slimefun.blocks.DisplayGroupBlock.KEY_UUID;

public abstract class DisplayGroupTickerBlock extends SlimefunItem {
    protected static final Vector INITIAL_LINE = new Vector(0, 0, 1);

    protected DisplayGroupTickerBlock(
            ItemGroup group, SlimefunItemStack item,
            RecipeType recipeType, ItemStack[] recipe) {
        super(group, item, recipeType, recipe);
    }

    @NotNull
    protected Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }

    protected abstract void addDisplays(DisplayGroup displayGroup, Location location, Player player);

    protected Vector rotateVectorByEyeDirection(@NotNull Player player, @NotNull Vector vector) {
        final double rotationAngle = Transformations.yawToCardinalDirection(player.getEyeLocation().getYaw());
        return vector.clone().rotateAroundY(rotationAngle);
    }

    protected Location formatPointLocation(Player player, @NotNull Location location, Vector relativeLocation) {
        final Vector newRelativeLocation = rotateVectorByEyeDirection(player, relativeLocation);
        newRelativeLocation.add(new Vector(0.5, 0.5, 0.5));
        return location.clone().add(newRelativeLocation);
    }

    protected void onPlace(BlockPlaceEvent event) {}

    protected void onBreak(BlockBreakEvent event) {}

    protected void onSlimefunTick(Block block, SlimefunItem item, Config data) {}

    @SuppressWarnings("unused")
    public void onQuapticTick(ConnectionGroup group) {}

    @Override
    @OverridingMethodsMustInvokeSuper
    public void preRegister() {
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull BlockPlaceEvent event) {
                        final Location location = event.getBlock().getLocation();
                        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
                        addDisplays(displayGroup, location, event.getPlayer());
                        setID(displayGroup, location);
                        event.getBlock().setType(getBaseMaterial());
                        BlockStorage.addBlockInfo(location, Keys.CONNECTION_GROUP_ID, displayGroup.getParentUUID().toString());
                        onPlace(event);
                    }
                },

                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(BlockBreakEvent event, ItemStack item, List<ItemStack> drops) {
                        final Location location = event.getBlock().getLocation();
                        final DisplayGroup displayGroup = getDisplayGroup(location.clone());
                        onBreak(event);
                        displayGroup.remove();
                        event.getBlock().setType(Material.AIR);
                    }
                },

                new BlockTicker() {
                    @Override
                    public void tick(Block block, SlimefunItem item, Config data) {
                        onSlimefunTick(block, item, data);
                    }

                    @Override
                    public boolean isSynchronized() {
                        return true;
                    }
                }
        );
    }

    private void setID(@NotNull DisplayGroup displayGroup, Location location) {
        BlockStorage.addBlockInfo(location, KEY_UUID, displayGroup.getParentUUID().toString());
    }

    public DisplayGroupID getDisplayGroupID(Location location) {
        final String uuid = BlockStorage.getLocationInfo(location, KEY_UUID);
        return new DisplayGroupID(uuid);
    }

    public DisplayGroup getDisplayGroup(Location location) {
        return DisplayGroup.fromUUID(getDisplayGroupID(location).getUUID());
    }
}
