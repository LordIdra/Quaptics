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
    private final int connections;
    private final Vector inputLocation = new Vector(0.0F, 0.0F, -radius);
    private final Vector3f mainDisplaySize = new Vector3f(radius*1.8F, radius*1.8F, radius*1.8F);
    private final Vector3f mainDisplayRotation = new Vector3f((float)(Math.PI/4), (float)(Math.PI/4), 0);
    private final double powerLoss;

    public Splitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe,
                    float radius, int connections, double maxPower, double powerLoss) {
        super(group, item, recipeType, recipe, radius, maxPower);
        this.connections = connections;
        this.powerLoss = powerLoss;
    }

    @Override
    protected void addDisplays(@NotNull DisplayGroup displayGroup, @NotNull Location location, Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.clone().add(RELATIVE_CENTER))
                        .setMaterial(Material.LIGHT_GRAY_STAINED_GLASS)
                        .setTransformation(Transformations.rotateAndScale(mainDisplaySize, mainDisplayRotation))
                        .build());
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(ConnectionGroupID groupID, Player player, Location location) {
        final List<ConnectionPoint> points = new ArrayList<>();

        points.add(new ConnectionPointInput(groupID, "input", formatPointLocation(player, location, inputLocation)));
        IntStream.range(0, connections).forEach(i -> {
            final String name = "output " + Objects.toString(i);
            final double angle = (-Math.PI / 8) + (Math.PI / 4) * ((double) (i) / connections);
            final Vector relativeLocation = new Vector(0.0F, 0.0F, radius).rotateAroundY(angle);
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

        doBurnoutCheck(group, input);

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

        outputs.stream()
                .filter(ConnectionPoint::hasLink)
                .forEach(output -> {
                    output.getLink().setPower(outputPower);
                    output.getLink().setEnabled(false);
                });
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.STRUCTURE_VOID;
    }
}
