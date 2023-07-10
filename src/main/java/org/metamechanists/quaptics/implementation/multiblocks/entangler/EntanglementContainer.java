package org.metamechanists.quaptics.implementation.multiblocks.entangler;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.multiblocks.ComplexMultiblock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.metamechanists.quaptics.implementation.multiblocks.entangler.magnet.EntanglementMagnetBottom.ENTANGLEMENT_MAGNET_BOTTOM;
import static org.metamechanists.quaptics.implementation.multiblocks.entangler.magnet.EntanglementMagnetTop.ENTANGLEMENT_MAGNET_TOP;


public class EntanglementContainer extends ConnectedBlock implements ComplexMultiblock {
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

    private static final Vector3f PILLAR_SCALE = new Vector3f(0.1F, 1.6F, 0.1F);
    private static final Vector3f PILLAR_1_OFFSET = new Vector3f(0.4F, 0.0F, 0.0F);
    private static final Vector3f PILLAR_2_OFFSET = new Vector3f(-0.4F, 0.0F, 0.0F);
    private static final Vector3f PILLAR_3_OFFSET = new Vector3f(0.0F, 0.0F, 0.4F);
    private static final Vector3f PILLAR_4_OFFSET = new Vector3f(0.0F, 0.0F, -0.4F);

    private static final Vector3f FRAME_1_SCALE = new Vector3f(0.2F, 0.4F, 0.7F);
    private static final Vector3f FRAME_2_SCALE = new Vector3f(0.7F, 0.4F, 0.2F);
    private static final Vector3f FRAME_3_SCALE = new Vector3f(0.7F, 0.4F, 0.2F);
    private static final Vector3f FRAME_4_SCALE = new Vector3f(0.2F, 0.4F, 0.7F);
    private static final Vector3f FRAME_1_OFFSET = new Vector3f(0.20F, 0.0F, 0.20F);
    private static final Vector3f FRAME_2_OFFSET = new Vector3f(0.20F, 0.0F, -0.20F);
    private static final Vector3f FRAME_3_OFFSET = new Vector3f(-0.20F, 0.0F, 0.20F);
    private static final Vector3f FRAME_4_OFFSET = new Vector3f(-0.20F, 0.0F, -0.20F);
    private static final Vector3f FRAME_ROTATION = new Vector3f(0.0F, (float) (-Math.PI / 4), 0.0F);

    private static final Vector MAGNET_1_LOCATION = new Vector(0, 4, 0);
    private static final Vector MAGNET_2_LOCATION = new Vector(0, -4, 0);
    private static final List<Vector> MAGNET_LOCATIONS = List.of(MAGNET_1_LOCATION, MAGNET_2_LOCATION);

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
        final Optional<InfoPanelId> panelId = InfoPanelBlock.getPanelId(location);
        final Optional<InfoPanelContainer> panel = panelId.isPresent() ? panelId.get().get() : Optional.empty();
        panel.ifPresent(InfoPanelContainer::remove);
        ItemHolderBlock.getStack(location).ifPresent(stack -> location.getWorld().dropItem(location, stack));
    }
    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        multiblockInteract(location.getBlock(), player);
    }
    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!BlockStorageAPI.getBoolean(location, Keys.BS_CRAFT_IN_PROGRESS)) {
            return;
        }

        if (!allMagnetsPowered(location)) {
            // TODO cancel craft
            return;
        }

        double secondsSinceCraftStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED);
        secondsSinceCraftStarted += 1.0 / QuapticTicker.QUAPTIC_TICKS_PER_SECOND;
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED, secondsSinceCraftStarted);

        tickAnimation(location, secondsSinceCraftStarted);

        if (secondsSinceCraftStarted >= settings.getTimePerItem()) {
            // TODO complete craft
        }
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

    }

    private static boolean isMagnetPowered(@NotNull final Location pillarLocation) {
        return BlockStorageAPI.getBoolean(pillarLocation, Keys.BS_POWERED);
    }
    private static boolean allMagnetsPowered(@NotNull final Location location) {
        return MAGNET_LOCATIONS.stream().allMatch(vector -> isMagnetPowered(location.clone().add(vector)));
    }
}
