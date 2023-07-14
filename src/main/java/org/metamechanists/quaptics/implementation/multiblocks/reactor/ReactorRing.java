package org.metamechanists.quaptics.implementation.multiblocks.reactor;

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
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
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
import java.util.Optional;

public class ReactorRing extends ConnectedBlock implements PowerAnimatedBlock {
    public static final Settings REACTOR_RING_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .build();
    public static final SlimefunItemStack REACTOR_RING = new SlimefunItemStack(
            "QP_REACTOR_RING",
            Material.BLUE_CONCRETE,
            "&6Reactor Ring",
            Lore.create(REACTOR_RING_SETTINGS,
                    "&7● Multiblock component",
                    "&7● Has two inputs",
                    "&7● The more power you put in, the more power the reactor outputs"));

    private final List<Vector> possibleControllerLocations = List.of(
            new Vector(3, 0, 0),
            new Vector(2, 0, 2),
            new Vector(0, 0, 3),
            new Vector(-2, 0, 2),
            new Vector(-3, 0, 0),
            new Vector(-2, 0, -2),
            new Vector(0, 0, -3),
            new Vector(2, 0, -2)
        );

    public ReactorRing(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.7F;
    }
    @Override
    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {}
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        final Vector controllerPosition = findController(location);
        return new ModelBuilder()
                .add("ring1a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.47F, 0.2F)
                        .location(0, 0, 0.4F))
                .add("ring1b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.47F, 0.2F)
                        .location(0, 0, -0.4F))

                .add("ring2a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.47F)
                        .location(0, 0.4F, 0))
                .add("ring2b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.47F)
                        .location(0, -0.4F, 0))

                .add("ring3a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.38F)
                        .location(0, 0.3F, 0.3F)
                        .rotation(Math.PI/4, 0, 0))
                .add("ring3b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.38F)
                        .location(0, -0.3F, -0.3F)
                        .rotation(Math.PI/4, 0, 0))

                .add("ring4a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.38F, 0.2F)
                        .location(0, 0.3F, -0.3F)
                        .rotation(Math.PI/4, 0, 0))
                .add("ring4b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.38F, 0.2F)
                        .location(0, -0.3F, 0.3F)
                        .rotation(Math.PI/4, 0, 0))

                .add("connection1", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.15F, 0.15F, 0.2F)
                        .location(0, 0, 0.6F))
                .add("connection2", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.15F, 0.15F, 0.2F)
                        .location(0, 0, -0.6F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        final Vector controllerDirection = findController(location).normalize();
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input 1",
                        location.clone().toCenterLocation().add(controllerDirection.clone().multiply(getConnectionRadius()))),
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input 2",
                        location.clone().toCenterLocation().add(controllerDirection.clone().multiply(-getConnectionRadius()))));
    }
    @Override
    protected void initBlockStorage(final @NotNull Location location) {
        BlockStorageAPI.set(location, Keys.BS_INPUT_POWER, false);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input 1") || doBurnoutCheck(group, "input 2")) {
            return;
        }

        int inputPower = 0;
        final Optional<Link> link1 = getLink(location, "input 1");
        final Optional<Link> link2 = getLink(location, "input 2");
        if (link1.isPresent()) {
            inputPower += link1.get().getPower();
        }
        if (link2.isPresent()) {
            inputPower += link2.get().getPower();
        }

        // todo animation
        BlockStorageAPI.set(location, Keys.BS_INPUT_POWER, inputPower);
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        // todo
        brightnessAnimation(location, "prism", powered);
    }

    private Vector findController(@NotNull final Location location) {
        for (final Vector vector : possibleControllerLocations) {
            if (BlockStorageAPI.check(location.clone().add(vector)) instanceof ReactorController) {
                return vector;
            }
        }
        return new Vector(0, 0, 3);
    }
}
