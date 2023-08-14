package org.metamechanists.quaptics.implementation.multiblocks.beacons.controllers;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.attachments.ComplexMultiblock;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.BeaconModule;
import org.metamechanists.quaptics.implementation.multiblocks.beacons.modules.PlayerModule;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.storage.PersistentDataTraverser;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.builders.InteractionBuilder;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.simple.InteractionId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public abstract class BeaconController extends ConnectedBlock implements ItemHolderBlock, ComplexMultiblock, PowerAnimatedBlock {
    private static final float MODULE_BUTTON_WIDTH = 0.20F;
    private static final float MODULE_BUTTON_HEIGHT = 0.42F;
    private static final Vector3f MODULE_BUTTON_OFFSET = new Vector3f(0, 0.13F, 0);

    protected BeaconController(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0;
    }
    @Override
    protected DisplayGroup initModel(@NotNull final Location location, @NotNull final Player player) {
        final DisplayGroup displayGroup = new ModelBuilder().buildAtBlockCenter(location);
        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroup.getParentUUID());
        traverser.set(Keys.BS_PLAYER_RECEIVERS, new ArrayList<>());
        return displayGroup;
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of();
    }
    public static @NotNull ItemStack emptyItemStack() {
        final ItemStack stack = new ItemStack(Material.BLACK_BANNER);
        final BannerMeta meta = (BannerMeta) stack.getItemMeta();
        meta.addPattern(new Pattern(DyeColor.RED, PatternType.CIRCLE_MIDDLE));
        stack.setItemMeta(meta);
        return stack;
    }
    @Override
    public @NotNull ItemStack getEmptyItemStack() {
        return emptyItemStack();
    }
    @Override
    public boolean isEmptyItemStack(final @NotNull ItemStack itemStack) {
        return itemStack.equals(getEmptyItemStack());
    }
    @Override
    protected boolean isTicker() {
        return true;
    }

    @Override
    public void onTick22(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final boolean isStructureValid = isStructureValid(location.getBlock());
        BlockStorageAPI.set(location, Keys.BS_MULTIBLOCK_INTACT, isStructureValid);
        if (!isStructureValid) {
            onPoweredAnimation(location, false);
            return;
        }

        final Location powerSupplyLocation = location.clone().add(getPowerSupplyLocation());
        final double power = BlockStorageAPI.getDouble(powerSupplyLocation, Keys.BS_POWER);
        final double frequency = BlockStorageAPI.getDouble(powerSupplyLocation, Keys.BS_FREQUENCY);
        final boolean powered = power >= settings.getMinPower() && frequency > settings.getMinFrequency();
        BlockStorageAPI.set(location, Keys.BS_POWERED, powered);
        onPoweredAnimation(location, powered);
        if (!powered) {
            return;
        }

        final Set<BeaconModule> modules = getModules(location);
        tickPlayerModules(location, modules);
    }
    @Override
    public void onTick102(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        final Optional<DisplayGroup> displayGroup = getDisplayGroup(location);
        if (displayGroup.isEmpty()) {
            return;
        }

        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroup.get().getParentUUID());
        traverser.set(Keys.BS_PLAYER_RECEIVERS, new ArrayList<>());

        if (!BlockStorageAPI.getBoolean(location, Keys.BS_MULTIBLOCK_INTACT) || !BlockStorageAPI.getBoolean(location, Keys.BS_POWERED)) {
            return;
        }

        traverser.set(Keys.BS_PLAYER_RECEIVERS, getNearbyPlayerUuids(location));
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        getModuleDisplayNames().forEach(name -> breakModuleSlot(location, name));
        getDisplayGroup(location).ifPresent(BeaconController::breakInteractions);
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player) {
        final SlimefunItem moduleItem = SlimefunItem.getByItem(stack);
        if (!(moduleItem instanceof final BeaconModule beaconModule)) {
            Language.sendLanguageMessage(player, "beacon.not-module");
            return false;
        }

        if (getModules(location).contains(beaconModule)) {
            Language.sendLanguageMessage(player, "beacon.duplicate-module");
            return false;
        }

        if (!Tier.greaterOrEqual(beaconModule.getSettings().getTier(), settings.getTier())) {
            Language.sendLanguageMessage(player, "beacon.incorrect-tier", settings.getTier().name);
            return false;
        }

        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack) {
        QuapticChargeableItem.updateLore(stack);
        return Optional.of(stack);
    }

    private List<UUID> getNearbyPlayerUuids(final @NotNull Location location) {
        final BoundingBox boundingBox = new BoundingBox(
                location.x() + settings.getRange() * 2, location.y() + 1000, location.z() + settings.getRange() * 2,
                location.x() - settings.getRange() * 2, location.y() - 1000, location.z() - settings.getRange() * 2);

        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> boundingBox.contains(player.getLocation().toVector()))
                .map(Entity::getUniqueId)
                .collect(Collectors.toList());
        //return location.getNearbyPlayers(settings.getRange()).stream().map(Entity::getUniqueId).toList();
    }
    private static @NotNull List<Player> getStoredPlayers(final @NotNull Location location) {
        final Optional<DisplayGroup> displayGroup = getDisplayGroup(location);
        if (displayGroup.isEmpty()) {
            return new ArrayList<>();
        }

        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroup.get().getParentUUID());
        final List<UUID> uuids = traverser.getUuidList(Keys.BS_PLAYER_RECEIVERS);
        if (uuids == null) {
            return new ArrayList<>();
        }

        return uuids.stream().map(Bukkit::getPlayer).toList();
    }

    protected static @NotNull InteractionId createButton(final ConnectionGroupId groupId, final @NotNull Location location, final Vector3f relativeLocation, final String slot) {
        final Interaction interaction = new InteractionBuilder()
                .width(MODULE_BUTTON_WIDTH)
                .height(MODULE_BUTTON_HEIGHT)
                .build(location.clone()
                        .toCenterLocation()
                        .add(Vector.fromJOML(MODULE_BUTTON_OFFSET))
                        .add(Vector.fromJOML(relativeLocation)));

        final PersistentDataTraverser traverser = new PersistentDataTraverser(interaction.getUniqueId());
        traverser.set("groupId", groupId);
        traverser.set("slot", slot);

        return new InteractionId(interaction.getUniqueId());
    }

    private void breakModuleSlot(@NotNull final Location location, final String name) {
        final Optional<ItemStack> stack = ItemHolderBlock.getStack(location, name);
        if (stack.isEmpty() || stack.get().equals(getEmptyItemStack())) {
            return;
        }
        onBreakItemHolderBlock(location, name);
    }
    private static void breakInteractions(@NotNull final DisplayGroup displayGroup) {
        final PersistentDataTraverser traverser = new PersistentDataTraverser(displayGroup.getParentUUID());
        final List<InteractionId> interactionIds = traverser.getCustomIdList(Keys.BS_INTERACTION_ID_LIST);
        if (interactionIds == null) {
            return;
        }

        interactionIds.stream()
                .map(InteractionId::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(Entity::remove);
    }

    public void interact(final Player player, final @NotNull ConnectionGroup group, final String slot) {
        final Optional<Location> location = group.getLocation();
        if (location.isEmpty()) {
            return;
        }

        itemHolderInteract(location.get(), slot, player);
    }

    private static Optional<BeaconModule> getModule(final ItemStack itemStack) {
        final SlimefunItem slimefunItem = SlimefunItem.getByItem(itemStack);
        if (slimefunItem instanceof final BeaconModule beaconModule) {
            return Optional.of(beaconModule);
        }
        return Optional.empty();
    }
    private Set<BeaconModule> getModules(@NotNull final Location location) {
        return getModuleDisplayNames().stream()
                .map(name -> ItemHolderBlock.getStack(location, name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(BeaconController::getModule)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private void tickPlayerModules(final @NotNull Location location, final @NotNull Set<BeaconModule> modules) {
        final Set<PlayerModule> playerModules = modules.stream()
                .filter(module -> module instanceof PlayerModule)
                .map(PlayerModule.class::cast)
                .collect(Collectors.toSet());
        if (!playerModules.isEmpty()) {
            playerModules.forEach(module -> module.apply(this, location, getStoredPlayers(location)));
        }
    }

    public abstract Vector getPowerSupplyLocation();
    protected abstract List<String> getModuleDisplayNames();
}
