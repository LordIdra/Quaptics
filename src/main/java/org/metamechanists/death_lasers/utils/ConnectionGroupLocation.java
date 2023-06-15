package org.metamechanists.death_lasers.utils;

import org.bukkit.Location;

public class ConnectionGroupLocation {
    public Location location;
    public ConnectionGroupLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }
}
