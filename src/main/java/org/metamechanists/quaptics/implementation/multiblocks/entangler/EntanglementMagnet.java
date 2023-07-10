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
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
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
            Material.BLUE_CONCRETE,
            "&6Entanglement Magnet",
            Lore.create(ENTANGLEMENT_MAGNET_SETTINGS,
                    "&7● Multiblock component"));

    private static final Vector3f BASE_SCALE = new Vector3f(0.80F, 0.70F, 0.80F);
    private static final Vector3f BASE_OFFSET = new Vector3f(0.0F, -0.35F, 0.0F);
    private static final Vector3f BASE_ROTATION = new Vector3f(0.0F, (float) (Math.PI / 4), 0.0F);
    private static final Vector3f COIL_SCALE = new Vector3f(0.20F, 0.70F, 0.20F);
    private static final Vector3f COIL_1_ROTATION = new Vector3f(0.0F, (float) (Math.PI / 4), 0.0F);
    private static final Vector3f COIL_2_ROTATION = new Vector3f(0.0F, (float) (-Math.PI / 4), 0.0F);
    private static final Vector3f COIL_3_ROTATION = new Vector3f(0.0F, (float) (Math.PI / 4), 0.0F);
    private static final Vector3f COIL_4_ROTATION = new Vector3f(0.0F, (float) (-Math.PI / 4), 0.0F);
    private static final Vector3f COIL_1_OFFSET = new Vector3f(0.0F, 0.3F, 0.3F);
    private static final Vector3f COIL_2_OFFSET = new Vector3f(0.0F, 0.3F, -0.3F);
    private static final Vector3f COIL_3_OFFSET = new Vector3f(0.0F, -0.3F, 0.3F);
    private static final Vector3f COIL_4_OFFSET = new Vector3f(0.0F, -0.3F, -0.3F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public EntanglementMagnet(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.55F;
    }
    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("base", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.GRAY_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(BASE_SCALE)
                        .translate(BASE_OFFSET)
                        .rotate(BASE_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("coil1", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.ORANGE_CONCRETE.createBlockData())
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(COIL_SCALE)
                        .translate(COIL_1_OFFSET)
                        .rotate(COIL_1_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("coil2", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.ORANGE_CONCRETE.createBlockData())
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(COIL_SCALE)
                        .translate(COIL_2_OFFSET)
                        .rotate(COIL_2_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("coil3", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.ORANGE_CONCRETE.createBlockData())
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(COIL_SCALE)
                        .translate(COIL_3_OFFSET)
                        .rotate(COIL_3_ROTATION)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("coil4", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.ORANGE_CONCRETE.createBlockData())
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(COIL_SCALE)
                        .translate(COIL_4_OFFSET)
                        .rotate(COIL_4_ROTATION)
                        .buildForBlockDisplay())
                .build());
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
