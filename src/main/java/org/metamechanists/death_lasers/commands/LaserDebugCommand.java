package org.metamechanists.death_lasers.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.joml.Vector3f;
import org.metamechanists.death_lasers.utils.DisplayUtils;

@CommandAlias("laserdebug")
public class LaserDebugCommand extends BaseCommand {

    @Subcommand("createblockdisplay")
    @Syntax("<material> <scale> <rotationX> <rotationY> <rotationZ> &7- Create block display with specified parameters")
    public void createBlockDisplay(Player player, String[] args) {
        if (args.length != 5) {
            return;
        }

        final Material material = Material.getMaterial(args[0]);
        final float scale = Float.parseFloat(args[1]);
        final float rotationX = Float.parseFloat(args[2]);
        final float rotationY = Float.parseFloat(args[3]);
        final float rotationZ = Float.parseFloat(args[4]);

        final BlockDisplay display = player.getWorld().spawn(player.getLocation(), BlockDisplay.class);
        display.setBlock(material.createBlockData());
        display.setTransformationMatrix(DisplayUtils.rotationTransformation(
                new Vector3f(scale, scale, scale),
                new Vector3f(rotationX, rotationY, rotationZ)));
    }
}
