package org.metamechanists.quaptics.implementation.multiblocks.reactor;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.displaymodellib.sefilib.entity.display.DisplayGroup;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.displaymodellib.models.ModelBuilder;
import org.metamechanists.displaymodellib.models.components.ModelCuboid;

import java.util.List;

public class ReactorRing extends ConnectedBlock {
    public static final Settings REACTOR_RING_SETTINGS = Settings.builder()
            .tier(Tier.INTERMEDIATE)
            .build();
    public static final SlimefunItemStack REACTOR_RING = new SlimefunItemStack(
            "QP_REACTOR_RING",
            Material.WHITE_CONCRETE,
            "&6Reactor Ring",
            Lore.create(REACTOR_RING_SETTINGS,
                    Lore.multiblockComponent()));

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
                        .size(0.2F, 0.477F, 0.2F)
                        .location(0, 0, 0.4F))
                .add("ring1b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.477F, 0.2F)
                        .location(0, 0, -0.4F))

                .add("ring2a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.477F)
                        .location(0, 0.4F, 0))
                .add("ring2b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.477F)
                        .location(0, -0.4F, 0))

                .add("ring3a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.365F)
                        .location(0, 0.3F, 0.3F)
                        .rotation(Math.PI/4, 0, 0))
                .add("ring3b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.2F, 0.365F)
                        .location(0, -0.3F, -0.3F)
                        .rotation(Math.PI/4, 0, 0))

                .add("ring4a", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.365F, 0.2F)
                        .location(0, 0.3F, -0.3F)
                        .rotation(Math.PI/4, 0, 0))
                .add("ring4b", new ModelCuboid()
                        .material(Material.WHITE_CONCRETE)
                        .facing(controllerPosition.toVector3f())
                        .size(0.2F, 0.365F, 0.2F)
                        .location(0, -0.3F, 0.3F)
                        .rotation(Math.PI/4, 0, 0))

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
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input",
                        location.clone().toCenterLocation().add(controllerDirection.clone().multiply(-getConnectionRadius()))));
    }
    @Override
    protected void initBlockStorage(final @NotNull Location location) {
        BlockStorageAPI.set(location, Keys.BS_INPUT_POWER, 0.0);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final double inputPower = getLink(location, "input").map(Link::getPower).orElse(0.0);
        BlockStorageAPI.set(location, Keys.BS_INPUT_POWER, inputPower);
    }

    private @NotNull Vector findController(@NotNull final Location location) {
        for (final Vector vector : possibleControllerLocations) {
            if (BlockStorageAPI.check(location.clone().add(vector)) instanceof ReactorController) {
                return vector.clone();
            }
        }
        return new Vector(0, 0, 3);
    }
}
