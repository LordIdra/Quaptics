package org.metamechanists.death_lasers.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.DEATH_LASERS;

import java.util.UUID;

public class PersistentDataLocation implements PersistentDataType<byte[], Location> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<Location> getComplexType() {
        return Location.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull Location complex, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        final ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeLong(complex.getWorld().getUID().getMostSignificantBits());
        output.writeLong(complex.getWorld().getUID().getLeastSignificantBits());
        output.writeDouble(complex.getX());
        output.writeDouble(complex.getY());
        output.writeDouble(complex.getZ());
        return output.toByteArray();
    }

    @NotNull
    @Override
    public Location fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        final ByteArrayDataInput input = ByteStreams.newDataInput(primitive);
        final long msb = input.readLong();
        final long lsb = input.readLong();
        final double x = input.readDouble();
        final double y = input.readDouble();
        final double z = input.readDouble();
        final UUID uuid = new UUID(msb, lsb);
        return new Location(DEATH_LASERS.getInstance().getServer().getWorld(uuid), x, y, z);
    }
}

