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
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;

import java.util.List;
import java.util.Optional;


public class EnergyConcentrator extends EnergyConnectedBlock implements PowerAnimatedBlock {
    public static final Settings ENERGY_CONCENTRATOR_1_SETTINGS = Settings.builder()
            .tier(Tier.BASIC)
            .operatingPowerHidden(true)
            .outputPower(30)
            .energyConsumption(120)
            .energyCapacity(240)
            .build();
    public static final Settings ENERGY_CONCENTRATOR_2_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .operatingPowerHidden(true)
            .outputPower(400)
            .energyConsumption(580)
            .energyCapacity(1160)
            .build();
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_1 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_1",
            Tier.BASIC.concreteMaterial,
            "&eEnergy Concentrator &6I",
            Lore.create(ENERGY_CONCENTRATOR_1_SETTINGS,
                    "&7● Concentrates energy into a quaptic ray"));
    public static final SlimefunItemStack ENERGY_CONCENTRATOR_2 = new SlimefunItemStack(
            "QP_ENERGY_CONCENTRATOR_2",
            Tier.INTERMEDIATE.concreteMaterial,
            "&eEnergy Concentrator &6II",
            Lore.create(ENERGY_CONCENTRATOR_2_SETTINGS,
                    "&7● Concentrates energy into a quaptic ray"));

    private final Vector outputLocation = new Vector(0.0F, 0.0F,getConnectionRadius());

    public EnergyConcentrator(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.55F;
    }
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("center", new ModelCuboid()
                        .material(settings.getTier().concreteMaterial)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .size(0.2F))
                .add("plate", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .rotation(Math.PI / 4)
                        .size(0.6F, 0.1F, 0.6F))
                .add("glass", new ModelCuboid()
                        .material(Material.GLASS)
                        .rotation(Math.PI / 4)
                        .size(0.4F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, outputLocation)));
    }

    @Override
    public void onSlimefunTick(@NotNull final Block block, final SlimefunItem item, final Config data) {
        super.onSlimefunTick(block, item, data);
        final Location location = block.getLocation();

        final boolean powered = hasEnoughEnergy(location);
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
