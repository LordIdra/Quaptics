package org.metamechanists.quaptics.items;

import dev.sefiraat.sefilib.slimefun.itemgroup.DummyItemGroup;
import dev.sefiraat.sefilib.slimefun.itemgroup.SimpleFlexGroup;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers.BeaconController;
import org.metamechanists.quaptics.items.groups.BeaconComponents;
import org.metamechanists.quaptics.items.groups.BeaconModules;
import org.metamechanists.quaptics.items.groups.BeamCreation;
import org.metamechanists.quaptics.items.groups.BeamManipulation;
import org.metamechanists.quaptics.items.groups.CraftingComponents;
import org.metamechanists.quaptics.items.groups.FrequencyAndPhase;
import org.metamechanists.quaptics.items.groups.Machines;
import org.metamechanists.quaptics.items.groups.Tools;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;

import java.util.LinkedHashMap;
import java.util.Map;


@UtilityClass
public class Groups {
    @Getter
    private final Map<String, ConnectedBlock> blocks = new LinkedHashMap<>();

    public final SimpleFlexGroup MAIN = new SimpleFlexGroup(
            Quaptics.getInstance(),
            Colors.QUAPTICS.getFormattedColor() + "Quaptics",
            Keys.MAIN,
            new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS, Colors.QUAPTICS.getFormattedColor() + "Quaptics"));

    public final ItemGroup GUIDE = new DummyItemGroup(Keys.GUIDE,
            new CustomItemStack(Material.MAP, "&aGuide"));

    public final ItemGroup CRAFTING_COMPONENTS = new DummyItemGroup(Keys.TOOLS,
            new CustomItemStack(Material.CLOCK, "&3Crafting Components"));

    public final ItemGroup TOOLS = new DummyItemGroup(Keys.TOOLS,
            new CustomItemStack(Material.DIAMOND_HORSE_ARMOR, "&9Tools"));

    public final ItemGroup BEAM_CREATION = new DummyItemGroup(Keys.BEAM_CREATION,
            new CustomItemStack(Material.GLASS_PANE, Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "Beam Creation"));
    public final ItemGroup BEAM_MANIPULATION = new DummyItemGroup(Keys.BEAM_MANIPULATION,
            new CustomItemStack(Material.WHITE_STAINED_GLASS, Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "Beam Manipulation"));
    public final ItemGroup FREQUENCY_AND_PHASE = new DummyItemGroup(Keys.FREQUENCY_AND_PHASE,
            new CustomItemStack(Material.RED_STAINED_GLASS, Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "Frequency & Phase"));
    public final ItemGroup MACHINES = new DummyItemGroup(Keys.MACHINES,
            new CustomItemStack(Material.DISPENSER, Colors.QUAPTIC_COMPONENTS.getFormattedColor() + "Machines"));

    public final ItemGroup BEACON_COMPONENTS = new DummyItemGroup(Keys.BEACON_COMPONENTS,
            new CustomItemStack(Material.DEEPSLATE_BRICK_WALL, Colors.BEACONS.getFormattedColor() + "Beacon Components"));
    public final ItemGroup BEACON_MODULES = new DummyItemGroup(Keys.BEACON_MODULES,
            new CustomItemStack(BeaconController.emptyItemStack(), Colors.BEACONS.getFormattedColor() + "Beacon Modules"));

    public final ItemGroup TESTING = new DummyItemGroup(Keys.TESTING,
            new CustomItemStack(Material.GRAY_CONCRETE, "&8Testing"));

    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        MAIN.addItemGroup(GUIDE);
        MAIN.addItemGroup(TOOLS);
        MAIN.addItemGroup(CRAFTING_COMPONENTS);
        MAIN.addItemGroup(BEAM_CREATION);
        MAIN.addItemGroup(BEAM_MANIPULATION);
        MAIN.addItemGroup(FREQUENCY_AND_PHASE);
        MAIN.addItemGroup(MACHINES);
        MAIN.addItemGroup(BEACON_COMPONENTS);
        MAIN.addItemGroup(BEACON_MODULES);
        MAIN.register(addon);

        Tools.initialize();
        CraftingComponents.initialize();
        BeamCreation.initialize();
        BeamManipulation.initialize();
        FrequencyAndPhase.initialize();
        Machines.initialize();
        BeaconComponents.initialize();
        BeaconModules.initialize();

        Slimefun.getRegistry().getAllSlimefunItems().stream()
                .filter(ConnectedBlock.class::isInstance)
                .map(ConnectedBlock.class::cast)
                .forEach(connectedBlock -> blocks.put(connectedBlock.getId(), connectedBlock));
    }
}
