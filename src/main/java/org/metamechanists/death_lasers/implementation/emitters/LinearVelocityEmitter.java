package org.metamechanists.death_lasers.implementation.emitters;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.metamechanists.death_lasers.lasers.Lasers;
import org.metamechanists.death_lasers.lasers.beam.Beam;
import org.metamechanists.death_lasers.lasers.beam.IntervalBlockDisplayBeam;
import org.metamechanists.death_lasers.lasers.BeamGroup;
import org.metamechanists.death_lasers.lasers.BeamStorage;
import org.metamechanists.death_lasers.lasers.ticker.factory.interval.IntervalLinearVelocityTickerFactory;


public class LinearVelocityEmitter extends LaserEmitter {
    private final Vector BLOCK_CENTER_VECTOR = new Vector(0.5F, 0.5F, 0.5F);
    private final String MAIN_BEAM = "MAIN";

    public LinearVelocityEmitter(ItemGroup group, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, int capacity, int consumption) {
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

        final Beam linearRedBeam = new IntervalBlockDisplayBeam(
                new IntervalLinearVelocityTickerFactory(
                        Lasers.testDisplay(),
                        source.clone().add(BLOCK_CENTER_VECTOR),
                        target.clone().add(BLOCK_CENTER_VECTOR),
                        0.2F),
                Lasers.testTimer(),
                false);

        BeamStorage.setBeamGroup(source, new BeamGroup(MAIN_BEAM, linearRedBeam));
    }
}
