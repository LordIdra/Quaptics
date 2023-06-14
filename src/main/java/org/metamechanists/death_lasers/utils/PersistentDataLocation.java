package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.death_lasers.DEATH_LASERS;

import java.nio.ByteBuffer;
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
        ByteBuffer buffer = ByteBuffer.wrap(new byte[48]);
        buffer.putLong(complex.getWorld().getUID().getMostSignificantBits());
        buffer.putLong(complex.getWorld().getUID().getLeastSignificantBits());
        buffer.putDouble(complex.getX());
        buffer.putDouble(complex.getY());
        buffer.putDouble(complex.getZ());
        return buffer.array();
    }

    @NotNull
    @Override
    public Location fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteBuffer buffer = ByteBuffer.wrap(primitive);
        final long msb = buffer.getLong();
        final long lsb = buffer.getLong();
        final double x = buffer.getDouble();
        final double y = buffer.getDouble();
        final double z = buffer.getDouble();
        final UUID uuid = new UUID(msb, lsb);
        return new Location(DEATH_LASERS.getInstance().getServer().getWorld(uuid), x, y, z);
    }
}

