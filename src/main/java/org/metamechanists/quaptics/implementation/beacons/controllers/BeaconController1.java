package org.metamechanists.quaptics.implementation.beacons.controllers;

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
import org.metamechanists.quaptics.implementation.attachments.ComplexMultiblock;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelItem;

import java.util.HashMap;
import java.util.Map;

import static org.metamechanists.quaptics.implementation.beacons.components.BeaconBeam.BEACON_BEAM;
import static org.metamechanists.quaptics.implementation.beacons.components.BeaconComputer.BEACON_COMPUTER;
import static org.metamechanists.quaptics.implementation.beacons.components.BeaconPanel.BEACON_PANEL;
import static org.metamechanists.quaptics.implementation.beacons.components.BeaconPowerSupply.BEACON_POWER_SUPPLY;
import static org.metamechanists.quaptics.implementation.beacons.components.BeaconTransmitter.BEACON_TRANSMITTER;


public class BeaconController1 extends BeaconController implements ComplexMultiblock, PowerAnimatedBlock {
    public static final Settings BEACON_CONTROLLER_1_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack BEACON_CONTROLLER_1 = new SlimefunItemStack(
            "QP_BEACON_CONTROLLER_1",
            Material.BLACK_CONCRETE,
            "&6Beacon Controller &eI",
            Lore.create(BEACON_CONTROLLER_1_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    public static final Vector COMPUTER_LOCATION = new Vector(0, 3, 0);

    public BeaconController1(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.BLUE_CONCRETE)
                        .size(0.8F, 1.0F, 0.8F)
                        .rotation(Math.PI/4))

                .add("module1", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .size(0.2F, 0.4F, 0.2F)
                        .location(0.4F, 0, 0.4F)
                        .rotation(Math.PI * 1/4))
                .add("module1", new ModelItem()
                        .material(Material.BLACK_BANNER)
                        .size(0.2F)
                        .location(0.4F, 0, 0.4F)
                        .rotation(Math.PI * 1/4))

                .buildAtBlockCenter(location);
    }

    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {

    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        final Map<Vector, ItemStack> structure = new HashMap<>();

        structure.put(new Vector(0, 1, 0), BEACON_POWER_SUPPLY);
        structure.put(new Vector(0, 2, 0), BEACON_BEAM);
        structure.put(COMPUTER_LOCATION, BEACON_COMPUTER);
        structure.put(new Vector(0, 4, 0), BEACON_BEAM);
        structure.put(new Vector(0, 4, 0), BEACON_TRANSMITTER);

        structure.put(new Vector(1, 2, 0), BEACON_TRANSMITTER);
        structure.put(new Vector(1, 3, 0), BEACON_PANEL);
        structure.put(new Vector(-1, 2, 0), BEACON_TRANSMITTER);
        structure.put(new Vector(-1, 3, 0), BEACON_PANEL);
        structure.put(new Vector(0, 2, 1), BEACON_TRANSMITTER);
        structure.put(new Vector(0, 3, 1), BEACON_PANEL);
        structure.put(new Vector(0, 2, -1), BEACON_TRANSMITTER);
        structure.put(new Vector(0, 3, -1), BEACON_PANEL);

        return structure;
    }
}
