package org.metamechanists.quaptics.implementation.multiblocks.reactor;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.ComplexMultiblock;
import org.metamechanists.quaptics.implementation.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.implementation.ReactorInfoPanel;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Particles;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelDiamond;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Map;

import static org.metamechanists.quaptics.implementation.multiblocks.reactor.ReactorRing.REACTOR_RING;


public class ReactorController extends ConnectedBlock implements ComplexMultiblock, InfoPanelBlock {
    public static final Settings REACTOR_CONTROLLER_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(30)
            .emissionPower(60)
            .timeToMaxEfficiency(20)
            .powerMultiplier(1.5)
            .build();
    public static final SlimefunItemStack REACTOR_CONTROLLER = new SlimefunItemStack(
            "QP_REACTOR_CONTROLLER",
            Material.GRAY_CONCRETE,
            "&6Reactor Controller",
            Lore.create(REACTOR_CONTROLLER_SETTINGS,
                    "&7● Multiblock structure: use the Multiblock Wand to build the structure",
                    "&7● Generates more power than you put in",
                    "&7● Immediately shuts down if power is lost",
                    "&7● Takes some time to reach maximum power output"));

    private static final Vector RING_1_LOCATION = new Vector(3, 0, 0);
    private static final Vector RING_2_LOCATION = new Vector(2, 0, 2);
    private static final Vector RING_3_LOCATION = new Vector(0, 0, 3);
    private static final Vector RING_4_LOCATION = new Vector(-2, 0, 2);
    private static final Vector RING_5_LOCATION = new Vector(-3, 0, 0);
    private static final Vector RING_6_LOCATION = new Vector(-2, 0, -2);
    private static final Vector RING_7_LOCATION = new Vector(0, 0, -3);
    private static final Vector RING_8_LOCATION = new Vector(2, 0, -2);
    private static final List<Vector> RING_LOCATIONS = List.of(
            RING_1_LOCATION, RING_2_LOCATION, RING_3_LOCATION, RING_4_LOCATION,
            RING_5_LOCATION, RING_6_LOCATION, RING_7_LOCATION, RING_8_LOCATION);
    private static final double LOWEST_ANIMATION_LENGTH = 0.3F;

    private final Vector outputPoint1Location = new Vector(getConnectionRadius(), 0, 0);
    private final Vector outputPoint2Location = new Vector(-getConnectionRadius(), 0, 0);
    private final Vector outputPoint3Location = new Vector(0, 0, getConnectionRadius());
    private final Vector outputPoint4Location = new Vector(0, 0, -getConnectionRadius());

    public ReactorController(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.7F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelDiamond()
                        .material(Material.LIGHT_GRAY_CONCRETE)
                        .size(1.0F))
                .add("panel1", new ModelCuboid()
                        .material(Material.CYAN_CONCRETE)
                        .size(0.8F, 0.6F, 0.6F)
                        .rotation(ModelDiamond.ROTATION))
                .add("panel2", new ModelCuboid()
                        .material(Material.BLUE_CONCRETE)
                        .size(0.6F, 0.8F, 0.6F)
                        .rotation(ModelDiamond.ROTATION))
                .add("panel3", new ModelCuboid()
                        .material(Material.LIGHT_BLUE_CONCRETE)
                        .size(0.6F, 0.6F, 0.8F)
                        .rotation(ModelDiamond.ROTATION))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output 1", location.clone().toCenterLocation().add(outputPoint1Location)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output 2", location.clone().toCenterLocation().add(outputPoint2Location)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output 3", location.clone().toCenterLocation().add(outputPoint3Location)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output 4", location.clone().toCenterLocation().add(outputPoint4Location)));
    }
    @Override
    protected void initBlockStorage(final @NotNull Location location) {
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_REACTOR_STARTED, 0.0);
        BlockStorageAPI.set(location, Keys.BS_OUTPUT_POWER, 0.0);
    }

    @Override
    public BlockInfoPanel createPanel(final Location location, final @NotNull ConnectionGroup group) {
        return new ReactorInfoPanel(location, group.getId());
    }
    @Override
    public BlockInfoPanel getPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new ReactorInfoPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        onPlaceInfoPanelBlock(event);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakInfoPanelBlock(location);
    }
    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        multiblockInteract(location.getBlock(), player);
    }
    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final boolean isMultiblockValid = areAllRingsInPlace(location);
        setPanelHidden(group, !isMultiblockValid);
        if (!isMultiblockValid) {
            BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_REACTOR_STARTED, 0.0);
            BlockStorageAPI.set(location, Keys.BS_OUTPUT_POWER, 0.0);
            return;
        }

        final double inputPower = getTotalInputPower(location);
        updatePanel(group);
        if (inputPower < settings.getMinPower()) {
            BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_REACTOR_STARTED, 0.0);
            return;
        }

        double secondsSinceReactorStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_REACTOR_STARTED);
        secondsSinceReactorStarted += 1.0 / QuapticTicker.QUAPTIC_TICKS_PER_SECOND;
        BlockStorageAPI.set(location, Keys.BS_SECONDS_SINCE_REACTOR_STARTED, secondsSinceReactorStarted);

        final double powerProportion = Math.min(secondsSinceReactorStarted, settings.getTimeToMaxEfficiency()) / settings.getTimeToMaxEfficiency();
        final double outputPower = (powerProportion * inputPower * settings.getPowerMultiplier());
        BlockStorageAPI.set(location, Keys.BS_OUTPUT_POWER, outputPower);

        tickAnimation(location, secondsSinceReactorStarted);

        final List<Link> outgoingLinks = getOutgoingLinks(location);
        if (outgoingLinks.isEmpty()) {
            return;
        }

        outgoingLinks.forEach(link -> link.setPower(outputPower / outgoingLinks.size()));
    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        return Map.of(
                RING_1_LOCATION, REACTOR_RING,
                RING_2_LOCATION, REACTOR_RING,
                RING_3_LOCATION, REACTOR_RING,
                RING_4_LOCATION, REACTOR_RING,
                RING_5_LOCATION, REACTOR_RING,
                RING_6_LOCATION, REACTOR_RING,
                RING_7_LOCATION, REACTOR_RING,
                RING_8_LOCATION, REACTOR_RING
        );
    }
    @Override
    public void tickAnimation(@NotNull final Location centerLocation, final double timeSeconds) {
        final double animationLength = (BlockStorageAPI.getDouble(centerLocation, Keys.BS_OUTPUT_POWER) / getMaxOutputPower()) * LOWEST_ANIMATION_LENGTH;
        Particles.animatedHorizontalCircle(Particle.ELECTRIC_SPARK,
                centerLocation.clone().toCenterLocation(),
                3,
                5,
                (timeSeconds % animationLength) / animationLength,
                0);
    }

    public double getMaxOutputPower() {
        return RING_LOCATIONS.size() * ReactorRing.REACTOR_RING_SETTINGS.getTier().maxPower * 2 * settings.getPowerMultiplier();
    }
    private static double getRingInputPower(@NotNull final Location ringLocation) {
        return BlockStorageAPI.getDouble(ringLocation, Keys.BS_INPUT_POWER);
    }
    public static double getTotalInputPower(@NotNull final Location location) {
        return RING_LOCATIONS.stream().mapToDouble(vector -> getRingInputPower(location.clone().add(vector))).sum();
    }
    private static boolean isRing(@NotNull final Location ringLocation) {
        return BlockStorageAPI.check(ringLocation) instanceof  ReactorRing;
    }
    private static boolean areAllRingsInPlace(@NotNull final Location center) {
        return RING_LOCATIONS.stream().allMatch(location -> isRing(center.clone().add(location)));
    }
}