package org.metamechanists.quaptics.implementation.blocks.upgraders;

import dev.sefiraat.sefilib.entity.display.DisplayGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metamechanists.quaptics.connections.ConnectionGroup;
import org.metamechanists.quaptics.connections.ConnectionPoint;
import org.metamechanists.quaptics.connections.ConnectionPointType;
import org.metamechanists.quaptics.connections.Link;
import org.metamechanists.quaptics.implementation.Settings;
import org.metamechanists.quaptics.implementation.attachments.ItemHolderBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerAnimatedBlock;
import org.metamechanists.quaptics.implementation.attachments.PowerLossBlock;
import org.metamechanists.quaptics.implementation.base.ConnectedBlock;
import org.metamechanists.quaptics.items.Lore;
import org.metamechanists.quaptics.items.Tier;
import org.metamechanists.quaptics.items.groups.CraftingComponents;
import org.metamechanists.quaptics.utils.BlockStorageAPI;
import org.metamechanists.quaptics.utils.Keys;
import org.metamechanists.quaptics.utils.Language;
import org.metamechanists.quaptics.utils.Utils;
import org.metamechanists.quaptics.utils.id.complex.ConnectionGroupId;
import org.metamechanists.quaptics.utils.id.complex.ConnectionPointId;
import org.metamechanists.quaptics.utils.models.ModelBuilder;
import org.metamechanists.quaptics.utils.models.components.ModelCuboid;
import org.metamechanists.quaptics.utils.models.components.ModelItem;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;


public class Polariser extends ConnectedBlock implements PowerAnimatedBlock, PowerLossBlock, ItemHolderBlock {
    public static final Settings POLARISER_SETTINGS = Settings.builder()
            .tier(Tier.ADVANCED)
            .maxPowerHidden(true)
            .minPower(200)
            .powerLoss(0.12)
            .build();
    public static final SlimefunItemStack POLARISER = new SlimefunItemStack(
            "QP_POLARISER",
            Material.YELLOW_TERRACOTTA,
            "&cPolariser",
            Lore.create(POLARISER_SETTINGS,
                    "&7● Increases the phase of quaptic rays",
                    "&7● The increase in phase depends on the phase crystal",
                    "&7  that the polariser is holding",
                    "&7● &eRight Click &7an item to insert",
                    "&7● &eRight Click &7again to retrieve"));

    private static final Vector MAIN_INPUT_LOCATION = new Vector(0.0F, 0.0F, -0.45F);
    private static final Vector OUTPUT_LOCATION = new Vector(0.0F, 0.0F, 0.45);

    private static final Map<ItemStack, Integer> PHASE_CHANGES = Map.of(
            CraftingComponents.PHASE_CRYSTAL_1, 1,
            CraftingComponents.PHASE_CRYSTAL_5, 5,
            CraftingComponents.PHASE_CRYSTAL_15, 15,
            CraftingComponents.PHASE_CRYSTAL_45, 45,
            CraftingComponents.PHASE_CRYSTAL_90, 90);

    public Polariser(final ItemGroup itemGroup, final SlimefunItemStack item, final RecipeType recipeType, final ItemStack[] recipe, final Settings settings) {
        super(itemGroup, item, recipeType, recipe, settings);
    }

    @Override
    protected float getConnectionRadius() {
        return 0.0F;
    }
    @Override
    public void connect(@NotNull final ConnectionPointId from, @NotNull final ConnectionPointId to) {}
    @Override
    protected DisplayGroup initModel(final @NotNull Location location, final @NotNull Player player) {
        return new ModelBuilder()
                .add("main", new ModelCuboid()
                        .material(Material.YELLOW_TERRACOTTA)
                        .facing(player.getFacing())
                        .size(0.3F, 0.3F, 0.9F))
                .add("prism", new ModelCuboid()
                        .material(Material.GRAY_CONCRETE)
                        .brightness(Utils.BRIGHTNESS_OFF)
                        .facing(player.getFacing())
                        .rotation(Math.PI/4)
                        .size(0.5F))
                .add("item", new ModelItem()
                        .brightness(Utils.BRIGHTNESS_ON)
                        .facing(player.getFacing())
                        .location(0, 0.3F, 0)
                        .size(0.5F))
                .add("item2", new ModelItem()
                        .brightness(Utils.BRIGHTNESS_ON)
                        .facing(player.getFacing())
                        .rotation(Math.PI/2)
                        .location(0, 0.3F, 0)
                        .size(0.5F))
                .buildAtBlockCenter(location);
    }
    @Override
    protected List<ConnectionPoint> initConnectionPoints(final ConnectionGroupId groupId, final Player player, final Location location) {
        return List.of(
                new ConnectionPoint(ConnectionPointType.INPUT, groupId, "input", formatPointLocation(player, location, MAIN_INPUT_LOCATION)),
                new ConnectionPoint(ConnectionPointType.OUTPUT, groupId, "output", formatPointLocation(player, location, OUTPUT_LOCATION)));
    }

    @Override
    public void onInputLinkUpdated(@NotNull final ConnectionGroup group, @NotNull final Location location) {
        if (!BlockStorageAPI.getBoolean(location, Keys.BS_IS_HOLDING_ITEM)) {
            onPoweredAnimation(location, false);
            return;
        }

        final Optional<ItemStack> itemStack = ItemHolderBlock.getStack(location, "item");
        if (itemStack.isEmpty()) {
            return;
        }

        updateOutput(location, itemStack.get());
    }
    @Override
    public void onPoweredAnimation(final @NotNull Location location, final boolean powered) {
        brightnessAnimation(location, "prism", powered);
    }
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void onBreak(@NotNull final Location location) {
        super.onBreak(location);
        onBreakItemHolderBlock(location, "item");
    }
    @Override
    protected boolean onRightClick(final @NotNull Location location, final @NotNull Player player) {
        itemHolderInteract(location, "item", player);
        return true;
    }
    @Override
    public boolean onInsert(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack, @NotNull final Player player) {
        final Optional<Integer> phaseChange = getPhaseChange(stack);
        if (phaseChange.isEmpty()) {
            Language.sendLanguageMessage(player, "polariser.not-phase-crystal");
            return false;
        }
        getItemDisplay(location, "item2").ifPresent(display -> display.setItemStack(stack.clone()));
        updateOutput(location, stack);
        return true;
    }
    @Override
    public Optional<ItemStack> onRemove(@NotNull final Location location, @NotNull final String name, @NotNull final ItemStack stack) {
        getItemDisplay(location, "item2").ifPresent(display -> display.setItemStack(new ItemStack(Material.AIR)));
        updateOutput(location, null);
        return Optional.of(stack);
    }

    private void updateOutput(@NotNull final Location location, @Nullable final ItemStack itemStack) {
        final Optional<Link> inputLink = getLink(location, "input");
        final Optional<Link> outputLink = getLink(location, "output");
        final boolean powered = inputLink.isPresent() && settings.isOperational(inputLink) && itemStack != null;
        onPoweredAnimation(location, powered);

        if (outputLink.isEmpty()) {
            return;
        }

        if (!powered) {
            outputLink.ifPresent(Link::disable);
            return;
        }

        final Optional<Integer> phaseChange = getPhaseChange(itemStack);
        if (phaseChange.isEmpty()) {
            return;
        }

        outputLink.get().setPowerFrequencyPhase(
                PowerLossBlock.calculatePowerLoss(settings, inputLink.get()),
                inputLink.get().getFrequency(),
                inputLink.get().getPhase() + phaseChange.get());
    }
    private static Optional<Integer> getPhaseChange(final @NotNull ItemStack itemStack) {
        for (final Entry<ItemStack, Integer> entry : PHASE_CHANGES.entrySet()) {
            if (SlimefunUtils.isItemSimilar(entry.getKey(), itemStack, true)) {
                return Optional.ofNullable(entry.getValue());
            }
        }
        return Optional.empty();
    }
}
