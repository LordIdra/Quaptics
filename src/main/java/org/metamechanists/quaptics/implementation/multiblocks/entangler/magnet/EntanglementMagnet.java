package org.metamechanists.quaptics.implementation.multiblocks.entangler.magnet;

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
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.transformations.TransformationMatrixBuilder;

import java.util.List;
import java.util.Optional;


public class EntanglementMagnet extends ConnectedBlock implements PowerAnimatedBlock {
    public static final Settings ENTANGLEMENT_MAGNET_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(7)
            .build();
    public static final SlimefunItemStack ENTANGLEMENT_MAGNET = new SlimefunItemStack(
            "QP_ENTANGLEMENT_MAGNET",
            Material.ORANGE_CONCRETE,
            "&6Entanglement Magnet",
            Lore.create(ENTANGLEMENT_MAGNET_SETTINGS,
                    "&7● Multiblock component"));

    private static final Vector3f BASE_SCALE = new Vector3f(0.80F, 0.40F, 0.80F);
    private static final Vector3f BASE_OFFSET = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3d BASE_ROTATION = new Vector3d(0.0F, Math.PI/4, 0.0F);
    private static final Vector3f COIL_1_SCALE = new Vector3f(0.60F, 0.20F, 0.10F);
    private static final Vector3f COIL_2_SCALE = new Vector3f(0.10F, 0.20F, 0.60F);
    private static final Vector3f COIL_3_SCALE = new Vector3f(0.10F, 0.20F, 0.60F);
    private static final Vector3f COIL_4_SCALE = new Vector3f(0.60F, 0.20F, 0.10F);
    private static final Vector3d COIL_ROTATION = new Vector3d(0.0F, Math.PI/4, 0.0F);
    private static final Vector3f COIL_1_OFFSET = new Vector3f(0.30F, 0.0F, 0.30F);
    private static final Vector3f COIL_2_OFFSET = new Vector3f(0.30F, 0.0F, -0.30F);
    private static final Vector3f COIL_3_OFFSET = new Vector3f(-0.30F, 0.0F, 0.30F);
    private static final Vector3f COIL_4_OFFSET = new Vector3f(-0.30F, 0.0F, -0.30F);
    private static final Vector3f BOTTOM_PLATE_SCALE = new Vector3f(0.50F, 0.10F, 0.50F);
    private static final Vector3f BOTTOM_PLATE_OFFSET = new Vector3f(0.0F, 0.20F, 0.0F);
    private static final Vector3d BOTTOM_PLATE_ROTATION = new Vector3d(0.0F, Math.PI/4, 0.0F);
    private static final Vector3f TOP_PLATE_SCALE = new Vector3f(0.50F, 0.10F, 0.50F);
    private static final Vector3f TOP_PLATE_OFFSET = new Vector3f(0.0F, -0.20F, 0.0F);
    private static final Vector3d TOP_PLATE_ROTATION = new Vector3d(0.0F, Math.PI/4, 0.0F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public EntanglementMagnet(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.65F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        final DisplayGroup displayGroup = new DisplayGroup(location);
        displayGroup.addDisplay("base", new BlockDisplayBuilder()
                .blockData(Material.GRAY_CONCRETE.createBlockData())
                .transformation(new TransformationMatrixBuilder()
                        .scale(BASE_SCALE)
                        .rotate(BASE_ROTATION)
                        .translate(BASE_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("bottom_plate", new BlockDisplayBuilder()
                .blockData(Material.LIGHT_GRAY_CONCRETE.createBlockData())
                .transformation(new TransformationMatrixBuilder()
                        .scale(BOTTOM_PLATE_SCALE)
                        .rotate(BOTTOM_PLATE_ROTATION)
                        .translate(BOTTOM_PLATE_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("top_plate", new BlockDisplayBuilder()
                .blockData(Material.LIGHT_GRAY_CONCRETE.createBlockData())
                .transformation(new TransformationMatrixBuilder()
                        .scale(TOP_PLATE_SCALE)
                        .rotate(TOP_PLATE_ROTATION)
                        .translate(TOP_PLATE_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("coil1", new BlockDisplayBuilder()
                .blockData(Material.ORANGE_CONCRETE.createBlockData())
                .brightness(Utils.BRIGHTNESS_OFF)
                .transformation(new TransformationMatrixBuilder()
                        .scale(COIL_1_SCALE)
                        .rotate(COIL_ROTATION)
                        .translate(COIL_1_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("coil2", new BlockDisplayBuilder()
                .blockData(Material.ORANGE_CONCRETE.createBlockData())
                .brightness(Utils.BRIGHTNESS_OFF)
                .transformation(new TransformationMatrixBuilder()
                        .scale(COIL_2_SCALE)
                        .rotate(COIL_ROTATION)
                        .translate(COIL_2_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("coil3", new BlockDisplayBuilder()
                .blockData(Material.ORANGE_CONCRETE.createBlockData())
                .brightness(Utils.BRIGHTNESS_OFF)
                .transformation(new TransformationMatrixBuilder()
                        .scale(COIL_3_SCALE)
                        .rotate(COIL_ROTATION)
                        .translate(COIL_3_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("coil4", new BlockDisplayBuilder()
                .blockData(Material.ORANGE_CONCRETE.createBlockData())
                .brightness(Utils.BRIGHTNESS_OFF)
                .transformation(new TransformationMatrixBuilder()
                        .scale(COIL_4_SCALE)
                        .rotate(COIL_ROTATION)
                        .translate(COIL_4_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        return displayGroup;
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }
    @Override
    protected void initBlockStorage(final @NotNull Location location) {
        BlockStorageAPI.set(location, Keys.BS_POWERED, false);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Link> link = getLink(location, "input");
        onPoweredAnimation(location, settings.isOperational(link));
        BlockStorageAPI.set(location, Keys.BS_POWERED, settings.isOperational(link));
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "coil1", powered);
        brightnessAnimation(location, "coil2", powered);
        brightnessAnimation(location, "coil3", powered);
        brightnessAnimation(location, "coil4", powered);
    }
}
