package org.metamechanists.quaptics.implementation.blocks.concentrators;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.EnergyConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;

import java.util.List;
import java.util.Optional;


public class EnergyConcentrator extends EnergyConnectedBlock implements PowerAnimatedBlock {
    public static final Settings ENERGY_CONCENTRATOR_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .connectionRadius(0.3F)
            .emissionPower(15)
            .energyConsumption(30)
            .energyCapacity(60)
            .build();
    public static final Settings ENERGY_CONCENTRATOR_2_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .connectionRadius(0.35F)
            .emissionPower(100)
            .energyConsumption(160)
            .energyCapacity(320)
            .build();
    public static final Settings ENERGY_CONCENTRATOR_3_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .connectionRadius(0.4F)
            .emissionPower(1250)
            .energyConsumption(680)
            .energyCapacity(1360)
            .build();
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Tier.BASIC.concreteMaterial,
            "&eEnergy Concentrator &bI",
            Lore.create(ENERGY_CONCENTRATOR_1_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Tier.INTERMEDIATE.concreteMaterial,
            "&eEnergy Concentrator &bII",
            Lore.create(ENERGY_CONCENTRATOR_2_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_3 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_3",
            Tier.ADVANCED.concreteMaterial,
            "&eEnergy Concentrator &bIII",
            Lore.create(ENERGY_CONCENTRATOR_3_SETTINGS,
                    "&7● Consumes energy",
                    "&7● Concentrates energy into a quaptic ray"));

    private final Vector outputLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());

    public EnergyConcentrator(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return settings.getConnectionRadius();
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("center", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.1F))
                .add("plate", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .rotation(Math.PI / 4)
                        .size(0.6F, 0.1F, 0.6F))
                .add("glass", new ModelCuboid()
                        .material(Material.GLASS)
                        .rotation(Math.PI / 4)
                        .size(0.4F))
                .build(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        super.onSlimefunTick(block, item, data);
        final Location location = block.getLocation();
        final double power = hasEnoughEnergy(location)
                ? settings.getEmissionPower()
                : 0;
        final Optional<Link> linkOptional = getLink(location, "output");
        onPoweredAnimation(location, linkOptional.isPresent() && hasEnoughEnergy(location));
        linkOptional.ifPresent(link -> link.setPower(power));
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "center", powered);
    }
}
