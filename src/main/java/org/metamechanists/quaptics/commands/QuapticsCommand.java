package org.metamechanists.quaptics.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Private;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.quaptics.implementation.tools.QuapticChargeableItem;
import org.metamechanists.quaptics.storage.QuapticTicker;
import org.metamechanists.quaptics.utils.Language;


@CommandAlias("qp|quaptics")
@SuppressWarnings("unused")
public class QuapticsCommand extends BaseCommand {
    @HelpCommand
    @Syntax("")
    @Private
    public static void helpCommand(final CommandSender sender, @NotNull final CommandHelp help) {
        help.showHelp();
    }

    @Subcommand("charge")
    @Syntax("<amount> - &7Charges the item held in your hand")
    @CommandPermission("quaptics.command.charge")
    public static void charge(final Player player, final String @NotNull [] args) {
        if (args.length < 1) {
            Language.sendLanguageMessage(player, "command.invalid-argument-count");
            return;
        }

        final ItemStack itemStack = player.getInventory().getItem(EquipmentSlot.HAND);
        if (!(SlimefunItem.getByItem(itemStack) instanceof final QuapticChargeableItem chargeableItem)) {
            Language.sendLanguageMessage(player, "command.charge.cannot-be-charged");
            return;
        }

        if (!args[0].matches("-?\\d+")) {
            Language.sendLanguageMessage(player, "command.charge.not-an-integer");
            return;
        }

        chargeableItem.chargeItem(Integer.valueOf(args[0]).doubleValue(), itemStack, QuapticTicker.TICKS_PER_SECOND);
        QuapticChargeableItem.updateLore(itemStack);
    }
}
