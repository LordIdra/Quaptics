package org.metamechanists.death_lasers.implementation;

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
        ByteBuffer buffer = ByteBuffer.wrap(new byte[28]);
        buffer.putLong(complex.getWorld().getUID().getMostSignificantBits());
        buffer.putLong(complex.getWorld().getUID().getLeastSignificantBits());
        buffer.putInt(complex.getBlockX());
        buffer.putInt(complex.getBlockY());
        buffer.putInt(complex.getBlockZ());
        return buffer.array();
    }

    @NotNull
    @Override
    public Location fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        ByteBuffer buffer = ByteBuffer.wrap(primitive);
        final long msb = buffer.getLong();
        final long lsb = buffer.getLong();
        final int x = buffer.getInt();
        final int y = buffer.getInt();
        final int z = buffer.getInt();
        final UUID uuid = new UUID(msb, lsb);
        return new Location(DEATH_LASERS.getInstance().getServer().getWorld(uuid), x, y, z);
    }
}

