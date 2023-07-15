package org.metamechanists.quaptics.implementation.multiblocks.entangler;

import com.destroystokyo.paper.ParticleBuilder;
import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.attachments.ComplexMultiblock;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.items.groups.Primitive;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.Particles;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelItem;
import org.metamechanists.quaptics.utils.models.components.ModelLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.metamechanists.quaptics.implementation.multiblocks.entangler.EntanglementMagnet.ENTANGLEMENT_MAGNET;


public class EntanglementContainer extends ConnectedBlock implements ItemHolderBlock, ComplexMultiblock {
    public static final Settings ENTANGLEMENT_CONTAINER_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .timePerItem(10)
            .build();
    public static final SlimefunItemStack ENTANGLEMENT_CONTAINER = new SlimefunItemStack(
            "QP_ENTANGLEMENT_CONTAINER",
            Material.BLUE_CONCRETE,
            "&6Entanglement Container",
            Lore.create(ENTANGLEMENT_CONTAINER_SETTINGS,
                    "&7● Multiblock structure: use the Multiblock Wand to build the structure",
                    "&7● Entangles items",
                    "&7● &eRight Click &7with an item to start the entanglement process"));

    private static final Vector MAGNET_1_LOCATION = new Vector(3, 0, 0);
    private static final Vector MAGNET_2_LOCATION = new Vector(-3, 0, 0);
    private static final Vector MAGNET_3_LOCATION = new Vector(0, 3, 0);
    private static final Vector MAGNET_4_LOCATION = new Vector(0, -3, 0);
    private static final Vector MAGNET_5_LOCATION = new Vector(0, 0, 3);
    private static final Vector MAGNET_6_LOCATION = new Vector(0, 0, -3);
    private static final List<Vector> MAGNET_LOCATIONS = List.of(
            MAGNET_1_LOCATION, MAGNET_2_LOCATION, MAGNET_3_LOCATION,
            MAGNET_4_LOCATION, MAGNET_5_LOCATION, MAGNET_6_LOCATION);

    private final double magnetParticleAnimationLengthSeconds = settings.getTimePerItem();

    private static final Map<ItemStack, ItemStack> RECIPES = Map.of(
            new ItemStack(Material.DEAD_BUSH), Primitive.ENTANGLED_CORE
    );

    public EntanglementContainer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.0F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("frame1a", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, -0.4F, -0.4F)
                        .to(0.4F, -0.4F, -0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1b", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, 0.4F, -0.4F)
                        .to(0.4F, 0.4F, -0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1c", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, -0.4F, 0.4F)
                        .to(0.4F, -0.4F, 0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1d", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, 0.4F, 0.4F)
                        .to(0.4F, 0.4F, 0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))

                .add("frame2a", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, -0.4F, -0.4F)
                        .to(-0.4F, 0.4F, -0.4F)
                        .thickness(0.1F))
                .add("frame2b", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(0.4F, -0.4F, -0.4F)
                        .to(0.4F, 0.4F, -0.4F)
                        .thickness(0.1F))
                .add("frame2c", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, -0.4F, 0.4F)
                        .to(-0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame2d", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(0.4F, -0.4F, 0.4F)
                        .to(0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))

                .add("frame3a", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, -0.4F, -0.4F)
                        .to(-0.4F, -0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame3b", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(0.4F, -0.4F, -0.4F)
                        .to(0.4F, -0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame3c", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(-0.4F, 0.4F, -0.4F)
                        .to(-0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame3d", new ModelLine()
                        .material(Material.BLUE_CONCRETE)
                        .from(0.4F, 0.4F, -0.4F)
                        .to(0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))
                .add("item", new ModelItem()
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.5F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return new ArrayList<>();
    }
    @Override
    protected void initBlockStorage(final @NotNull Location location) {
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, false);
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0.0);
    }

    @Override
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakItemHolderBlock(location);
    }
    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        if (multiblockInteract(location.getBlock(), player)) {
            return true;
        }
        itemHolderInteract(location, player);
        return true;
    }
    @SuppressWarnings("unused")
    @Override
    public void onTick21(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_MULTIBLOCK_INTACT, isStructureValid(location.getBlock()));
    }
    @SuppressWarnings("unused")
    @Override
    public void onTick2(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!BlockStorageAPI.getBoolean(location, Keys.BS_CRAFT_IN_PROGRESS)) {
            return;
        }

        if (!BlockStorageAPI.getBoolean(location, Keys.BS_MULTIBLOCK_INTACT) || !allMagnetsPowered(location)) {
            cancelCraft(location);
        }

        double secondsSinceCraftStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED);
        secondsSinceCraftStarted += (double) QuapticTicker.INTERVAL_TICKS_2 / QuapticTicker.TICKS_PER_SECOND;
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, secondsSinceCraftStarted);

        tickAnimation(location, secondsSinceCraftStarted);

        if (secondsSinceCraftStarted >= settings.getTimePerItem()) {
            completeCraft(location);
        }
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final ItemStack stack, @NotNull final Player player) {
        if (RECIPES.keySet().stream().noneMatch(input -> SlimefunUtils.isItemSimilar(input, stack, false))) {
            Language.sendLanguageMessage(player, "entangler.cannot-be-entangled");
            return false;
        }

        if (!allMagnetsPowered(location)) {
            Language.sendLanguageMessage(player, "entangler.magnets-not-powered");
            return false;
        }

        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, true);
        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final ItemStack stack) {
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, false);
        return Optional.of(stack);
    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        return Map.of(
                MAGNET_1_LOCATION, ENTANGLEMENT_MAGNET,
                MAGNET_2_LOCATION, ENTANGLEMENT_MAGNET,
                MAGNET_3_LOCATION, ENTANGLEMENT_MAGNET,
                MAGNET_4_LOCATION, ENTANGLEMENT_MAGNET,
                MAGNET_5_LOCATION, ENTANGLEMENT_MAGNET,
                MAGNET_6_LOCATION, ENTANGLEMENT_MAGNET);
    }
    @Override
    public void tickAnimation(@NotNull final Location centerLocation, final double timeSeconds) {
        MAGNET_LOCATIONS.forEach(magnetLocation -> animateMagnet(centerLocation, centerLocation.clone().add(magnetLocation), timeSeconds));
    }

    private static boolean isMagnetPowered(@NotNull final Location pillarLocation) {
        return BlockStorageAPI.getBoolean(pillarLocation, Keys.BS_POWERED);
    }
    private static boolean allMagnetsPowered(@NotNull final Location location) {
        return MAGNET_LOCATIONS.stream().allMatch(vector -> isMagnetPowered(location.clone().add(vector)));
    }

    private void animateMagnet(@NotNull final Location center, @NotNull final Location pillarLocation, final double timeSinceCraftStarted) {
        Particles.animatedLine(Particle.BUBBLE_POP,
                pillarLocation.clone().toCenterLocation(),
                center.clone().toCenterLocation(),
                8,
                (40*timeSinceCraftStarted % magnetParticleAnimationLengthSeconds) / (magnetParticleAnimationLengthSeconds+0.001),
                0.05);
    }
    private static void animateCenterCompleted(@NotNull final Location center) {
        new ParticleBuilder(Particle.FIREWORKS_SPARK)
                .location(center.toCenterLocation())
                .extra(0.1)
                .count(50)
                .spawn();
    }

    private static void cancelCraft(@NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, false);
    }
    private static void completeCraft(@NotNull final Location location) {
        animateCenterCompleted(location);
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, 0);
        BlockStorageAPI.set(location, Keys.BS_CRAFT_IN_PROGRESS, false);

        final Optional<ItemStack> stack = ItemHolderBlock.getStack(location);
        if (stack.isEmpty()) {
            return;
        }

        ItemHolderBlock.insertItem(location, RECIPES.get(stack.get()));
    }
}
