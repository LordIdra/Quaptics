package org.metamechanists.quaptics.implementation.blocks.base;

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
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.id.DisplayGroupId;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

import static dev.sefiraat.sefilib.slimefun.blocks.DisplayGroupBlock.KEY_UUID;

public abstract class DisplayGroupTickerBlock extends SlimefunItem {
    private static final Vector CENTER_VECTOR = new Vector(0.5, 0.5, 0.5);
    protected static final Vector INITIAL_LINE = new Vector(0, 0, 1);

    protected DisplayGroupTickerBlock(
            final ItemGroup group, final SlimefunItemStack item,
            final RecipeType recipeType, final ItemStack[] recipe) {
        super(group, item, recipeType, recipe);
    }

    @NotNull
    protected Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }

    @ParametersAreNonnullByDefault
    protected abstract void addDisplays(DisplayGroup displayGroup, Location location, Player player);

    protected static @NotNull Vector rotateVectorByEyeDirection(@NotNull final Player player, @NotNull final Vector vector) {
        final double rotationAngle = Transformations.yawToCardinalDirection(player.getEyeLocation().getYaw());
        return vector.clone().rotateAroundY(rotationAngle);
    }

    protected static @NotNull Location formatPointLocation(final Player player, @NotNull final Location location, final Vector relativeLocation) {
        final Vector newRelativeLocation = rotateVectorByEyeDirection(player, relativeLocation);
        newRelativeLocation.add(CENTER_VECTOR);
        return location.clone().add(newRelativeLocation);
    }

    protected void onPlace(@NotNull final BlockPlaceEvent event) {}

    protected void onBreak(@NotNull final Location location) {}

    protected void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {}

    @SuppressWarnings("unused")
    public void onQuapticTick(@NotNull final ConnectionGroup group) {}

    @Override
    @OverridingMethodsMustInvokeSuper
    public void preRegister() {
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull final BlockPlaceEvent event) {
                        final Location location = event.getBlock().getLocation();
                        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
                        addDisplays(displayGroup, location, event.getPlayer());
                        setId(displayGroup, location);
                        event.getBlock().setType(getBaseMaterial());
                        onPlace(event);
                    }
                },

                new BlockBreakHandler(false, false) {
                    @Override
                    @ParametersAreNonnullByDefault
                    public void onPlayerBreak(final BlockBreakEvent event, final ItemStack item, final List<ItemStack> drops) {
                        final Location location = event.getBlock().getLocation();
                        onBreak(location);
                        getDisplayGroup(location.clone()).ifPresent(DisplayGroup::remove);
                        event.getBlock().setType(Material.AIR);
                    }
                },

                new BlockTicker() {
                    @Override
                    public void tick(final Block block, final SlimefunItem item, final Config data) {
                        onSlimefunTick(block, item, data);
                    }

                    @Override
                    public boolean isSynchronized() {
                        return true;
                    }
                }
        );
    }

    private static void setId(@NotNull final DisplayGroup displayGroup, final Location location) {
        BlockStorage.addBlockInfo(location, KEY_UUID, displayGroup.getParentUUID().toString());
    }

    public static Optional<DisplayGroupId> getDisplayGroupId(final Location location) {
        final String uuid = BlockStorage.getLocationInfo(location, KEY_UUID);
        return Optional.ofNullable(uuid).map(DisplayGroupId::new);
    }

    public static Optional<DisplayGroup> getDisplayGroup(final Location location) {
        return getDisplayGroupId(location).map(displayGroupId -> DisplayGroup.fromUUID(displayGroupId.getUUID()));
    }

    public static Optional<Display> getDisplay(final Location location, final String name) {
        return getDisplayGroup(location).map(displayGroup -> displayGroup.getDisplays().get(name));
    }

    protected static void removeDisplay(final @NotNull DisplayGroup displayGroup, final String name) {
        final Display display = displayGroup.removeDisplay(name);
        if (display != null) {
            display.remove();
        }
    }
}
