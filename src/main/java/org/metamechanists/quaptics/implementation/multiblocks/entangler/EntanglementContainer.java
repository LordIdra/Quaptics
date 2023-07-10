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
import org.joml.Vector3f;
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
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.metamechanists.quaptics.implementation.multiblocks.entangler.magnet.EntanglementMagnetBottom.ENTANGLEMENT_MAGNET_BOTTOM;
import static org.metamechanists.quaptics.implementation.multiblocks.entangler.magnet.EntanglementMagnetTop.ENTANGLEMENT_MAGNET_TOP;


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

    private static final Vector3f PILLAR_SCALE = new Vector3f(0.1F, 3.2F, 0.1F);
    private static final Vector3f PILLAR_1_OFFSET = new Vector3f(0.7F, 0.0F, 0.0F);
    private static final Vector3f PILLAR_2_OFFSET = new Vector3f(-0.7F, 0.0F, 0.0F);
    private static final Vector3f PILLAR_3_OFFSET = new Vector3f(0.0F, 0.0F, 0.7F);
    private static final Vector3f PILLAR_4_OFFSET = new Vector3f(0.0F, 0.0F, -0.7F);

    private static final Vector3f FRAME_1_SCALE = new Vector3f(0.20F, 0.40F, 1.20F);
    private static final Vector3f FRAME_2_SCALE = new Vector3f(1.2F, 0.40F, 0.20F);
    private static final Vector3f FRAME_3_SCALE = new Vector3f(1.2F, 0.40F, 0.20F);
    private static final Vector3f FRAME_4_SCALE = new Vector3f(0.20F, 0.40F, 1.20F);
    private static final Vector3f FRAME_1_OFFSET = new Vector3f(0.35F, 0.0F, 0.35F);
    private static final Vector3f FRAME_2_OFFSET = new Vector3f(0.35F, 0.0F, -0.35F);
    private static final Vector3f FRAME_3_OFFSET = new Vector3f(-0.35F, 0.0F, 0.35F);
    private static final Vector3f FRAME_4_OFFSET = new Vector3f(-0.35F, 0.0F, -0.35F);
    private static final Vector3f FRAME_ROTATION = new Vector3f(0.0F, (float) (-Math.PI / 4), 0.0F);

    private static final Vector MAGNET_1_LOCATION = new Vector(0, 4, 0);
    private static final Vector MAGNET_2_LOCATION = new Vector(0, -4, 0);
    private static final List<Vector> MAGNET_LOCATIONS = List.of(MAGNET_1_LOCATION, MAGNET_2_LOCATION);

    private static final int MAGNET_PARTICLE_COUNT = 2;
    private final double magnetParticleAnimationLengthSeconds = settings.getTimePerItem();
    private final double centerParticleAnimationLengthSeconds = settings.getTimePerItem() / 8.0;
    private static final double MAGNET_PARTICLE_SPEED = 0.02;
    private static final double CONTAINER_PARTICLE_RADIUS = 0.8;
    private static final int CONTAINER_PARTICLE_COUNT = 3;
    private static final double COMPLETED_PARTICLE_SPEED = 0.1;
    private static final int COMPLETED_PARTICLE_COUNT = 200;

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
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("pillar1", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.GRAY_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PILLAR_SCALE)
                        .translate(PILLAR_1_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("pillar2", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.GRAY_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PILLAR_SCALE)
                        .translate(PILLAR_2_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("pillar3", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.GRAY_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PILLAR_SCALE)
                        .translate(PILLAR_3_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("pillar4", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.GRAY_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PILLAR_SCALE)
                        .translate(PILLAR_4_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("frame1", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.BLUE_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(FRAME_1_SCALE)
                        .rotate(FRAME_ROTATION)
                        .translate(FRAME_1_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("frame2", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.BLUE_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(FRAME_2_SCALE)
                        .rotate(FRAME_ROTATION)
                        .translate(FRAME_2_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("frame3", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.BLUE_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(FRAME_3_SCALE)
                        .rotate(FRAME_ROTATION)
                        .translate(FRAME_3_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("frame4", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.BLUE_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(FRAME_4_SCALE)
                        .rotate(FRAME_ROTATION)
                        .translate(FRAME_4_OFFSET)
                        .buildForBlockDisplay())
                .build());
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
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        if (multiblockInteract(location.getBlock(), player)) {
            return;
        }
        itemHolderInteract(location, player);
    }
    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!BlockStorageAPI.getBoolean(location, Keys.BS_CRAFT_IN_PROGRESS)) {
            return;
        }

        if (!allMagnetsPowered(location)) {
            cancelCraft(location);
            return;
        }

        double secondsSinceCraftStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED);
        secondsSinceCraftStarted += 1.0 / QuapticTicker.QUAPTIC_TICKS_PER_SECOND;
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
                MAGNET_1_LOCATION, ENTANGLEMENT_MAGNET_TOP,
                new Vector(0, 3, 0), new ItemStack(Material.AIR),
                new Vector(0, 2, 0), new ItemStack(Material.AIR),
                new Vector(0, 1, 0), new ItemStack(Material.AIR),
                new Vector(0, -1, 0), new ItemStack(Material.AIR),
                new Vector(0, -2, 0), new ItemStack(Material.AIR),
                new Vector(0, -3, 0), new ItemStack(Material.AIR),
                MAGNET_2_LOCATION, ENTANGLEMENT_MAGNET_BOTTOM
        );
    }
    @Override
    public void tickAnimation(@NotNull final Location centerLocation, final double timeSeconds) {
        MAGNET_LOCATIONS.forEach(magnetLocation -> animatePillar(centerLocation, centerLocation.clone().add(magnetLocation), timeSeconds));
        animateCenter(centerLocation, timeSeconds);
    }

    private static boolean isMagnetPowered(@NotNull final Location pillarLocation) {
        return BlockStorageAPI.getBoolean(pillarLocation, Keys.BS_POWERED);
    }
    private static boolean allMagnetsPowered(@NotNull final Location location) {
        return MAGNET_LOCATIONS.stream().allMatch(vector -> isMagnetPowered(location.clone().add(vector)));
    }

    private void animatePillar(@NotNull final Location center, @NotNull final Location pillarLocation, final double timeSinceCraftStarted) {
        Particles.animatedLine(Particle.SCULK_CHARGE_POP,
                pillarLocation.clone().toCenterLocation(),
                center.clone().toCenterLocation(),
                MAGNET_PARTICLE_COUNT,
                (timeSinceCraftStarted % magnetParticleAnimationLengthSeconds) / (magnetParticleAnimationLengthSeconds+0.001),
                MAGNET_PARTICLE_SPEED);
    }
    private void animateCenter(@NotNull final Location center, final double timeSinceCraftStarted) {
        Particles.animatedHorizontalCircle(Particle.ELECTRIC_SPARK,
                center.clone().toCenterLocation(),
                CONTAINER_PARTICLE_RADIUS,
                CONTAINER_PARTICLE_COUNT,
                (timeSinceCraftStarted % centerParticleAnimationLengthSeconds) / centerParticleAnimationLengthSeconds,
                0);
        Particles.animatedHorizontalCircle(Particle.ELECTRIC_SPARK,
                center.clone().toCenterLocation(),
                CONTAINER_PARTICLE_RADIUS,
                CONTAINER_PARTICLE_COUNT,
                1 - ((timeSinceCraftStarted % centerParticleAnimationLengthSeconds) / centerParticleAnimationLengthSeconds),
                0);
    }
    private static void animateCenterCompleted(@NotNull final Location center) {
        new ParticleBuilder(Particle.FIREWORKS_SPARK)
                .location(center.toCenterLocation())
                .extra(COMPLETED_PARTICLE_SPEED)
                .count(COMPLETED_PARTICLE_COUNT)
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
