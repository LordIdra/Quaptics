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
    private static final int BRIGHTNESS_ON = 15;
    private static final int VIEW_RANGE_ON = 64;
    private static final int VIEW_RANGE_OFF = 0;
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
                .setBrightness(BRIGHTNESS_ON)
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
        final List<ConnectionPointOutput> outputs = IntStream.range(0, connections)
                .mapToObj(i -> (ConnectionPointOutput) group.getPoint("output " + Objects.toString(i)))
                .toList();

        if (doBurnoutCheck(group, input)) {
            return;
        }

        if (input.hasLink() && input.getLink().isEnabled()) {
            getDisplayGroup(group.getLocation()).getDisplays().get("concrete").setViewRange(VIEW_RANGE_ON);
        } else {
            getDisplayGroup(group.getLocation()).getDisplays().get("concrete").setViewRange(VIEW_RANGE_OFF);
        }

        if (outputs.stream().noneMatch(ConnectionPoint::hasLink)) {
            return;
        }

        if (!input.hasLink() || !input.getLink().isEnabled()) {
            outputs.stream()
                    .filter(ConnectionPoint::hasLink)
                    .forEach(output -> output.getLink().setEnabled(false));
            return;
        }

        final long numberOfLinkedOutputs = outputs.stream().filter(ConnectionPoint::hasLink).count();
        final double outputPower = powerLoss(input.getLink().getPower(), powerLoss) / numberOfLinkedOutputs;
        final double outputFrequency = input.getLink().getFrequency() / numberOfLinkedOutputs;
        final int outputPhase = input.getLink().getPhase();

        outputs.stream()
                .filter(ConnectionPoint::hasLink)
                .forEach(output -> {
                    output.getLink().setPower(outputPower);
                    output.getLink().setFrequency(outputFrequency);
                    output.getLink().setPhase(outputPhase);
                    output.getLink().setEnabled(true);
                });
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
