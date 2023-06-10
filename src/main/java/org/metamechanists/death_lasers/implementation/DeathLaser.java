package org.metamechanists.death_lasers.implementation;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.lasers.Lasers;
import org.metamechanists.death_lasers.lasers.beam.Beam;
import org.metamechanists.death_lasers.lasers.beam.BlockDisplayBeam;
import org.metamechanists.death_lasers.lasers.storage.BeamGroup;
import org.metamechanists.death_lasers.lasers.storage.BeamStorage;
import org.metamechanists.death_lasers.lasers.ticker.factory.LinearTimeTickerFactory;



public class DeathLaser extends LaserEmitter {
    private final Vector BLOCK_CENTER_VECTOR = new Vector(0.4F, 0.4F, 0.4F);
    private final String MAIN_BEAM = "MAIN";

    public DeathLaser(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
        super(group, item, recipeType, recipe, capacity, consumption);
    }

    @Override
    protected void powerOn(Location source) {
        BeamStorage.setBeamGroupPowered(source, true);
    }

    @Override
    public void updateBeamGroup(Location source, Location target) {
        if (BeamStorage.hasBeamGroup(source)) {
            BeamStorage.deprecateBeamGroup(source);
        }

        final Beam linearRedBeam = new BlockDisplayBeam(
                new LinearTimeTickerFactory(
                        Lasers.testDisplay(source.clone().add(BLOCK_CENTER_VECTOR)),
                        source.clone().add(BLOCK_CENTER_VECTOR),
                        target.clone().add(BLOCK_CENTER_VECTOR),
                        20),
                Lasers.testTimer,
                false);

        BeamStorage.setBeamGroup(source, new BeamGroup(MAIN_BEAM, linearRedBeam));
    }
}
