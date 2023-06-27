package org.metamechanists.quaptics.items;

import dev.sefiraat.sefilib.slimefun.itemgroup.DummyItemGroup;
import dev.sefiraat.sefilib.slimefun.itemgroup.SimpleFlexGroup;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import org.bukkit.Material;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.utils.Colors;
import org.metamechanists.quaptics.utils.Keys;

public class Groups {
    public static final SimpleFlexGroup MAIN = new SimpleFlexGroup(
            Quaptics.getInstance(),
            Colors.QUAPTICS.getString() + "Quaptics",
            Keys.MAIN,
            new CustomItemStack(Material.LIGHT_BLUE_STAINED_GLASS, Colors.QUAPTICS.getString() + "Quaptics"));

    public static final ItemGroup GUIDE = new DummyItemGroup(Keys.GUIDE,
            new CustomItemStack(Material.MAP, "&aGuide"));

    public static final ItemGroup TOOLS = new DummyItemGroup(Keys.TOOLS,
            new CustomItemStack(Material.GOLDEN_PICKAXE, "&fTools"));

    public static final ItemGroup PRIMITIVE = new DummyItemGroup(Keys.PRIMITIVE,
            new CustomItemStack(Material.BROWN_CONCRETE, Colors.PRIMITIVE.getString() + "Primitive Components"));

    public static final ItemGroup BASIC = new DummyItemGroup(Keys.BASIC,
            new CustomItemStack(Material.GRAY_CONCRETE, Colors.BASIC.getString() + "Basic Components"));

    public static final ItemGroup INTERMEDIATE = new DummyItemGroup(Keys.INTERMEDIATE,
            new CustomItemStack(Material.YELLOW_CONCRETE, Colors.INTERMEDIATE.getString() + "Intermediate Components"));

    public static final ItemGroup ADVANCED = new DummyItemGroup(Keys.ADVANCED,
            new CustomItemStack(Material.ORANGE_CONCRETE, Colors.ADVANCED.getString() + "Advanced Components"));

    public static void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();
        MAIN.addItemGroup(GUIDE);
        MAIN.addItemGroup(TOOLS);
        MAIN.addItemGroup(PRIMITIVE);
        MAIN.addItemGroup(BASIC);
        MAIN.addItemGroup(INTERMEDIATE);
        MAIN.addItemGroup(ADVANCED);
        MAIN.register(addon);
    }
}
