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
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelDiamond;

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

    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -getConnectionRadius());

    public EntanglementMagnet(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.45F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("diamond", new ModelDiamond()
                        .material(Material.GRAY_CONCRETE)
                        .size(0.5F))
                .add("panel1", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.4F, 0.2F, 0.2F)
                        .rotation(ModelDiamond.ROTATION))
                .add("panel2", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.2F, 0.4F, 0.2F)
                        .rotation(ModelDiamond.ROTATION))
                .add("panel3", new ModelCuboid()
                        .material(Material.ORANGE_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.2F, 0.2F, 0.4F)
                        .rotation(ModelDiamond.ROTATION))
                .buildAtBlockCenter(location);
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
        brightnessAnimation(location, "panel1", powered);
        brightnessAnimation(location, "panel2", powered);
        brightnessAnimation(location, "panel3", powered);
    }
}
