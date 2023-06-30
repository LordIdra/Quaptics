package org.metamechanists.quaptics.implementation.blocks.upgraders;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.List;
import java.util.Objects;

public class Repeater extends ConnectedBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Vector3f glassDisplaySize = new Vector3f(settings.getDisplayRadius()*2);
    private final Vector3f repeaterDisplaySize = new Vector3f(settings.getDisplayRadius());
    private final Vector3f repeaterOffset = new Vector3f(0.0F, 0.3F, 0.0F);
    private final Vector3f concreteDisplaySize = new Vector3f(settings.getDisplayRadius()+0.01F, 0.075F, settings.getDisplayRadius()+0.01F);
    private final Vector3f concreteOffset = new Vector3f(0.0F, 0.15F, 0.0F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0.0F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());
    private final Vector outputPointLocation = new Vector(0.0F, 0.0F, settings.getConnectionRadius());
    private final int delayVisual;

    public Repeater(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                    Settings settings, int delayVisual) {
        super(group, item, recipeType, recipe, settings);
        this.delayVisual = delayVisual;
    }

    private void setRepeaterPowered(Location location, boolean powered) {
        final BlockDisplay display = (BlockDisplay) getDisplay(location, "repeater");
        if (display != null) {
            display.setBlock(Material.REPEATER.createBlockData(display.getBlock().getAsString(true)
                            .replace("powered=" + Objects.toString(!powered), "powered=" + Objects.toString(powered))));
        }
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, @NotNull Player player) {
        final BlockFace face = Transformations.yawToFace(player.getEyeLocation().getYaw());
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.RED_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, mainDisplayRotation))
                .build());
        displayGroup.addDisplay("repeater", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.REPEATER)
                .setBlockData(Material.REPEATER.createBlockData("[delay=" + delayVisual
                        + ",facing=" + face.name().toLowerCase()
                        + ",powered=false"
                        + "]"))
                .setTransformation(Transformations.adjustedScaleAndOffset(repeaterDisplaySize, repeaterOffset))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(settings.getTier().material)
                .setTransformation(Transformations.adjustedScaleAndOffset(concreteDisplaySize, concreteOffset))
                .setBrightness(CONCRETE_BRIGHTNESS)
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        return List.of(
                new ConnectionPointInput(groupID, "input", formatPointLocation(player, location, inputPointLocation)),
                new ConnectionPointOutput(groupID, "output", formatPointLocation(player, location, outputPointLocation)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final ConnectionPointInput input = group.getInput("input");
        final ConnectionPointOutput output = group.getOutput("output");

        if (doBurnoutCheck(group, input)) {
            return;
        }

        doDisplayBrightnessCheck(group.getLocation(), "concrete", false);

        if (!output.hasLink()) {
            return;
        }

        if (!input.isLinkEnabled()) {
            setRepeaterPowered(group.getLocation(), false);
            output.disableLinkIfExists();
            return;
        }

        double newFrequency = input.getLink().getFrequency();
        if (input.getLink().getFrequency() < settings.getMaxFrequency()) {
            newFrequency += settings.getFrequencyStep();
        }

        setRepeaterPowered(group.getLocation(), true);
        output.getLink().setAttributes(
                powerLoss(input.getLink().getPower(), settings.getPowerLoss()),
                newFrequency,
                input.getLink().getPhase(),
                true);
    }
}
