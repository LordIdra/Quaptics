package org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers;

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
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBattery.BEACON_BATTERY;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconBeam.BEACON_BEAM;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconComputer.BEACON_COMPUTER;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconMatrix.BEACON_MATRIX;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPanel.BEACON_PANEL;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconPowerSupply.BEACON_POWER_SUPPLY;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconRod.BEACON_ROD;
import static org.metamechanists.quaptics.implementation.multiblocks.beacons.components.BeaconTransmitter.BEACON_TRANSMITTER;


public class BeaconController3 extends BeaconController {
    public static final Settings BEACON_CONTROLLER_3_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .minPower(20)
            .range(60)
            .build();
    public static final SlimefunItemStack BEACON_CONTROLLER_3 = new SlimefunItemStack(
            "QP_BEACON_CONTROLLER_3",
            Material.BLUE_CONCRETE,
            "&6Beacon Controller &eIII",
            Lore.create(BEACON_CONTROLLER_3_SETTINGS,
                    "&7● Part of the Beacon multiblock"));

    private static final Vector MATRIX_LOCATION = new Vector(0, 2, 0);
    private static final Vector COMPUTER_1_LOCATION = new Vector(1, 2, 0);
    private static final Vector COMPUTER_2_LOCATION = new Vector(-1, 2, 0);
    private static final Vector COMPUTER_3_LOCATION = new Vector(0, 2, 1);
    private static final Vector COMPUTER_4_LOCATION = new Vector(0, 2, -1);
    private static final Vector POWER_SUPPLY_LOCATION = new Vector(0, -1, 0);

    private static final Vector3f MODULE_1_LOCATION = new Vector3f(0.15F, -0.15F, 0.38F);
    private static final Vector3f MODULE_2_LOCATION = new Vector3f(-0.15F, -0.15F, 0.38F);
    private static final Vector3f MODULE_3_LOCATION = new Vector3f(0.15F, -0.15F, -0.38F);
    private static final Vector3f MODULE_4_LOCATION = new Vector3f(-0.15F, -0.15F, -0.38F);
    private static final Vector3f MODULE_5_LOCATION = new Vector3f(0.38F, -0.15F, 0.15F);
    private static final Vector3f MODULE_6_LOCATION = new Vector3f(0.38F, -0.15F, -0.15F);
    private static final Vector3f MODULE_7_LOCATION = new Vector3f(-0.38F, -0.15F, 0.15F);
    private static final Vector3f MODULE_8_LOCATION = new Vector3f(-0.38F, -0.15F, -0.15F);

    public BeaconController3(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        final DisplayGroup displayGroup = new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.BLUE_CONCRETE)
                        .size(0.8F, 1.0F, 0.8F))

                .add("module1", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_1_LOCATION)
                        .rotation(0))
                .add("module2", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_2_LOCATION)
                        .rotation(0))

                .add("module3", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_3_LOCATION)
                        .rotation(Math.PI))
                .add("module4", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_4_LOCATION)
                        .rotation(Math.PI))

                .add("module5", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_5_LOCATION)
                        .rotation(Math.PI / 2))
                .add("module6", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_6_LOCATION)
                        .rotation(Math.PI / 2))

                .add("module7", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_7_LOCATION)
                        .rotation(-Math.PI / 2))
                .add("module8", new ModelItem()
                        .item(getEmptyItemStack())
                        .brightness(Utils.BRIGHTNESS_ON)
                        .size(0.25F)
                        .location(MODULE_8_LOCATION)
                        .rotation(-Math.PI / 2))

                .buildAtBlockCenter(location);

        final ConnectionGroupId groupId = new ConnectionGroupId(displayGroup.getParentUUID());
        final List<InteractionId> interactionIds = new ArrayList<>();

        interactionIds.add(createButton(groupId, location, MODULE_1_LOCATION, "module1"));
        interactionIds.add(createButton(groupId, location, MODULE_2_LOCATION, "module2"));
        interactionIds.add(createButton(groupId, location, MODULE_3_LOCATION, "module3"));
        interactionIds.add(createButton(groupId, location, MODULE_4_LOCATION, "module4"));
        interactionIds.add(createButton(groupId, location, MODULE_5_LOCATION, "module5"));
        interactionIds.add(createButton(groupId, location, MODULE_6_LOCATION, "module6"));
        interactionIds.add(createButton(groupId, location, MODULE_7_LOCATION, "module7"));
        interactionIds.add(createButton(groupId, location, MODULE_8_LOCATION, "module8"));

        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroup.getParentUUID());
        traverser.setCustomIdList(Keys.BS_INTERACTION_ID_LIST, interactionIds);

        return displayGroup;
    }

    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        multiblockInteract(location.getBlock(), player);
        return true;
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location.clone().add(MATRIX_LOCATION), "main", powered);
        brightnessAnimation(location.clone().add(COMPUTER_1_LOCATION), "main", powered);
        brightnessAnimation(location.clone().add(COMPUTER_2_LOCATION), "main", powered);
        brightnessAnimation(location.clone().add(COMPUTER_3_LOCATION), "main", powered);
        brightnessAnimation(location.clone().add(COMPUTER_4_LOCATION), "main", powered);
        brightnessAnimation(location.clone().add(POWER_SUPPLY_LOCATION), "panel1", powered);
        brightnessAnimation(location.clone().add(POWER_SUPPLY_LOCATION), "panel2", powered);

    }

    @Override
    public Map<Vector, ItemStack> getStructure() {
        final Map<Vector, ItemStack> structure = new HashMap<>();

        structure.put(new Vector(0, -2, 0), BEACON_BATTERY);
        structure.put(new Vector(1, -2, 0), BEACON_BEAM);
        structure.put(new Vector(-1, -2, 0), BEACON_BEAM);
        structure.put(new Vector(0, -2, 1), BEACON_BEAM);
        structure.put(new Vector(0, -2, -1), BEACON_BEAM);

        structure.put(new Vector(0, -1, 0), BEACON_POWER_SUPPLY);

        structure.put(new Vector(0, 1, 0), BEACON_BEAM);
        structure.put(new Vector(1, 1, 0), BEACON_BEAM);
        structure.put(new Vector(-1, 1, 0), BEACON_BEAM);
        structure.put(new Vector(0, 1, 1), BEACON_BEAM);
        structure.put(new Vector(0, 1, -1), BEACON_BEAM);

        structure.put(new Vector(0, 2, 0), BEACON_MATRIX);
        structure.put(new Vector(1, 2, 0), BEACON_COMPUTER);
        structure.put(new Vector(1, 2, 0), BEACON_COMPUTER);
        structure.put(new Vector(0, 2, 1), BEACON_COMPUTER);
        structure.put(new Vector(0, 2, -1), BEACON_COMPUTER);

        structure.put(new Vector(0, 3, 0), BEACON_BEAM);
        structure.put(new Vector(1, 3, 0), BEACON_ROD);
        structure.put(new Vector(-1, 3, 0), BEACON_ROD);
        structure.put(new Vector(0, 3, 1), BEACON_ROD);
        structure.put(new Vector(0, 3, -1), BEACON_ROD);

        structure.put(new Vector(0, 4, 0), BEACON_BATTERY);
        structure.put(new Vector(1, 4, 0), BEACON_TRANSMITTER);
        structure.put(new Vector(-1, 4, 0), BEACON_TRANSMITTER);
        structure.put(new Vector(0, 4, 1), BEACON_TRANSMITTER);
        structure.put(new Vector(0, 4, -1), BEACON_TRANSMITTER);

        structure.put(new Vector(0, 5, 0), BEACON_BATTERY);

        structure.put(new Vector(0, 6, 0), BEACON_BEAM);

        structure.put(new Vector(0, 7, 0), BEACON_ROD);

        structure.put(new Vector(0, 8, 0), BEACON_TRANSMITTER);

        structure.put(new Vector(0, 9, 0), BEACON_PANEL);

        return structure;
    }
    @Override
    public Vector getPowerSupplyLocation() {
        return POWER_SUPPLY_LOCATION;
    }
    @Override
    protected List<String> getModuleDisplayNames() {
        return List.of("module1", "module2", "module3", "module4", "module5", "module6", "module7", "module8");
    }
}