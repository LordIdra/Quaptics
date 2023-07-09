package org.metamechanists.quaptics.implementation.blocks.base;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.id.simple.DisplayGroupId;
import org.metamechanists.quaptics.utils.transformations.TransformationUtils;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

import static dev.sefiraat.sefilib.slimefun.blocks.DisplayGroupBlock.KEY_UUID;

public abstract class QuapticBlock extends SlimefunItem {
    private static final int BURNOUT_EXPLODE_VOLUME = 2;
    private static final float BURNOUT_EXPLODE_PITCH = 1.2F;
    private static final Vector CENTER_VECTOR = new Vector(0.5, 0.5, 0.5);
    protected static final Vector INITIAL_LINE = new Vector(0, 0, 1);
    @Getter
    protected final Settings settings;

    protected QuapticBlock(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe);
        this.settings = settings;
        addItemHandler(onRightClick());
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void preRegister() {
        addItemHandler(
                new BlockPlaceHandler(false) {
                    @Override
                    public void onPlayerPlace(@Nonnull final BlockPlaceEvent event) {
                        final Location location = event.getBlock().getLocation();
                        final DisplayGroup displayGroup = new DisplayGroup(location, 0, 0);
                        initDisplays(displayGroup, location, event.getPlayer());
                        setId(displayGroup, location);
                        event.getBlock().setType(getBaseMaterial());
                        initBlockStorage(location);
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

    @ParametersAreNonnullByDefault
    protected abstract void initDisplays(DisplayGroup displayGroup, Location location, Player player);
    protected void initBlockStorage(@NotNull final Location location) {}

    protected void onPlace(@NotNull final BlockPlaceEvent event) {}
    protected void onBreak(@NotNull final Location location) {}
    protected void onRightClick(@NotNull final Location location, @NotNull final Player player) {}
    protected void onShiftRightClick(@NotNull final Location location, @NotNull final Player player) {}
    @NotNull
    private BlockUseHandler onRightClick() {
        return event -> {
            final Block block = event.getClickedBlock().orElse(null);
            if (block == null) {
                return;
            }

            if (event.getPlayer().isSneaking()) {
                onShiftRightClick(block.getLocation(), event.getPlayer());
            } else {
                onRightClick(block.getLocation(), event.getPlayer());
            }

            event.cancel();
        };
    }
    protected void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {}

    private static void setId(@NotNull final DisplayGroup displayGroup, final Location location) {
        BlockStorageAPI.set(location, KEY_UUID, displayGroup.getParentUUID());
    }

    @OverridingMethodsMustInvokeSuper
    protected void burnout(final Location location) {
        getDisplayGroup(location).ifPresent(displayGroup -> {
            displayGroup.getDisplays().values().forEach(Entity::remove);
            displayGroup.remove();
        });

        // TODO make this naturally break, not the forced shit we have going on here
        BlockStorageAPI.removeData(location);
        location.getBlock().setBlockData(Material.AIR.createBlockData());
        location.getWorld().playSound(location.toCenterLocation(), Sound.ENTITY_GENERIC_EXPLODE, BURNOUT_EXPLODE_VOLUME, BURNOUT_EXPLODE_PITCH);
        new ParticleBuilder(Particle.FLASH).location(location.toCenterLocation()).count(3).spawn();
    }


    @NotNull
    protected Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
    protected static @NotNull Vector rotateVectorByEyeDirection(@NotNull final Player player, @NotNull final Vector vector) {
        final double rotationAngle = TransformationUtils.yawToCardinalDirection(player.getEyeLocation().getYaw());
        return vector.clone().rotateAroundY(rotationAngle);
    }
    protected static @NotNull Location formatPointLocation(final Player player, @NotNull final Location location, final Vector relativeLocation) {
        final Vector newRelativeLocation = rotateVectorByEyeDirection(player, relativeLocation);
        newRelativeLocation.add(CENTER_VECTOR);
        return location.clone().add(newRelativeLocation);
    }

    protected static Optional<DisplayGroupId> getDisplayGroupId(final Location location) {
        return BlockStorageAPI.getDisplayGroupId(location, KEY_UUID);
    }
    public static Optional<DisplayGroup> getDisplayGroup(final Location location) {
        return getDisplayGroupId(location).map(displayGroupId -> DisplayGroup.fromUUID(displayGroupId.getUUID()));
    }
    public static Optional<Display> getDisplay(final Location location, final String name) {
        return getDisplayGroup(location).map(displayGroup -> displayGroup.getDisplays().get(name));
    }
    public static Optional<BlockDisplay> getBlockDisplay(final Location location, final String name) {
        final Optional<Display> display = getDisplay(location, name);
        return display.isPresent() && display.get() instanceof final BlockDisplay blockDisplay
                ? Optional.of(blockDisplay)
                : Optional.empty();
    }
    public static Optional<ItemDisplay> getItemDisplay(final Location location, final String name) {
        final Optional<Display> display = getDisplay(location, name);
        return display.isPresent() && display.get() instanceof final ItemDisplay itemDisplay
                ? Optional.of(itemDisplay)
                : Optional.empty();
    }
    public static void removeDisplay(final @NotNull DisplayGroup displayGroup, final String name) {
        final Display display = displayGroup.removeDisplay(name);
        if (display != null) {
            display.remove();
        }
    }
}
