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
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
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

    private static final Vector3f PILLAR_SCALE = new Vector3f(0.30F, 0.40F, 0.30F);
    private static final Vector3f PILLAR_OFFSET = new Vector3f(0.0F, -0.30F, 0.0F);
    private static final Vector3f PRISM_SCALE = new Vector3f(0.20F);

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public InfusionPillar(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.30F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        final DisplayGroup displayGroup = new DisplayGroup(location);
        displayGroup.addDisplay("pillar", new BlockDisplayBuilder()
                .blockData(Material.BLUE_CONCRETE.createBlockData())
                .transformation(new TransformationMatrixBuilder()
                        .scale(PILLAR_SCALE)
                        .translate(PILLAR_OFFSET)
                        .buildForBlockDisplay())
                .build(location.toCenterLocation()));
        displayGroup.addDisplay("prism", new BlockDisplayBuilder()
                .blockData(Material.LIGHT_BLUE_STAINED_GLASS.createBlockData())
                .brightness(Utils.BRIGHTNESS_OFF)
                .transformation(new TransformationMatrixBuilder()
                        .scale(PRISM_SCALE)
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
        brightnessAnimation(location, "prism", powered);
    }
}
