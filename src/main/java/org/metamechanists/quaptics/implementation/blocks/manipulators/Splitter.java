package org.metamechanists.quaptics.implementation.blocks.manipulators;

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
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.points.ConnectionPoint;
import org.metamechanists.quaptics.connections.points.ConnectionPointInput;
import org.metamechanists.quaptics.connections.points.ConnectionPointOutput;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.ConnectionGroupID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Splitter extends ConnectedBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Material concreteMaterial;
    private final double connectionAngle = Math.PI / 2;
    private final int connections;
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -connectionRadius);
    private final Vector outputStartingLocation = new Vector(0.0F, 0.0F, connectionRadius);
    private final Vector3f glassDisplaySize = new Vector3f(displayRadius*2);
    private final Vector3f concreteDisplaySize = new Vector3f(displayRadius);
    private final Vector3f displayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final double powerLoss;

    public Splitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                    Material concreteMaterial, float radius, int connections, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, radius, 2*radius, maxPower);
        this.concreteMaterial = concreteMaterial;
        this.connections = connections;
        this.powerLoss = powerLoss;
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIGHT_GRAY_STAINED_GLASS)
                .setTransformation(Transformations.adjustedRotateAndScale(glassDisplaySize, displayRotation))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(concreteMaterial)
                .setBrightness(CONCRETE_BRIGHTNESS)
                .setViewRange(VIEW_RANGE_OFF)
                .setTransformation(Transformations.adjustedRotateAndScale(concreteDisplaySize, displayRotation))
                .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();

        points.add(new ConnectionPointInput(groupID, "input", formatPointLocation(player, location, inputLocation)));
        IntStream.range(0, connections).forEach(i -> {
            final String name = "output " + Objects.toString(i);
            final double angle = (-connectionAngle/2) + connectionAngle*((double)(i) / (connections-1));
            final Vector relativeLocation = outputStartingLocation.clone().rotateAroundY(angle);
            points.add(new ConnectionPointOutput(groupID, name, formatPointLocation(player, location, relativeLocation)));
        });

        return points;
    }

    @Override
    public void onInputLinkUpdated(@NotNull ConnectionGroup group) {
        final ConnectionPointInput input = (ConnectionPointInput) group.getPoint("input");
        final List<ConnectionPointOutput> linkedOutputs = getLinkedOutputs(group.getLocation());

        if (doBurnoutCheck(group, input)) {
            return;
        }

        doDisplayBrightnessCheck(group.getLocation(), "concrete");

        if (linkedOutputs.isEmpty()) {
            return;
        }

        if (!input.isLinkEnabled()) {
            linkedOutputs.forEach(output -> output.getLink().setEnabled(false));
            return;
        }

        final double outputPower = powerLoss(input.getLink().getPower(), powerLoss) / linkedOutputs.size();
        final double outputFrequency = input.getLink().getFrequency();
        final int outputPhase = input.getLink().getPhase();

        linkedOutputs.forEach(output -> output.getLink().setAttributes(outputPower, outputFrequency, outputPhase, true));
    }
}
