package org.metamechanists.quaptics.implementation.beacons.components;

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
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;

import java.util.List;


public class BeaconPowerSupply extends ConnectedBlock {
    public static final Settings BEACON_POWER_SUPPLY_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack BEACON_POWER_SUPPLY = new SlimefunItemStack(
            "QP_BEACON_POWER_SUPPLY",
            Material.BLACK_CONCRETE,
            "&6Beacon Power Supply",
            Lore.create(BEACON_POWER_SUPPLY_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    public BeaconPowerSupply(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0;
    }
    @Override
    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {}
    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.BLACK_CONCRETE)
                        .size(0.6F, 1.0F, 0.6F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input 1", location.clone().toCenterLocation().add(new Vector(0.3, 0, 0))),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input 2", location.clone().toCenterLocation().add(new Vector(-0.3, 0, 0))),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input 3", location.clone().toCenterLocation().add(new Vector(0, 0, 0.3))),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input 4", location.clone().toCenterLocation().add(new Vector(0, 0, -0.3))));
    }
    @Override
    protected void initBlockStorage(final @NotNull Location location) {
        BlockStorageAPI.set(location, Keys.BS_INPUT_POWER, 0.0);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final List<ConnectionPoint> enabledInputs = getEnabledInputs(location);
        if (doBurnoutCheck(group, enabledInputs)) {
            return;
        }

        final double inputPower = getIncomingLinks(location).stream().mapToDouble(Link::getPower).sum();
        BlockStorageAPI.set(location, Keys.BS_INPUT_POWER, inputPower);
    }
}
