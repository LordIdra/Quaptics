package org.metamechanists.quaptics.implementation.multiblocks.entangler;

import com.destroystokyo.paper.ParticleBuilder;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.attachments.ComplexMultiblock;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.ItemProcessor;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.Particles;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.displaymodellib.models.components.ModelLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.metamechanists.quaptics.implementation.multiblocks.entangler.EntanglementMagnet.ENTANGLEMENT_MAGNET;
import static org.metamechanists.quaptics.items.groups.CraftingComponents.*;


public class EntanglementContainer extends ConnectedBlock implements ItemHolderBlock, ComplexMultiblock, ItemProcessor {
    public static final Settings ENTANGLEMENT_CONTAINER_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .timePerItem(5)
            .build();
    public static final SlimefunItemStack ENTANGLEMENT_CONTAINER = new SlimefunItemStack(
            "QP_ENTANGLEMENT_CONTAINER",
            Material.CYAN_CONCRETE,
            "&6Entanglement Container",
            Lore.create(ENTANGLEMENT_CONTAINER_SETTINGS,
                    Lore.multiblock(),
                    "&7● Entangles items",
                    "&7● &eRight Click &7with an item to start the entanglement process"));

    private static final Map<Vector, ItemStack> MAGNETS = Map.of(
            new Vector(3, 0, 0), ENTANGLEMENT_MAGNET,
            new Vector(-3, 0, 0), ENTANGLEMENT_MAGNET,
            new Vector(0, 3, 0), ENTANGLEMENT_MAGNET,
            new Vector(0, -3, 0), ENTANGLEMENT_MAGNET,
            new Vector(0, 0, 3), ENTANGLEMENT_MAGNET,
            new Vector(0, 0, -3), ENTANGLEMENT_MAGNET);

    private final double magnetParticleAnimationLengthSeconds = settings.getTimePerItem();

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
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, -0.4F, -0.4F)
                        .to(0.4F, -0.4F, -0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1b", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, 0.4F, -0.4F)
                        .to(0.4F, 0.4F, -0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1c", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, -0.4F, 0.4F)
                        .to(0.4F, -0.4F, 0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))
                .add("frame1d", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, 0.4F, 0.4F)
                        .to(0.4F, 0.4F, 0.4F)
                        .thickness(0.1F)
                        .extraLength(0.1F))

                .add("frame2a", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, -0.4F, -0.4F)
                        .to(-0.4F, 0.4F, -0.4F)
                        .thickness(0.1F))
                .add("frame2b", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0.4F, -0.4F, -0.4F)
                        .to(0.4F, 0.4F, -0.4F)
                        .thickness(0.1F))
                .add("frame2c", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, -0.4F, 0.4F)
                        .to(-0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame2d", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0.4F, -0.4F, 0.4F)
                        .to(0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))

                .add("frame3a", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, -0.4F, -0.4F)
                        .to(-0.4F, -0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame3b", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(0.4F, -0.4F, -0.4F)
                        .to(0.4F, -0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame3c", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
                        .from(-0.4F, 0.4F, -0.4F)
                        .to(-0.4F, 0.4F, 0.4F)
                        .thickness(0.1F))
                .add("frame3d", new ModelLine()
                        .material(Material.CYAN_CONCRETE)
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
    protected boolean isTicker() {
        return true;
    }

    @Override
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakItemHolderBlock(location, "item");
    }
    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        if (multiblockInteract(location.getBlock(), player)) {
            return true;
        }
        itemHolderInteract(location, "item", player);
        return true;
    }
    @SuppressWarnings("unused")
    @Override
    public void onTick22(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_MULTIBLOCK_INTACT, isStructureValid(location.getBlock()));
    }
    @SuppressWarnings("unused")
    @Override
    public void onTick2(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!isProcessing(location)) {
            return;
        }

        if (!BlockStorageAPI.getBoolean(location, Keys.BS_MULTIBLOCK_INTACT) || !allMagnetsPowered(location)) {
            cancelProcessing(location);
        }

        final double secondsSinceCraftStarted = BlockStorageAPI.getDouble(location, Keys.BS_SECONDS_SINCE_CRAFT_STARTED);

        tickProcessing(location, QuapticTicker.INTERVAL_TICKS_2);
        tickAnimation(location, secondsSinceCraftStarted);

        if (secondsSinceCraftStarted >= settings.getTimePerItem()) {
            completeProcessing(location);
            animateCenterCompleted(location);
        }
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player) {
        if (!isValidRecipe(stack)) {
            Language.sendLanguageMessage(player, "entangler.cannot-be-entangled");
            return false;
        }

        if (!allMagnetsPowered(location)) {
            Language.sendLanguageMessage(player, "entangler.magnets-not-powered");
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
    public Map<ItemStack, ItemStack> getRecipes() {
        return Map.of(
                INFUSED_MODULE_CARD, ENTANGLED_MODULE_CARD,
                INFUSED_FREQUENCY_CRYSTAL, ENTANGLED_FREQUENCY_CRYSTAL);
    }
    @Override
    public Map<Vector, ItemStack> getStructure() {
        return MAGNETS;
    }
    @Override
    public void tickAnimation(@NotNull final Location centerLocation, final double timeSeconds) {
        getStructure().keySet().forEach(magnetLocation -> animateMagnet(centerLocation, centerLocation.clone().add(magnetLocation), timeSeconds));
    }

    private static boolean isMagnetPowered(@NotNull final Location pillarLocation) {
        return BlockStorageAPI.getBoolean(pillarLocation, Keys.BS_POWERED);
    }
    private boolean allMagnetsPowered(@NotNull final Location location) {
        return getStructure().keySet().stream().allMatch(vector -> isMagnetPowered(location.clone().add(vector)));
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
}
