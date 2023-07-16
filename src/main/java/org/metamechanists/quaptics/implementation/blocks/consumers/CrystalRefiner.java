package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.misc.ParticleUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.metalib.utils.RandomUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.ItemProcessor;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.items.groups.Primitive;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelDiamond;
import org.metamechanists.quaptics.utils.models.components.ModelItem;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class CrystalRefiner extends ConnectedBlock implements ItemHolderBlock, PowerAnimatedBlock, ItemProcessor {
    public static final Settings CRYSTAL_REFINER_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack CRYSTAL_REFINER = new SlimefunItemStack(
            "QP_CRYSTAL_REFINER",
            Material.QUARTZ_BLOCK,
            "&bCrystal Refiner &3I",
            Lore.create(CRYSTAL_REFINER_SETTINGS,
                    "&7● Refines Phase Crystals",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public CrystalRefiner(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.60F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("wall1", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .location(0.3F, -0.1F, 0.3F)
                        .size(0.3F, 0.8F, 0.8F)
                        .rotation(Math.PI / 4))
                .add("wall2", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .location(-0.3F, -0.1F, -0.3F)
                        .size(0.3F, 0.8F, 0.8F)
                        .rotation(Math.PI / 4))
                .add("wall3", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .location(0.3F, -0.1F, -0.3F)
                        .size(0.8F, 0.8F, 0.3F)
                        .rotation(Math.PI / 4))
                .add("wall4", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .location(-0.3F, -0.1F, 0.3F)
                        .size(0.8F, 0.8F, 0.3F)
                        .rotation(Math.PI / 4))

                .add("water", new ModelCuboid()
                        .material(Material.BLUE_CONCRETE)
                        .location(0, -0.3F, 0)
                        .size(1.0F, 0.4F, 1.0F)
                        .rotation(Math.PI / 4))
                .add("concrete", new ModelDiamond()
                        .material(settings.getTier().glassMaterial)
                        .location(0, -0.2F, 0)
                        .size(0.3F))

                .add("item", new ModelItem()
                        .facing(player.getFacing())
                        .size(0.5F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    protected boolean isTicker() {
        return true;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakItemHolderBlock(location, "item");
    }
    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        itemHolderInteract(location, "item", player);
        return true;
    }
    @SuppressWarnings("unused")
    @Override
    public void onTick2(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!isProcessing(location)) {
            return;
        }

        if (!BlockStorageAPI.getBoolean(location, Keys.BS_POWERED)) {
            cancelProcessing(location);
        }

        final double secondsSinceCraftStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED);

        tickProcessing(location, QuapticTicker.INTERVAL_TICKS_2);
        tickAnimation(location);

        if (secondsSinceCraftStarted >= settings.getTimePerItem()) {
            completeProcessing(location);
        }
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        doBurnoutCheck(group, "input");
        final Optional<Link> inputLink = getLink(location, "input");
        BlockStorageAPI.set(location, Keys.BS_POWERED, settings.isOperational(inputLink));
        onPoweredAnimation(location, settings.isOperational(inputLink));
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player) {
        if (!isValidRecipe(stack)) {
            Language.sendLanguageMessage(player, "crystal-refiner.invalid-item");
            return false;
        }

        startProcessing(location);
        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack) {
        cancelProcessing(location);
        return Optional.of(stack);
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "concrete", powered);
    }

    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        return Map.of(
                new ItemStack(Material.QUARTZ), Primitive.PHASE_CRYSTAL_1,
                Primitive.PHASE_CRYSTAL_1, Primitive.PHASE_CRYSTAL_5,
                Primitive.PHASE_CRYSTAL_5, Primitive.PHASE_CRYSTAL_15,
                Primitive.PHASE_CRYSTAL_15, Primitive.PHASE_CRYSTAL_45,
                Primitive.PHASE_CRYSTAL_45, Primitive.PHASE_CRYSTAL_90,
                Primitive.PHASE_CRYSTAL_90, Primitive.PHASE_CRYSTAL_180
        );
    }

    private static void tickAnimation(@NotNull final Location location) {
        final Vector direction = new Vector(
                RandomUtils.randomDouble(0, 1),
                RandomUtils.randomDouble(0, 1),
                RandomUtils.randomDouble(0, 1))
                .normalize();
        final Location origin = location.clone().add(direction);
        ParticleUtils.drawLine(Particle.END_ROD, origin, location, 0.15F);
    }
}
