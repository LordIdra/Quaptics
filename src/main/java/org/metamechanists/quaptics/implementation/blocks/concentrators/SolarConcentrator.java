package org.metamechanists.quaptics.implementation.blocks.concentrators;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.models.components.ModelItem;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;

import java.util.List;
import java.util.Optional;


public class SolarConcentrator extends ConnectedBlock implements PowerAnimatedBlock {
    public static final Settings SOLAR_CONCENTRATOR_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .operatingPowerHidden(true)
            .outputPower(2)
            .build();
    public static final Settings SOLAR_CONCENTRATOR_2_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .operatingPowerHidden(true)
            .outputPower(8)
            .build();

    public static final SlimefunItemStack SOLAR_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_1",
            Material.GLASS_PANE,
            "&eSolar Concentrator &6I",
            Lore.create(SOLAR_CONCENTRATOR_1_SETTINGS,
                    "&7● Concentrates sunlight into a quaptic ray"));
    public static final SlimefunItemStack SOLAR_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_SOLAR_CONCENTRATOR_2",
            Material.GLASS_PANE,
            "&eSolar Concentrator &6II",
            Lore.create(SOLAR_CONCENTRATOR_2_SETTINGS,
                    "&7● Concentrates sunlight into a quaptic ray"));

    private final Vector outputLocation = new Vector(0.0F, 0.0F, getConnectionRadius());

    public SolarConcentrator(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.60F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("center", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.20F))
                .add("panel", new ModelItem()
                        .material(Material.GLASS_PANE)
                        .rotation(Math.PI / 2, 0, Math.PI / 4)
                        .size(0.90F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputLocation)));
    }


    @SuppressWarnings("unused")
    @Override
    public void onTick10(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final boolean powered = location.getWorld().isDayTime();
        onPoweredAnimation(location, powered);
        BlockStorageAPI.set(location, Keys.BS_POWERED, powered);

        final Optional<Link> linkOptional = getLink(location, "output");
        linkOptional.ifPresent(link -> link.setPower(powered ? settings.getOutputPower() : 0));
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        if (powered != BlockStorageAPI.getBoolean(location, Keys.BS_POWERED)) {
            brightnessAnimation(location, "center", powered);
        }
    }
}
