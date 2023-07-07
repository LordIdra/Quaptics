package org.metamechanists.quaptics.implementation.blocks.consumers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.metamechanists.metalib.utils.ExperienceUtils;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.blocks.Settings;
import org.metamechanists.quaptics.implementation.blocks.attachments.InfoPanelBlock;
import org.metamechanists.quaptics.implementation.blocks.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.blocks.base.ConnectedBlock;
import org.metamechanists.quaptics.panels.info.BlockInfoPanel;
import org.metamechanists.quaptics.panels.info.InfoPanelContainer;
import org.metamechanists.quaptics.panels.info.implementation.CapacitorInfoPanel;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Transformations;
import org.metamechanists.quaptics.utils.builders.BlockDisplayBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.InfoPanelId;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ExperienceBattery extends ConnectedBlock implements InfoPanelBlock, PowerLossBlock {
    private static final int CONCRETE_BRIGHTNESS = 15;
    private final Vector3f mainGlassDisplaySize = new Vector3f(settings.getDisplayRadius()*2.0F);
    private final Vector3f maxConcreteDisplaySize = new Vector3f(settings.getDisplayRadius()*1.9F);
    private final Vector inputPointLocation = new Vector(0.0F, 0.0F, -settings.getConnectionRadius());

    public ExperienceBattery(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected void addDisplays(@NotNull final DisplayGroup displayGroup, @NotNull final Location location, final @NotNull Player player) {
        displayGroup.addDisplay("mainGlass", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.GLASS)
                .setTransformation(Transformations.adjustedRotateScale(mainGlassDisplaySize, Transformations.GENERIC_ROTATION_ANGLES))
                .build());
        displayGroup.addDisplay("concrete", new BlockDisplayBuilder(location.toCenterLocation())
                .setMaterial(Material.LIME_CONCRETE)
                .setTransformation(Transformations.none())
                .setBrightness(CONCRETE_BRIGHTNESS)
                .build());
        BlockStorageAPI.set(location, Keys.BS_EXPERIENCE, 0);
    }

    @Override
    protected List<ConnectionPoint> generateConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, inputPointLocation)));
    }

    @Override
    public BlockInfoPanel createPanel(final InfoPanelId panelId, final ConnectionGroupId groupId) {
        return new CapacitorInfoPanel(panelId, groupId);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onPlace(@NotNull final BlockPlaceEvent event) {
        super.onPlace(event);
        final Location location = event.getBlock().getLocation();
        final Optional<ConnectionGroup> optionalGroup = getGroup(location);
        optionalGroup.ifPresent(group -> InfoPanelBlock.setPanelId(location, new CapacitorInfoPanel(location, group.getId()).getId()));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        InfoPanelBlock.getPanelId(location)
                .flatMap(InfoPanelId::get)
                .ifPresent(InfoPanelContainer::remove);
    }

    @Override
    protected void onRightClick(final @NotNull Location location, final @NotNull Player player) {
        if (!BlockStorageAPI.getBoolean(location, Keys.BS_ENABLED)) {
            return;
        }

        if (player.isSneaking()) {
            BlockStorageAPI.set(location, Keys.BS_CHARGING, false);
            BlockStorageAPI.set(location, Keys.BS_DISCHARGING, true);
            return;
        }

        BlockStorageAPI.set(location, Keys.BS_CHARGING, true);
        BlockStorageAPI.set(location, Keys.BS_DISCHARGING, false);
    }

    @Override
    public void onQuapticTick(@NotNull final ConnectionGroup group) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final boolean charging = BlockStorageAPI.getBoolean(location.get(), Keys.BS_CHARGING);
        final boolean discharging = BlockStorageAPI.getBoolean(location.get(), Keys.BS_DISCHARGING);
        if (!charging && !discharging) {
            return;
        }

        final Optional<UUID> uuid = BlockStorageAPI.getUuid(location.get(), Keys.BS_PLAYER);
        if (uuid.isEmpty()) {
            return;
        }

        final Player player = Bukkit.getPlayer(uuid.get());
        if (player == null) {
            return;
        }

        int playerExperience = ExperienceUtils.getTotalExperience(player);
        int experience = BlockStorageAPI.getInt(location.get(), Keys.BS_EXPERIENCE);

        if (charging) {
            playerExperience -= settings.getExperienceTransferRate();
            experience += settings.getExperienceTransferRate();

            if (playerExperience == 0) {
                BlockStorageAPI.set(location.get(), Keys.BS_CHARGING, false);
                return;
            }

            if (playerExperience < 0) {
                BlockStorageAPI.set(location.get(), Keys.BS_CHARGING, false);
                experience += playerExperience;
                playerExperience = 0;
            }

            if (experience > settings.getExperienceCapacity()) {
                playerExperience += experience - settings.getExperienceCapacity();
                experience = 0;
            }
        }

        if (discharging) {
            playerExperience += settings.getExperienceTransferRate();
            experience -= settings.getExperienceTransferRate();

            if (experience == 0) {
                BlockStorageAPI.set(location.get(), Keys.BS_DISCHARGING, false);
                return;
            }

            if (experience < 0) {
                BlockStorageAPI.set(location.get(), Keys.BS_DISCHARGING, false);
                experience += playerExperience;
                playerExperience = 0;
            }
        }

        player.setTotalExperience(playerExperience);
        BlockStorageAPI.set(location.get(), Keys.BS_EXPERIENCE, experience);
        updateConcreteTransformation(location.get());
        setPanelHidden(group, experience == 0);
        updatePanel(group);
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group) {
        if (doBurnoutCheck(group, "input")) {
            return;
        }

        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        final Optional<Link> inputLink = getLink(location.get(), "input");
        BlockStorageAPI.set(location.get(), Keys.BS_POWERED, inputLink.isPresent() && settings.isOperational(inputLink.get()));
    }

    private Matrix4f getConcreteTransformationMatrix(final double charge) {
        return Transformations.adjustedRotateScale(
                new Vector3f(maxConcreteDisplaySize).mul((float)(charge/settings.getCapacity())),
                Transformations.GENERIC_ROTATION_ANGLES);
    }

    private void updateConcreteTransformation(final Location location) {
        final double charge = BlockStorageAPI.getDouble(location, Keys.BS_CHARGE);
        final Optional<Display> concreteDisplay = getDisplay(location, "concrete");
        concreteDisplay.ifPresent(display -> display.setTransformationMatrix(getConcreteTransformationMatrix(charge)));
    }
}
