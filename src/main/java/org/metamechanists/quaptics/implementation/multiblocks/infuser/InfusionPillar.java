package org.metamechanists.quaptics.implementation.multiblocks.infuser;

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

public class InfusionPillar extends ConnectedBlock implements PowerAnimatedBlock {
    public static final Settings INFUSION_PILLAR_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(7)
            .build();
    public static final SlimefunItemStack INFUSION_PILLAR = new SlimefunItemStack(
            "QP_INFUSION_PILLAR",
            Material.BLUE_CONCRETE,
            "&6Infusion Pillar",
            Lore.create(INFUSION_PILLAR_SETTINGS,
                    "&7● Multiblock component"));

    private static final Vector3f PILLAR_SCALE = new Vector3f(0.3F, 0.4F, 0.3F);
    private static final Vector3f PILLAR_OFFSET = new Vector3f(0.0F, -0.3F, 0.0F);
    private static final Vector3f PRISM_SCALE = new Vector3f(0.2F, 0.2F, 0.2F);
    private static final Vector INPUT_POINT_LOCATION = new Vector(0.0F, 0.0F, -0.3F);

    public InfusionPillar(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("pillar", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.BLUE_CONCRETE.createBlockData())
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PILLAR_SCALE)
                        .translate(PILLAR_OFFSET)
                        .buildForBlockDisplay())
                .build());
        displayGroup.addDisplay("prism", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.LIGHT_BLUE_STAINED_GLASS.createBlockData())
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .setTransformation(new TransformationMatrixBuilder()
                        .scale(PRISM_SCALE)
                        .buildForBlockDisplay())
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, INPUT_POINT_LOCATION)));
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
        brightnessAnimation(location, "prism", powered);
    }
}
