package org.metamechanists.quaptics.implementation.multiblocks.infuser;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import dev.sefiraat.sefilib.misc.ParticleUtils;
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
import org.joml.Vector3f;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.blocks.base.QuapticBlock;
import org.metamechanists.quaptics.implementation.multiblocks.ComplexMultiblock;
import org.metamechanists.quaptics.items.groups.Primitive;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.builders.ItemDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import java.util.Map;
import java.util.Optional;

public class InfusionContainer extends QuapticBlock implements ItemHolderBlock, ComplexMultiblock {
    private static final Vector3f BASE_SCALE = new Vector3f(0.9F, 0.6F, 0.9F);
    private static final Vector3f BASE_OFFSET = new Vector3f(0.0F, -0.3F, 0.0F);
    private static final Vector3f PILLAR_SCALE = new Vector3f(0.2F, 0.8F, 0.2F);

    private static final Vector3f PILLAR_1_OFFSET = new Vector3f(-0.4F, -0.1F, -0.4F);
    private static final Vector3f PILLAR_2_OFFSET = new Vector3f(-0.4F, -0.1F, 0.4F);
    private static final Vector3f PILLAR_3_OFFSET = new Vector3f(0.4F, -0.1F, -0.4F);
    private static final Vector3f PILLAR_4_OFFSET = new Vector3f(0.4F, -0.1F, 0.4F);

    private static final Vector PILLAR_1_LOCATION = new Vector(2, 0, 0);
    private static final Vector PILLAR_2_LOCATION = new Vector(-2, 0, 0);
    private static final Vector PILLAR_3_LOCATION = new Vector(0, 0, 2);
    private static final Vector PILLAR_4_LOCATION = new Vector(0, 0, -2);

    private static final Vector3f ITEM_DISPLAY_SIZE = new Vector3f(0.5F);
    private static final Vector3f ITEM_DISPLAY_OFFSET = new Vector3f(0, 0.3F, 0);

    private static final double PILLAR_PARTICLE_SPACING = 0.2;
    private static final double CONTAINER_PARTICLE_RADIUS = 1.2;


    public InfusionContainer(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("base", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.GRAY_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(BASE_SCALE, BASE_OFFSET))
                .build());
        displayGroup.addDisplay("pillar1", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(PILLAR_SCALE, PILLAR_1_OFFSET))
                .build());
        displayGroup.addDisplay("pillar2", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(PILLAR_SCALE, PILLAR_2_OFFSET))
                .build());
        displayGroup.addDisplay("pillar3", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(PILLAR_SCALE, PILLAR_3_OFFSET))
                .build());
        displayGroup.addDisplay("pillar4", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.WHITE_CONCRETE.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(PILLAR_SCALE, PILLAR_4_OFFSET))
                .build());
        displayGroup.addDisplay("item", new ItemDisplayBuilder(location.toCenterLocation())
                .setTransformation(Transformations.unadjustedScaleTranslate(ITEM_DISPLAY_SIZE, ITEM_DISPLAY_OFFSET))
                .build());
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
        if (multiblockInteract(location.getBlock(), player)) {
            return;
        }
        itemHolderInteract(location, player);
    }

    @Override
    public boolean onInsert(@NotNull final ItemStack stack, @NotNull final Player player) {
        return true;
    }

    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final ItemStack stack) {
        return Optional.of(stack);
    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        return Map.of(
                PILLAR_1_LOCATION, Primitive.INFUSION_PILLAR,
                PILLAR_2_LOCATION, Primitive.INFUSION_PILLAR,
                PILLAR_3_LOCATION, Primitive.INFUSION_PILLAR,
                PILLAR_4_LOCATION, Primitive.INFUSION_PILLAR
        );
    }

    @Override
    public void tickAnimation(@NotNull final Location location) {
        animatePillar(location.clone().add(PILLAR_1_LOCATION), location);
        animatePillar(location.clone().add(PILLAR_2_LOCATION), location);
        animatePillar(location.clone().add(PILLAR_3_LOCATION), location);
        animatePillar(location.clone().add(PILLAR_4_LOCATION), location);
        animateCenter(location);
    }

    private static void animatePillar(@NotNull final Location center, @NotNull final Location pillarLocation) {
        ParticleUtils.drawLine(Particle.END_ROD, pillarLocation, center, PILLAR_PARTICLE_SPACING);
    }

    private static void animateCenter(@NotNull final Location center) {
        org.metamechanists.metalib.utils.ParticleUtils.sphere(center, Particle.ELECTRIC_SPARK, CONTAINER_PARTICLE_RADIUS, false);
    }
}