package org.metamechanists.quaptics.items.groups;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.metamechanists.quaptics.Quaptics;
import org.metamechanists.quaptics.items.Groups;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.utils.Colors;


@UtilityClass
public class Guide {
    private final SlimefunItemStack WHAT_IS_QUAPTICS = new SlimefunItemStack("QP_GUIDE_WHAT_IS_QUAPTICS", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "What Is Quaptics?",
            Lore.clickToOpen());
    private final SlimefunItemStack WHAT_IS_QUAPTICS_1 = new SlimefunItemStack("QP_GUIDE_WHAT_IS_QUAPTICS_1", Material.CYAN_CONCRETE,
            "&7Quaptics is all about creating, manipulating,",
            "&7and using quaptic rays, mysterious beams",
            "&7of power that can be used to power quaptic",
            "&7machines.");
    private final SlimefunItemStack WHAT_IS_QUAPTICS_2 = new SlimefunItemStack("QP_GUIDE_WHAT_IS_QUAPTICS_2", Material.CYAN_CONCRETE,
            "&7Quaptic Rays have properties such as power,",
            "&7frequency, and phase. Quaptic machines may",
            "&7require rays with some specific properties.");
    private final SlimefunItemStack WHAT_IS_QUAPTICS_3 = new SlimefunItemStack("QP_GUIDE_WHAT_IS_QUAPTICS_3", Material.CYAN_CONCRETE,
            "&7See the next guide entry 'Getting Started' to",
            "&7begin your journey into Quaptics.");

    private final SlimefunItemStack GETTING_STARTED = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Getting Started",
            Lore.clickToOpen());
    private final SlimefunItemStack GETTING_STARTED_1 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7Craft the following:",
            "&7- &eSolar Concentrator I",
            "&7- &eLens I",
            "&7- &eTargeting Wand");
    private final SlimefunItemStack GETTING_STARTED_2 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7Place down the &eSolar Concentrator I &7and",
            "&7the &eLens I &7close to each other.");
    private final SlimefunItemStack GETTING_STARTED_3 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_3", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 3",
            "&7Hold the &eTargeting Wand &7and right click",
            "&7the &aoutput &7on the &eSolar Concentrator I&7.");
    private final SlimefunItemStack GETTING_STARTED_4 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_4", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 4",
            "&7You've now selected an &aoutput. &7Next, right",
            "&7click the &cinput &7on the &eLens I&7. You might",
            "&7need to select the &aoutput &7again if you've",
            "&7deselected it.");
    private final SlimefunItemStack GETTING_STARTED_5 = new SlimefunItemStack("QP_GUIDE_GETTING_STARTED_5", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 5",
            "&7You've now created your first Quaptic link!",
            "&7You might have to wait until daytime to see",
            "&7the link, since the &eSolar Concentrator I",
            "&7only works during the day.");

    private final SlimefunItemStack VIEWING_CONNECTION_INFORMATION = new SlimefunItemStack("QP_GUIDE_VIEWING_CONNECTION_INFORMATION", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Viewing Connection Information",
            Lore.clickToOpen());
    private final SlimefunItemStack VIEWING_CONNECTION_INFORMATION_1 = new SlimefunItemStack("QP_GUIDE_VIEWING_CONNECTION_INFORMATION_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7You can view information about &cinputs &7and",
            "&aoutputs &7by right clicking them with an empty",
            "&7hand to bring up an information panel. Try it!");
    private final SlimefunItemStack VIEWING_CONNECTION_INFORMATION_2 = new SlimefunItemStack("QP_GUIDE_VIEWING_CONNECTION_INFORMATION_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7You can also shift right click a block to",
            "&7toggle the panels of all its points.");

    private final SlimefunItemStack USING_A_RAY_GUN = new SlimefunItemStack("QP_GUIDE_USING_A_RAY_GUN", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Using a Ray Gun",
            Lore.clickToOpen());
    private final SlimefunItemStack USING_A_RAY_GUN_1 = new SlimefunItemStack("QP_GUIDE_USING_A_RAY_GUN_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7Craft the following:",
            "&7- &eRay Gun I",
            "&7- &eCharger I");
    private final SlimefunItemStack USING_A_RAY_GUN_2 = new SlimefunItemStack("QP_GUIDE_USING_A_RAY_GUN_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7Place down the &eCharger I &7and connect",
            "&7it to a power source (as explained in",
            "&7the Getting Started section)");
    private final SlimefunItemStack USING_A_RAY_GUN_3 = new SlimefunItemStack("QP_GUIDE_USING_A_RAY_GUN_3", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 3",
            "&7Hold the &eRay Gun I &7and right click the",
            "&eCharger I&7. The &eRay Gun I &7should start",
            "&7charging. You might have to wait until daytime for",
            "&7the &eSolar Concentrator I &7to start working.");
    private final SlimefunItemStack USING_A_RAY_GUN_4 = new SlimefunItemStack("QP_GUIDE_USING_A_RAY_GUN_4", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 4",
            "&7When the &eRay Gun I &7is charged, right click",
            "&7to take it from the charger, and then right",
            "&7click with the &eRay Gun I &7to fire at a target.");
    private final SlimefunItemStack USING_A_RAY_GUN_5 = new SlimefunItemStack("QP_GUIDE_USING_A_RAY_GUN_5", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 5",
            "&7Now go explore some of the other components!",
            "&7You might want to look at:",
            "&7- &eSplitter I",
            "&7- &eCombiner I",
            "&7- &eCapacitor I",
            "&7- &eTurret I");

    private final SlimefunItemStack BURNOUT = new SlimefunItemStack("QP_GUIDE_BURNOUT", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Component Burnout (and how to avoid it)",
            Lore.clickToOpen());
    private final SlimefunItemStack BURNOUT_1 = new SlimefunItemStack("QP_GUIDE_BURNOUT_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7Once you get into the " + Colors.BASIC.getFormattedColor() + "Basic &7tier, you might",
            "&7start encountering component burnout.");
    private final SlimefunItemStack BURNOUT_2 = new SlimefunItemStack("QP_GUIDE_BURNOUT_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7This occurs when you put too high a power",
            "&7through a component, and it explodes.");
    private final SlimefunItemStack BURNOUT_3 = new SlimefunItemStack("QP_GUIDE_BURNOUT_3", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 3",
            "&7Avoid burnout by using the connection",
            "&7panels to check you're not about to put",
            "&7too much power through a component.");
    private final SlimefunItemStack BURNOUT_4 = new SlimefunItemStack("QP_GUIDE_BURNOUT_4", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 4",
            "&7You can also use Transformers to decrease",
            "&7the power of a ray to safe limits.");

    private final SlimefunItemStack INCREASING_FREQUENCY = new SlimefunItemStack("QP_GUIDE_INCREASING_FREQUENCY", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Increasing Frequency",
            Lore.clickToOpen());
    private final SlimefunItemStack INCREASING_FREQUENCY_1 = new SlimefunItemStack("QP_GUIDE_INCREASING_FREQUENCY_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7More powerful machines require Quaptic rays",
            "&7to be above a certain frequency. Let's increase",
            "&7the frequency of a ray using repeaters.");
    private final SlimefunItemStack INCREASING_FREQUENCY_2 = new SlimefunItemStack("QP_GUIDE_INCREASING_FREQUENCY_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7Craft the following:",
            "&7- &eEnergy Concentrator I",
            "&7- &eRepeater I &7(x3)");
    private final SlimefunItemStack INCREASING_FREQUENCY_3 = new SlimefunItemStack("QP_GUIDE_INCREASING_FREQUENCY_3", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 3",
            "&7Place the components down and link the",
            "&eEnergy Concentrator I &7to a repeater, and",
            "&7chain the repeaters together");
    private final SlimefunItemStack INCREASING_FREQUENCY_4 = new SlimefunItemStack("QP_GUIDE_INCREASING_FREQUENCY_4", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 4",
            "&7The output of the last repeater in the chain",
            "&7should be enough to power a &eMultiblock",
            "&eClicker I&7. Try it!");
    private final SlimefunItemStack INCREASING_FREQUENCY_5 = new SlimefunItemStack("QP_GUIDE_INCREASING_FREQUENCY_5", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 5",
            "&7You'll need to use higher tier Repeaters, as",
            "&7well as Scatterers (and later Diffraction Gratings)",
            "&7to further increase frequency.");

    private final SlimefunItemStack BUILDING_MULTIBLOCKS = new SlimefunItemStack("QP_GUIDE_BUILDING_MULTIBLOCKS", Material.WHITE_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Building Multiblocks",
            Lore.clickToOpen());
    private final SlimefunItemStack BUILDING_MULTIBLOCKS_1 = new SlimefunItemStack("QP_GUIDE_BUILDING_MULTIBLOCKS_1", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 1",
            "&7The first multiblock you'll need to build",
            "&7is an Infuser.");
    private final SlimefunItemStack BUILDING_MULTIBLOCKS_2 = new SlimefunItemStack("QP_GUIDE_BUILDING_MULTIBLOCKS_2", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 2",
            "&7Craft the following:",
            "&7- &eMultiblock Wand",
            "&7- &eInfusion Container",
            "&7- &eInfusion Pillar &7(x4)");
    private final SlimefunItemStack BUILDING_MULTIBLOCKS_3 = new SlimefunItemStack("QP_GUIDE_BUILDING_MULTIBLOCKS_3", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 3",
            "&7Place down the &eInfusion Container &7and",
            "&7right click it with the &eMultiblock Wand&7.",
            "&7The multiblock structure will be projected.");
    private final SlimefunItemStack BUILDING_MULTIBLOCKS_4 = new SlimefunItemStack("QP_GUIDE_BUILDING_MULTIBLOCKS_4", Material.CYAN_CONCRETE,
            Colors.QUAPTICS.getFormattedColor() + "Step 4",
            "&7Right click each projected block to see",
            "&7which block it is, and place them as shown.",
            "&7That's it!");


    public void initialize() {
        final SlimefunAddon addon = Quaptics.getInstance();

        new SlimefunItem(Groups.GUIDE, WHAT_IS_QUAPTICS, RecipeType.NULL, new ItemStack[]{
                WHAT_IS_QUAPTICS_1, WHAT_IS_QUAPTICS_2, WHAT_IS_QUAPTICS_3,
                null, null, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.GUIDE, GETTING_STARTED, RecipeType.NULL, new ItemStack[]{
                GETTING_STARTED_1, GETTING_STARTED_2, GETTING_STARTED_3,
                GETTING_STARTED_4, GETTING_STARTED_5, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.GUIDE, VIEWING_CONNECTION_INFORMATION, RecipeType.NULL, new ItemStack[]{
                VIEWING_CONNECTION_INFORMATION_1, VIEWING_CONNECTION_INFORMATION_2, null,
                null, null, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.GUIDE, USING_A_RAY_GUN, RecipeType.NULL, new ItemStack[]{
                USING_A_RAY_GUN_1, USING_A_RAY_GUN_2, USING_A_RAY_GUN_3,
                USING_A_RAY_GUN_4, USING_A_RAY_GUN_5, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.GUIDE, BURNOUT, RecipeType.NULL, new ItemStack[]{
                BURNOUT_1, BURNOUT_2, BURNOUT_3,
                BURNOUT_4, null, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.GUIDE, INCREASING_FREQUENCY, RecipeType.NULL, new ItemStack[]{
                INCREASING_FREQUENCY_1, INCREASING_FREQUENCY_2, INCREASING_FREQUENCY_3,
                INCREASING_FREQUENCY_4, INCREASING_FREQUENCY_5, null,
                null, null, null
        }).register(addon);

        new SlimefunItem(Groups.GUIDE, BUILDING_MULTIBLOCKS, RecipeType.NULL, new ItemStack[]{
                BUILDING_MULTIBLOCKS_1, BUILDING_MULTIBLOCKS_2, BUILDING_MULTIBLOCKS_3,
                BUILDING_MULTIBLOCKS_4, null, null,
                null, null, null
        }).register(addon);
    }
}
