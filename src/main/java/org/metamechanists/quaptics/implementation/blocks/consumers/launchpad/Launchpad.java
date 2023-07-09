package org.metamechanists.quaptics.implementation.blocks.consumers.launchpad;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.ConfigPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.panels.config.ConfigPanel;
import org.metamechanists.quaptics.panels.config.implementation.LaunchpadConfigPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConfigPanelId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;

public class Launchpad extends ConnectedBlock implements ConfigPanelBlock, PowerAnimatedBlock {
    public static final Settings LAUNCHPAD_SETTINGS = Settings.builder()
            .tier(Tier.PRIMITIVE)
            .connectionRadius(0.8F)
            .minPower(3)
            .build();
    public static final SlimefunItemStack LAUNCHPAD = new SlimefunItemStack(
            "QP_LAUNCHPAD",
            Material.LIGHT_GRAY_CONCRETE,
            "&6Launchpad",
            Lore.create(LAUNCHPAD_SETTINGS,
                    "&7● Launches players",
                    "&7● Launch velocity can be configured",
                    "&7● &eWalk onto the launchpad &7to get launched"));

    private static final Vector RELATIVE_PANEL_LOCATION = new Vector(0, 0, -0.51);
    private static final Vector INITIAL_VELOCITY = new Vector(0, 0, 0);
    public static final float MAX_VELOCITY = 10;
    private static final float VELOCITY_POWER = 1.5F;
    private static final float VELOCITY_DIVISOR = 5;
    private static final Vector3f MAIN_DISPLAY_SIZE = new Vector3f(0.8F, 0.1F, 0.8F);
    private static final Vector3f MAIN_DISPLAY_OFFSET = new Vector3f(0, 0.51F, 0);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public Launchpad(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void initDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("main", new BlockDisplayBuilder(location.toCenterLocation())
                .setBlockData(Material.CYAN_CONCRETE_POWDER.createBlockData())
                .setTransformation(Transformations.adjustedScaleOffset(MAIN_DISPLAY_SIZE, MAIN_DISPLAY_OFFSET))
                .setBrightness(Utils.BRIGHTNESS_OFF)
                .build());
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }
    @Override
    protected void initBlockStorage(@NotNull final Location location) {
        BlockStorageAPI.set(location, Keys.BS_VELOCITY, INITIAL_VELOCITY);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        onPlaceConfigPanelBlock(event);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakConfigPanelBlock(location);
    }
    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Link> inputLink = getLink(location, "input");
        onPoweredAnimation(location, inputLink.isPresent() && settings.isOperational(inputLink));
    }
    @Override
    public void onPoweredAnimation(final Location location, final boolean powered) {
        brightnessAnimation(location, "main", powered);
    }

    @Override
    public ConfigPanel createPanel(final Location location, final Player player, @NotNull final ConnectionGroup group) {
        return new LaunchpadConfigPanel(formatPointLocation(player, location, RELATIVE_PANEL_LOCATION), group.getId(),
                (float) Transformations.yawToCardinalDirection(player.getEyeLocation().getYaw()));
    }
    @Override
    public ConfigPanel getPanel(final ConfigPanelId panelId, final ConnectionGroupId groupId) {
        return new LaunchpadConfigPanel(panelId, groupId);
    }

    @Override
    protected @NotNull Material getBaseMaterial() {
        return Material.LIGHT_GRAY_CONCRETE;
    }

    public void launch(final Location location, final Player player) {
        final Optional<Link> inputLink = getLink(location, "input");
        if (inputLink.isEmpty() || !settings.isOperational(inputLink)) {
            return;
        }

        final Optional<Vector> velocity = BlockStorageAPI.getVector(location, Keys.BS_VELOCITY);
        if (velocity.isEmpty()) {
            return;
        }

        velocity.get().setX(calculateFinalVelocity(velocity.get().getX()));
        velocity.get().setY(calculateFinalVelocity(velocity.get().getY()));
        velocity.get().setZ(calculateFinalVelocity(velocity.get().getZ()));

        velocity.get().multiply(1.0/VELOCITY_DIVISOR);

        player.setVelocity(velocity.get());
    }
    private static double calculateFinalVelocity(final double velocity) {
        return velocity >= 0
                ? Math.pow(velocity, VELOCITY_POWER)
                : -Math.pow(Math.abs(velocity), VELOCITY_POWER);

    }
}